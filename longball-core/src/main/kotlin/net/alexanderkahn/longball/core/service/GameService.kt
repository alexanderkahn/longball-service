package net.alexanderkahn.longball.core.service


import net.alexanderkahn.longball.core.assembler.pxUser
import net.alexanderkahn.longball.core.assembler.toModel
import net.alexanderkahn.longball.core.persistence.repository.GameRepository
import net.alexanderkahn.longball.core.persistence.repository.InningRepository
import net.alexanderkahn.longball.core.persistence.repository.LineupPlayerRepository
import net.alexanderkahn.longball.model.Game
import net.alexanderkahn.longball.model.LineupPlayer
import net.alexanderkahn.longball.model.Side
import net.alexanderkahn.servicebase.core.security.UserContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class GameService(@Autowired private val gameRepository: GameRepository,
                  @Autowired private val lineupPlayerRepository: LineupPlayerRepository,
                  @Autowired private val inningRepository: InningRepository) {

    fun getOne(id: Long): Game {
        val game = gameRepository.findByIdAndOwner(id, UserContext.pxUser)
        return game.toModel()
    }

    fun getAll(pageable: Pageable): Page<Game> {
        val games = gameRepository.findByOwner(pageable, UserContext.pxUser)
        return games.map { it.toModel() }
    }

    fun getLineupPlayers(pageable: Pageable, gameId: Long, side: Side): Page<LineupPlayer> {
        val game = gameRepository.findByIdAndOwner(gameId, UserContext.pxUser)
        val players = lineupPlayerRepository.findByGameAndSideAndOwner(pageable, game, side, UserContext.pxUser)
        return players.map { it.toModel() }
    }

//    fun getCurrentPlateAppearance(gameId: Long): PlateAppearance {
//        //TODO: this seems like it could be simplified a lot with a custom query
//        val game = gameRepository.findByIdAndOwner(gameId)
//        val currentInning: PxInning = inningRepository.findFirstByGameAndOwnerOrderByIdDesc(game) ?: throw Exception()
//        val currentSide: PxInningSide = inningSideRepository.findFirstByInningAndOwnerOrderByIdDesc(currentInning) ?: throw Exception()
//        val appearance: PxPlateAppearance = plateAppearanceRepository.findFirstBySideAndOwnerOrderByIdDesc(currentSide) ?: throw Exception()
//        val inningAppearances = plateAppearanceRepository.findBySideAndOwner(appearance.side)
//        return getPlateAppearanceModel(inningAppearances, appearance)
//    }
//
//    private fun getOpposingPitcher(appearance: PxPlateAppearance): PxPlayer {
//        val oppositeSide = if (appearance.side.side == Side.TOP) Side.BOTTOM else Side.TOP
//        val currentPitcher = getPlayerByPosition(appearance.side.inningSide.game, oppositeSide, FieldPosition.PITCHER)
//        return currentPitcher
//    }
//
//    //TODO: handle basepathResults
//    fun addGameplayEvent(gameId: Long, gameplayEvent: GameplayEvent): PlateAppearance {
//        val game = gameRepository.findByIdAndOwner(gameId)
//        val appearance = getOrCreatePlateAppearance(game)
//        val pxEvent = gameplayEvent.toPersistence(null, appearance)
//        validateEvent(gameplayEvent, appearance, game)
//        gameplayEventRepository.save(pxEvent)
//
//        val appearanceEvents = gameplayEventRepository.findByPlateAppearanceAndOwner(appearance)
//        val inningAppearances = plateAppearanceRepository.findBySideAndOwner(appearance.side)
//
//        processAppearanceResult(appearance, appearanceEvents, gameplayEvent.basepathResults)
//        processSideResult(appearance.side, inningAppearances)
//        processGameResult(game)
//        gameRepository.save(game)
//        return getPlateAppearanceModel(inningAppearances, appearance)
//    }
//
//    private fun validateEvent(gameplayEvent: GameplayEvent, appearance: PxPlateAppearance, game: PxGame) {
//        if (game.result != null) {
//            throw Exception("Can't add a result to a game that's already over")
//        }
//        val previousPitches = gameplayEventRepository.findByPlateAppearanceAndOwner(appearance)
//        if (gameplayEvent.basepathResults.isNotEmpty() && pitchEndsAtBatWithoutHit(gameplayEvent, previousPitches) && gameplayEvent.basepathResults.isNotEmpty()) {
//            throw Exception("Cannot record basepath event on at-bat-ending pitch")
//        }
//        if (gameplayEvent.pitch == Pitch.IN_PLAY && (gameplayEvent.ballInPlay == null || !gameplayEvent.basepathResults.map { it.lineupPlayer }.contains(appearance.batter.id))) {
//            throw Exception("Hit must include ballInPlay and basepathResult for batter")
//        }
//        validateOnBase(gameplayEvent, appearance)
//    }
//
//    private fun validateOnBase(gameplayEvent: GameplayEvent, appearance: PxPlateAppearance) {
//        if (gameplayEvent.pitch == Pitch.IN_PLAY && gameplayEvent.basepathResults.none { appearance.batter.id == it.lineupPlayer }) {
//            throw Exception("In play pitch must include a baserunning result for the batter")
//        }
//        if (gameplayEvent.basepathResults.map { it.lineupPlayer }.distinct().count() != gameplayEvent.basepathResults.count()) {
//            throw Exception("Basepath results cannot contain the same player multiple times")
//        }
//
//        //TODO: there's gonna be a bug here, I can smell it
//        val onBasePlayerIds = getCurrentOnBase(appearance.side).map { it.lineupPlayer.id ?: throw Exception() }
//        if (gameplayEvent.basepathResults.any { it.lineupPlayer != appearance.batter.id && !onBasePlayerIds.contains(it.lineupPlayer) }) {
//            throw Exception("Basepath runners may not swap order")
//        }
//
//        //TODO: assert same order as it was before (maybe just (wrapped) order of battingorder)
//    }
//
//    private fun pitchEndsAtBatWithoutHit(gameplayEvent: GameplayEvent, previousEvents: List<PxGameplayEvent>): Boolean {
//        return gameplayEvent.pitch == Pitch.HIT_BY_PITCH ||
//                gameplayEvent.pitch == Pitch.BALL && previousEvents.balls == LeagueRuleSet.BALLS_PER_WALK - 1 ||
//                gameplayEvent.pitch in arrayOf(Pitch.STRIKE_LOOKING, Pitch.STRIKE_SWINGING) && previousEvents.strikes == LeagueRuleSet.STRIKES_PER_OUT - 1
//    }
//
//    private fun processGameResult(game: PxGame) {
//        val innings = inningRepository.findByGameAndOwner(game)
//        //TODO: Wow, this is tortured. Also, not entirely right. Maybe the game is tied. Maybe the bottom side doesn't need to be played.
//        if (innings.count { inningIsComplete(it) } >= LeagueRuleSet.INNINGS_PER_GAME) {
//            game.result = PxGameResult(game, OffsetDateTime.now())
//        }
//    }
//
//    private fun inningIsComplete(inningSide: PxInning): Boolean {
//        val inningHalves = inningSideRepository.findByInningAndSideAndOwner(inningSide)
//        return inningHalves.count { it.result != null } >= 2
//    }
//
//    private fun getPlateAppearanceModel(inningAppearances: MutableList<PxPlateAppearance>, appearance: PxPlateAppearance): PlateAppearance {
//        val basepathResults = basepathResultRepository.findByPlateAppearanceAndOwner(inningAppearances)
//        val events = gameplayEventRepository.findByPlateAppearanceAndOwner(appearance)
//        val balls = events.balls
//        val strikes = events.strikes
//
//        val sideBasepathResults = basepathResultRepository.findByPlateAppearanceAndOwner(inningAppearances)
//        val outs = inningAppearances.getOuts(basepathResults)
//        val hits = sideBasepathResults.hits
//        val walks = sideBasepathResults.walks
//        val errors = sideBasepathResults.errors
//        val runs = sideBasepathResults.runs
//        return appearance.toModel(getOpposingPitcher(appearance), outs,hits,walks,errors,runs, balls, strikes, basepathResults.getCurrentOnBase())
//    }
//
//    private fun processSideResult(side: PxInningSide, plateAppearances: List<PxPlateAppearance>) {
//        val basepathResults = basepathResultRepository.findByPlateAppearanceAndOwner(plateAppearances)
//        if (plateAppearances.getOuts(basepathResults) >= LeagueRuleSet.OUTS_PER_INNING) {
//            side.result = side.toResult(basepathResults)
//            inningSideRepository.save(side)
//        }
//    }
//
//    private fun processAppearanceResult(appearance: PxPlateAppearance, pitchEvents: List<PxGameplayEvent>, basepathResults: List<BasepathResult>) {
//        if (shouldAddResult(pitchEvents)) {
//            val lastPitch = pitchEvents.last()
//            appearance.plateAppearanceResult = getPlateAppearanceResult(lastPitch)
//            when(appearance.plateAppearanceResult) {
//                PlateAppearanceResult.BASE_ON_BALLS, PlateAppearanceResult.HIT_BY_PITCH -> advanceRunnersOnBaseAwarded(lastPitch, appearance, getCurrentOnBase(appearance.side))
//                PlateAppearanceResult.IN_PLAY -> advanceRunnersOnBallInPlay(lastPitch, basepathResults)
//                else -> {}
//            }
//        }
//    }
//
//    private fun getCurrentOnBase(side: PxInningSide): List<PxBasepathResult> {
//        val plateAppearances = plateAppearanceRepository.findBySideAndOwner(side)
//        val results = plateAppearances.flatMap { gameplayEventRepository.findByPlateAppearanceAndOwner(it) }
//                .flatMap { basepathResultRepository.findByGameplayEventAndOwner(it) }
//        return results.sortedByDescending { it.id }.distinctBy { it.lineupPlayer }
//                .filterNot { it.location == BaseLocation.HOME || it.playResult == PlayResult.OUT }
//    }
//
//    private fun advanceRunnersOnBaseAwarded(lastPitch: PxGameplayEvent, appearance: PxPlateAppearance, currentOnBase: List<PxBasepathResult>) {
//        for (base in BaseLocation.values().sortedArrayDescending()) {
//            val onBase = currentOnBase.firstOrNull { it.location == base }
//            if (onBase != null && currentOnBase.any{ it.location == BaseLocation.FIRST || it.location == BaseLocation.values()[base.ordinal - 1]}) {
//                basepathResultRepository.save(PxBasepathResult(lastPitch, onBase.lineupPlayer, BaseLocation.values()[base.ordinal + 1], PlayResult.SAFE))
//            }
//        }
//        basepathResultRepository.save(PxBasepathResult(lastPitch, appearance.batter, BaseLocation.FIRST, PlayResult.SAFE))
//    }
//
//    private fun advanceRunnersOnBallInPlay(resultEvent: PxGameplayEvent, basepathResults: List<BasepathResult>) {
//        basepathResults.map { PxBasepathResult(resultEvent, lineupPlayerRepository.findOne(it.lineupPlayer), it.location, it.result) }
//                .forEach { basepathResultRepository.save(it) }
//    }
//
//    private fun shouldAddResult(events: List<PxGameplayEvent>): Boolean {
//        if (events.isNotEmpty() && events.last().pitch == Pitch.HIT_BY_PITCH) {
//            return true
//        } else if (events.last().pitch == Pitch.IN_PLAY) {
//            return true
//        } else {
//            return events.strikes >= LeagueRuleSet.STRIKES_PER_OUT ||
//                    events.balls >= LeagueRuleSet.BALLS_PER_WALK
//        }
//    }
//
//    private fun getPlateAppearanceResult(lastEvent: PxGameplayEvent): PlateAppearanceResult {
//        when(lastEvent.pitch) {
//            Pitch.BALL -> return PlateAppearanceResult.BASE_ON_BALLS
//            Pitch.STRIKE_SWINGING -> return PlateAppearanceResult.STRIKEOUT_SWINGING
//            Pitch.STRIKE_LOOKING -> return PlateAppearanceResult.STRIKEOUT_LOOKING
//            Pitch.HIT_BY_PITCH -> return PlateAppearanceResult.HIT_BY_PITCH
//            Pitch.IN_PLAY -> return PlateAppearanceResult.IN_PLAY
//            else -> throw NotImplementedError()
//        }
//    }
//
//    private fun getOrCreatePlateAppearance(game: PxGame): PxPlateAppearance {
//        val inningSide: PxInning = getOrCreateInning(game)
//        val side: PxInningSide = getOrCreateSide(inningSide)
//        val appearance: PxPlateAppearance = getOrCreateAppearance(side)
//        return appearance
//    }
//
//    private fun getOrCreateInning(game: PxGame): PxInning {
//        var inningSide = inningRepository.findFirstByGameAndOwnerOrderByIdDesc(game)
//        if (inningSide == null) {
//            inningSide = PxInning(game, 1)
//            inningRepository.save(inningSide)
//        } else {
//            val inningHalves = inningSideRepository.findByInningAndSideAndOwner(inningSide)
//            if (inningHalves.count { it.result != null } >= Side.values().size) {
//                inningSide = PxInning(game, inningSide.inningNumber + 1)
//                inningRepository.save(inningSide)
//            }
//        }
//        return inningSide
//    }
//
//    private fun getOrCreateSide(inningSide: PxInning): PxInningSide {
//        var side = inningSideRepository.findFirstByInningAndOwnerOrderByIdDesc(inningSide)
//        if (side == null) {
//            side = PxInningSide(inningSide, Side.TOP)
//            inningSideRepository.save(side)
//        } else if (side.result != null) {
//            side = PxInningSide(inningSide, Side.BOTTOM)
//            inningSideRepository.save(side)
//        }
//        return side
//    }
//
//    private fun getOrCreateAppearance(side: PxInningSide): PxPlateAppearance {
//        var appearance = plateAppearanceRepository.findFirstBySideAndOwnerOrderByIdDesc(side)
//        if (appearance == null) {
//            val batter: PxLineupPlayer
//            if (side.inningSide.inningNumber == 1) {
//                batter = getPlayerByBattingOrder(side.inningSide.game, side.side, 1)
//            } else {
//                val lastInning = inningRepository.findByGameAndOwner(side.inningSide.game).single { it.inningNumber == side.inningSide.inningNumber - 1 }
//                val lastSide = inningSideRepository.findByInningAndSideAndOwner(lastInning).single { it.side == side.side }
//                val lastAppearance = plateAppearanceRepository.findBySideAndOwner(lastSide).last { it.plateAppearanceResult != null }
//                batter = getPlayerByBattingOrder(lastInning.game, lastSide.side, (lastAppearance.batter.battingOrder % LeagueRuleSet.BATTERS_PER_LINEUP) + 1)
//            }
//            appearance = PxPlateAppearance(side, batter)
//            plateAppearanceRepository.save(appearance)
//        } else {
//            val pitchEvents = gameplayEventRepository.findByPlateAppearanceAndOwner(appearance)
//            if (pitchEvents.isNotEmpty() && appearance.plateAppearanceResult != null) {
//                val batter = getPlayerByBattingOrder(appearance.side.inningSide.game, appearance.side.side, (appearance.batter.battingOrder % LeagueRuleSet.BATTERS_PER_LINEUP) + 1)
//                appearance = PxPlateAppearance(side, batter)
//                plateAppearanceRepository.save(appearance)
//            }
//        }
//
//        return appearance
//    }
//
//    private fun getPlayerByBattingOrder(game: PxGame, side: Side, battingOrder: Int): PxLineupPlayer {
//        return lineupPlayerRepository.findFirstByGameAndSideAndBattingOrderAndOwner(game, side, battingOrder)
//    }
//
//    private fun getPlayerByPosition(game: PxGame, side: Side, fieldPosition:FieldPosition): PxPlayer {
//        return lineupPlayerRepository.findFirstByGameAndSideAndFieldPositionAndOwner(game, side, fieldPosition).player
//    }
}

