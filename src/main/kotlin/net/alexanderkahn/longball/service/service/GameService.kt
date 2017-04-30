package net.alexanderkahn.longball.service.service

import net.alexanderkahn.longball.service.model.*
import net.alexanderkahn.longball.service.persistence.model.entity.*
import net.alexanderkahn.longball.service.persistence.repository.*
import net.alexanderkahn.longball.service.service.assembler.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.OffsetDateTime

@Service
class GameService(@Autowired private val gameRepository: GameRepository,
                  @Autowired private val lineupPlayerRepository: LineupPlayerRepository,
                  @Autowired private val inningRepository: InningRepository,
                  @Autowired private val inningHalfRepository: InningHalfRepository,
                  @Autowired private val plateAppearanceRepository: PlateAppearanceRepository,
                  @Autowired private val gameplayEventRepository: GameplayEventRepository,
                  @Autowired private val basepathResultRepository: BasepathResultRepository) {

    fun get(id: Long): Game {
        val game = gameRepository.findByIdAndOwner(id)
        return game.toModel()
    }

    fun getAll(pageable: Pageable): Page<Game> {
        val games = gameRepository.findByOwner(pageable)
        return games.map { it.toModel() }
    }

    fun getLineupPlayers(pageable: Pageable, gameId: Long, inningHalf: InningHalf): Page<LineupPlayer> {
        val game = gameRepository.findByIdAndOwner(gameId)
        val players = lineupPlayerRepository.findByGameAndInningHalfAndOwner(pageable, game, inningHalf)
        return players.map { it.toModel() }
    }

    fun getCurrentPlateAppearance(gameId: Long): PlateAppearance {
        //TODO: this seems like it could be simplified a lot with a custom query
        val game = gameRepository.findByIdAndOwner(gameId)
        val currentInning: PxInning = inningRepository.findFirstByGameAndOwnerOrderByIdDesc(game) ?: throw Exception()
        val currentHalf: PxInningHalf = inningHalfRepository.findFirstByInningAndOwnerOrderByIdDesc(currentInning) ?: throw Exception()
        val appearance: PxPlateAppearance = plateAppearanceRepository.findFirstByInningHalfAndOwnerOrderByIdDesc(currentHalf) ?: throw Exception()
        val inningAppearances = plateAppearanceRepository.findByInningHalfAndOwner(appearance.inningHalf)
        return getPlateAppearanceModel(inningAppearances, appearance)
    }

    private fun getOpposingPitcher(appearance: PxPlateAppearance): PxPlayer {
        val oppositeHalf = if (appearance.inningHalf.half == InningHalf.TOP) InningHalf.BOTTOM else InningHalf.TOP
        val currentPitcher = getPlayerByPosition(appearance.inningHalf.inning.game, oppositeHalf, FieldPosition.PITCHER)
        return currentPitcher
    }

    //TODO: handle basepathResults
    fun addGameplayEvent(gameId: Long, gameplayEvent: GameplayEvent): PlateAppearance {
        val game = gameRepository.findByIdAndOwner(gameId)
        val appearance = getOrCreatePlateAppearance(game)
        val pxEvent = gameplayEvent.toPersistence(null, appearance)
        validateEvent(gameplayEvent, appearance, game)
        gameplayEventRepository.save(pxEvent)

        val appearanceEvents = gameplayEventRepository.findByPlateAppearanceAndOwner(appearance).toMutableList()
        appearanceEvents.add(pxEvent)

        val inningAppearances = plateAppearanceRepository.findByInningHalfAndOwner(appearance.inningHalf)

        processAppearanceResult(appearance, appearanceEvents)
        proccessInningHalfResult(appearance.inningHalf, inningAppearances)
        processGameResult(game)
        gameRepository.save(game)
        return getPlateAppearanceModel(inningAppearances, appearance)
    }

    private fun validateEvent(gameplayEvent: GameplayEvent, appearance: PxPlateAppearance, game: PxGame) {
        if (game.result != null) {
            throw Exception("Can't add a result to a game that's already over")
        }
        val previousPitches = gameplayEventRepository.findByPlateAppearanceAndOwner(appearance)
        if (gameplayEvent.basepathResults.isNotEmpty() && pitchEndsAtBatWithoutHit(gameplayEvent, previousPitches) && gameplayEvent.basepathResults.isNotEmpty()) {
            throw Exception("Cannot record basepath event on at-bat-ending pitch")
        }
        validateOnBase(gameplayEvent, appearance)
    }

    private fun validateOnBase(gameplayEvent: GameplayEvent, appearance: PxPlateAppearance) {
        if (gameplayEvent.pitch == Pitch.IN_PLAY && gameplayEvent.basepathResults.none { appearance.batter.id == it.lineupPlayer }) {
            throw Exception("In play pitch must include a baserunning result for the batter")
        }
        if (gameplayEvent.basepathResults.map { it.lineupPlayer }.distinct().count() != gameplayEvent.basepathResults.count()) {
            throw Exception("Basepath results cannot contain the same player multiple times")
        }

        //TODO: there's gonna be a bug here, I can smell it
        val onBasePlayerIds = getCurrentOnBase(appearance.inningHalf).map { it.lineupPlayer.id ?: throw Exception() }
        if (gameplayEvent.basepathResults.any { it.lineupPlayer != appearance.batter.id && !onBasePlayerIds.contains(it.lineupPlayer) }) {
            throw Exception("Basepath runners may not swap order")
        }

        //TODO: assert same order as it was before (maybe just (wrapped) order of battingorder)
    }

    private fun pitchEndsAtBatWithoutHit(gameplayEvent: GameplayEvent, previousEvents: List<PxGameplayEvent>): Boolean {
        return gameplayEvent.pitch == Pitch.HIT_BY_PITCH ||
                gameplayEvent.pitch == Pitch.BALL && previousEvents.balls == LeagueRuleSet.BALLS_PER_WALK - 1 ||
                gameplayEvent.pitch in arrayOf(Pitch.STRIKE_LOOKING, Pitch.STRIKE_SWINGING) && previousEvents.strikes == LeagueRuleSet.STRIKES_PER_OUT - 1
    }

    private fun processGameResult(game: PxGame) {
        val innings = inningRepository.findByGameAndOwner(game)
        //TODO: Wow, this is tortured. Also, not entirely right. Maybe the game is tied. Maybe the bottom half doesn't need to be played.
        if (innings.count { inningIsComplete(it) } >= LeagueRuleSet.INNINGS_PER_GAME) {
            game.result = PxGameResult(game, OffsetDateTime.now())
        }
    }

    private fun inningIsComplete(inning: PxInning): Boolean {
        val inningHalves = inningHalfRepository.findByInningAndOwner(inning)
        return inningHalves.count { it.result != null } >= 2
    }

    private fun getPlateAppearanceModel(inningAppearances: MutableList<PxPlateAppearance>, appearance: PxPlateAppearance): PlateAppearance {
        val basepathResults = basepathResultRepository.findByPlateAppearanceAndOwner(inningAppearances)
        val events = gameplayEventRepository.findByPlateAppearanceAndOwner(appearance)
        val outs = inningAppearances.getOuts(basepathResults)
        val hits = basepathResults.hits
        val walks = basepathResults.walks
        val errors = basepathResults.errors
        val runs = basepathResults.runs
        val balls = events.balls
        val strikes = events.strikes
        return appearance.toModel(getOpposingPitcher(appearance), outs,hits,walks,errors,runs, balls, strikes, basepathResults)
    }

    private fun proccessInningHalfResult(inningHalf: PxInningHalf, plateAppearances: List<PxPlateAppearance>) {
        val basepathResults = basepathResultRepository.findByPlateAppearanceAndOwner(plateAppearances)
        if (plateAppearances.getOuts(basepathResults) >= LeagueRuleSet.OUTS_PER_INNING) {
            inningHalf.result = inningHalf.toResult(basepathResults)
            inningHalfRepository.save(inningHalf)
        }
    }

    private fun processAppearanceResult(appearance: PxPlateAppearance, pitchEvents: List<PxGameplayEvent>) {
        if (shouldAddResult(pitchEvents)) {
            val lastPitch = pitchEvents.last()
            appearance.plateAppearanceResult = getPlateAppearanceResult(lastPitch)
            when(appearance.plateAppearanceResult) {
                PlateAppearanceResult.BASE_ON_BALLS, PlateAppearanceResult.HIT_BY_PITCH -> advanceRunners(lastPitch, appearance, getCurrentOnBase(appearance.inningHalf))
                else -> {}
            }
        }
    }

    private fun getCurrentOnBase(inningHalf: PxInningHalf): List<PxBasepathResult> {
        val plateAppearances = plateAppearanceRepository.findByInningHalfAndOwner(inningHalf)
        val results = plateAppearances.flatMap { gameplayEventRepository.findByPlateAppearanceAndOwner(it) }
                .flatMap { basepathResultRepository.findByGameplayEventAndOwner(it) }
        return results.sortedByDescending { it.id }.distinctBy { it.lineupPlayer }
                .filterNot { it.location == PlayLocation.HOME || it.playResult == PlayResult.OUT }
    }

    private fun advanceRunners(lastPitch: PxGameplayEvent, appearance: PxPlateAppearance, currentOnBase: List<PxBasepathResult>) {
        for (base in PlayLocation.values().sortedArrayDescending()) {
            val onBase = currentOnBase.firstOrNull { it.location == base }
            if (onBase != null && currentOnBase.any{ it.location == PlayLocation.FIRST || it.location == PlayLocation.values()[base.ordinal - 1]}) {
                basepathResultRepository.save(PxBasepathResult(lastPitch, onBase.lineupPlayer, PlayLocation.values()[base.ordinal + 1], PlayResult.SAFE))
            }
        }
        basepathResultRepository.save(PxBasepathResult(lastPitch, appearance.batter, PlayLocation.FIRST, PlayResult.SAFE))
    }

    private fun shouldAddResult(events: List<PxGameplayEvent>): Boolean {
        if (events.isNotEmpty() && events.last().pitch == Pitch.HIT_BY_PITCH) {
            return true
        }
        return events.strikes >= LeagueRuleSet.STRIKES_PER_OUT ||
                events.balls >= LeagueRuleSet.BALLS_PER_WALK
    }

    private fun getPlateAppearanceResult(lastEvent: PxGameplayEvent): PlateAppearanceResult {
        when(lastEvent.pitch) {
            Pitch.BALL -> return PlateAppearanceResult.BASE_ON_BALLS
            Pitch.STRIKE_SWINGING -> return PlateAppearanceResult.STRIKEOUT_SWINGING
            Pitch.STRIKE_LOOKING -> return PlateAppearanceResult.STRIKEOUT_LOOKING
            Pitch.HIT_BY_PITCH -> return PlateAppearanceResult.HIT_BY_PITCH
            else -> throw NotImplementedError()
        }
    }

    private fun getOrCreatePlateAppearance(game: PxGame): PxPlateAppearance {
        val inning: PxInning = getOrCreateInning(game)
        val inningHalf: PxInningHalf = getOrCreateInningHalf(inning)
        val appearance: PxPlateAppearance = getOrCreateAppearance(inningHalf)
        return appearance
    }

    private fun getOrCreateInning(game: PxGame): PxInning {
        var inning = inningRepository.findFirstByGameAndOwnerOrderByIdDesc(game)
        if (inning == null) {
            inning = PxInning(game, 1)
            inningRepository.save(inning)
        } else {
            val inningHalves = inningHalfRepository.findByInningAndOwner(inning)
            if (inningHalves.count { it.result != null } >= InningHalf.values().size) {
                inning = PxInning(game, inning.inningNumber + 1)
                inningRepository.save(inning)
            }
        }
        return inning
    }

    private fun getOrCreateInningHalf(inning: PxInning): PxInningHalf {
        var inningHalf = inningHalfRepository.findFirstByInningAndOwnerOrderByIdDesc(inning)
        if (inningHalf == null) {
            inningHalf = PxInningHalf(inning, InningHalf.TOP)
            inningHalfRepository.save(inningHalf)
        } else if (inningHalf.result != null) {
            inningHalf = PxInningHalf(inning, InningHalf.BOTTOM)
            inningHalfRepository.save(inningHalf)
        }
        return inningHalf
    }

    private fun getOrCreateAppearance(inningHalf: PxInningHalf): PxPlateAppearance {
        var appearance = plateAppearanceRepository.findFirstByInningHalfAndOwnerOrderByIdDesc(inningHalf)
        if (appearance == null) {
            val batter: PxLineupPlayer
            if (inningHalf.inning.inningNumber == 1) {
                batter = getPlayerByBattingOrder(inningHalf.inning.game, inningHalf.half, 1)
            } else {
                val lastInning = inningRepository.findByGameAndOwner(inningHalf.inning.game).single { it.inningNumber == inningHalf.inning.inningNumber - 1 }
                val lastHalf = inningHalfRepository.findByInningAndOwner(lastInning).single { it.half == inningHalf.half }
                val lastAppearance = plateAppearanceRepository.findByInningHalfAndOwner(lastHalf).last { it.plateAppearanceResult != null }
                batter = getPlayerByBattingOrder(lastInning.game, lastHalf.half, (lastAppearance.batter.battingOrder % LeagueRuleSet.BATTERS_PER_LINEUP) + 1)
            }
            appearance = PxPlateAppearance(inningHalf, batter)
            plateAppearanceRepository.save(appearance)
        } else {
            val pitchEvents = gameplayEventRepository.findByPlateAppearanceAndOwner(appearance)
            if (pitchEvents.isNotEmpty() && appearance.plateAppearanceResult != null) {
                val batter = getPlayerByBattingOrder(appearance.inningHalf.inning.game, appearance.inningHalf.half, (appearance.batter.battingOrder % LeagueRuleSet.BATTERS_PER_LINEUP) + 1)
                appearance = PxPlateAppearance(inningHalf, batter)
                plateAppearanceRepository.save(appearance)
            }
        }

        return appearance
    }

    private fun getPlayerByBattingOrder(game: PxGame, inningHalf: InningHalf, battingOrder: Int): PxLineupPlayer {
        return lineupPlayerRepository.findFirstByGameAndInningHalfAndBattingOrderAndOwner(game, inningHalf, battingOrder)
    }

    private fun getPlayerByPosition(game: PxGame, inningHalf: InningHalf, fieldPosition:FieldPosition): PxPlayer {
        return lineupPlayerRepository.findFirstByGameAndInningHalfAndFieldPositionAndOwner(game, inningHalf, fieldPosition).player
    }
}

