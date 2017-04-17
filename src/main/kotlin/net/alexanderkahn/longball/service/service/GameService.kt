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
                  @Autowired private val plateAppearanceRepository: PlateAppearanceRepository) {

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
        val game = gameRepository.findByIdAndOwner(gameId)
        val currentInning: PxInning = game.innings.last()
        val currentHalf = currentInning.inningHalves.last()
        val appearance = currentHalf.plateAppearances.last()
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
        val inningAppearances = plateAppearanceRepository.findByInningHalfAndOwner(appearance.inningHalf)
        val pxEvent = gameplayEvent.toPersistence(null, appearance)
        validateEvent(gameplayEvent, appearance, game)
        appearance.pitchEvents.add(pxEvent)

        processAppearanceResult(appearance)
        proccessInningHalfResult(appearance.inningHalf)
        processGameResult(game)
        gameRepository.save(game)
        return getPlateAppearanceModel(inningAppearances, appearance)
    }

    private fun validateEvent(gameplayEvent: GameplayEvent, appearance: PxPlateAppearance, game: PxGame) {
        if (game.result != null) {
            throw Exception("Can't add a result to a game that's already over")
        }
        if (gameplayEvent.basepathResults.isNotEmpty() && pitchEndsAtBatWithoutHit(gameplayEvent, appearance) && gameplayEvent.basepathResults.isNotEmpty()) {
            throw Exception("Cannot record basepath event on at-bat-ending pitch")
        }
        validateOnBase(gameplayEvent, appearance)
    }

    private fun validateOnBase(gameplayEvent: GameplayEvent, appearance: PxPlateAppearance) {
        if (gameplayEvent.pitch == Pitch.IN_PLAY && gameplayEvent.basepathResults.none { appearance.batter.player.id == it.player }) {
            throw Exception("In play pitch must include a baserunning result for the batter")
        }
        if (gameplayEvent.basepathResults.map { it.player }.distinct().count() != gameplayEvent.basepathResults.count()) {
            throw Exception("Basepath results cannot contain the same player multiple times")
        }

        val onBasePlayerIds = getCurrentOnBase(appearance.inningHalf).map { it.lineupPlayer.player.id ?: throw Exception() }.toMutableSet()
        if (gameplayEvent.basepathResults.any { it.player != appearance.batter.id && !onBasePlayerIds.contains(it.player) }) {
            throw Exception("Basepath runners may not swap order")
        }

        //TODO: assert same order as it was before (maybe just (wrapped) order of battingorder
    }

    private fun pitchEndsAtBatWithoutHit(gameplayEvent: GameplayEvent, appearance: PxPlateAppearance): Boolean {
        return gameplayEvent.pitch == Pitch.HIT_BY_PITCH ||
                gameplayEvent.pitch == Pitch.BALL && appearance.pitchEvents.balls == LeagueRuleSet.BALLS_PER_WALK - 1 ||
                gameplayEvent.pitch in arrayOf(Pitch.STRIKE_LOOKING, Pitch.STRIKE_SWINGING) && appearance.pitchEvents.strikes == LeagueRuleSet.STRIKES_PER_OUT - 1
    }

    private fun processGameResult(game: PxGame) {
        //TODO: Wow, this is tortured. Also, not entirely right. Maybe the game is tied. Maybe the bottom half doesn't need to be played.
        if (game.innings.count { it.inningHalves.count { it.result != null } >=2 } >= LeagueRuleSet.INNINGS_PER_GAME) {
            game.result = PxGameResult(game, OffsetDateTime.now())
        }
    }

    private fun getPlateAppearanceModel(inningAppearances: MutableList<PxPlateAppearance>, appearance: PxPlateAppearance): PlateAppearance {
        val outs = inningAppearances.outs
        val hits = inningAppearances.hits
        val walks = inningAppearances.walks
        val errors = inningAppearances.errors
        val runs = inningAppearances.runs
        val basePathResults = inningAppearances.currentOnBase
        return appearance.toModel(getOpposingPitcher(appearance), outs,hits,walks,errors,runs, basePathResults)
    }

    private fun proccessInningHalfResult(inningHalf: PxInningHalf) {
        if (inningHalf.plateAppearances.outs >= LeagueRuleSet.OUTS_PER_INNING) {
            inningHalf.result = inningHalf.toResult()
            inningHalfRepository.save(inningHalf)
        }
    }

    private fun processAppearanceResult(appearance: PxPlateAppearance) {
        if (shouldAddResult(appearance.pitchEvents)) {
            val lastPitch = appearance.pitchEvents.last()
            appearance.plateAppearanceResult = getPlateAppearanceResult(lastPitch)
            when(appearance.plateAppearanceResult) {
                PlateAppearanceResult.BASE_ON_BALLS, PlateAppearanceResult.HIT_BY_PITCH -> advanceRunners(lastPitch, appearance, getCurrentOnBase(appearance.inningHalf))
                else -> {}
            }
        }
    }

    private fun getCurrentOnBase(inningHalf: PxInningHalf): List<PxBasepathResult> {
        val results = inningHalf.plateAppearances.flatMap { it.pitchEvents }.flatMap { it.basepathResults }
        return results.sortedByDescending { it.id }.distinctBy { it.lineupPlayer }
                .filterNot { it.location == PlayLocation.HOME || it.playResult == PlayResult.OUT }
    }

    private fun advanceRunners(lastPitch: PxGameplayEvent, appearance: PxPlateAppearance, currentOnBase: List<PxBasepathResult>) {
        for (base in PlayLocation.values().sortedArrayDescending()) {
            val onBase = currentOnBase.firstOrNull { it.location == base }
            if (onBase != null && currentOnBase.any{ it.location == PlayLocation.FIRST || it.location == PlayLocation.values()[base.ordinal - 1]}) {
                lastPitch.basepathResults.add(PxBasepathResult(lastPitch, onBase.lineupPlayer, PlayLocation.values()[base.ordinal + 1], PlayResult.SAFE))
            }
        }
        lastPitch.basepathResults.add(PxBasepathResult(lastPitch, appearance.batter, PlayLocation.FIRST, PlayResult.SAFE))
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
        var inning: PxInning? = game.innings.lastOrNull()
        if (inning == null) {
            inning = PxInning(game, 1)
            inningRepository.save(inning) //TODO figure out how to get rid of BS like this
            game.innings.add(inning)
        } else if (inning.inningHalves.count { it.result != null } >= InningHalf.values().size ) {
            inning = PxInning(game, inning.inningNumber + 1)
            inningRepository.save(inning)
            game.innings.add(inning)
        }
        return inning
    }

    private fun getOrCreateInningHalf(inning: PxInning): PxInningHalf {
        var inningHalf: PxInningHalf? = inning.inningHalves.lastOrNull()
        if (inningHalf == null) {
            inningHalf = PxInningHalf(inning, InningHalf.TOP)
            inningHalfRepository.save(inningHalf)
            inning.inningHalves.add(inningHalf)
        } else if (inningHalf.result != null) {
            inningHalf = PxInningHalf(inning, InningHalf.BOTTOM)
            inningHalfRepository.save(inningHalf)
            inning.inningHalves.add(inningHalf)
        }
        return inningHalf
    }

    private fun getOrCreateAppearance(inningHalf: PxInningHalf): PxPlateAppearance {
        var appearance: PxPlateAppearance? = inningHalf.plateAppearances.lastOrNull()
        if (appearance == null) {
            val batter: PxLineupPlayer
            if (inningHalf.inning.inningNumber == 1) {
                batter = getPlayerByBattingOrder(inningHalf.inning.game, inningHalf.half, 1)
            } else {
                //TODO: I think these filters can be replaced with functional first()s and count()s
                val lastInning = inningHalf.inning.game.innings.filter { it.inningNumber == inningHalf.inning.inningNumber - 1 }.first()
                val lastHalfInning = lastInning.inningHalves.filter { it.half == inningHalf.half }.first()
                val lastAppearance = lastHalfInning.plateAppearances.filter { it.plateAppearanceResult != null }.last()
                batter = getPlayerByBattingOrder(lastInning.game, lastHalfInning.half, (lastAppearance.batter.battingOrder % LeagueRuleSet.BATTERS_PER_LINEUP) + 1)
            }
            appearance = PxPlateAppearance(inningHalf, batter)
            plateAppearanceRepository.save(appearance)
        } else if (appearance.pitchEvents.isNotEmpty() && appearance.plateAppearanceResult != null) {
            val batter = getPlayerByBattingOrder(appearance.inningHalf.inning.game, appearance.inningHalf.half, (appearance.batter.battingOrder % LeagueRuleSet.BATTERS_PER_LINEUP) + 1)
            appearance = PxPlateAppearance(inningHalf, batter)
            plateAppearanceRepository.save(appearance)

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

