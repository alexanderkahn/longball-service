package net.alexanderkahn.longball.provider.service


import net.alexanderkahn.longball.model.dto.GameDTO
import net.alexanderkahn.longball.model.dto.LineupPositionDTO
import net.alexanderkahn.longball.model.type.Side
import net.alexanderkahn.longball.provider.assembler.toDTO
import net.alexanderkahn.longball.provider.entity.GameEntity
import net.alexanderkahn.longball.provider.repository.GameRepository
import net.alexanderkahn.longball.provider.repository.InningRepository
import net.alexanderkahn.longball.provider.repository.LineupPlayerRepository
import net.alexanderkahn.service.base.api.exception.NotFoundException
import net.alexanderkahn.service.longball.api.IGameService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*

@Service
class GameService @Autowired constructor(
        private val userService: UserService,
        private val gameRepository: GameRepository,
        private val lineupPlayerRepository: LineupPlayerRepository,
        private val inningRepository: InningRepository) : IGameService {

    override fun getOne(id: UUID): GameDTO {
        return getPxGame(id).toDTO()
    }

    fun getPxGame(id: UUID): GameEntity {
        val game = gameRepository.findByIdAndOwner(id, userService.embeddableUser())
        return game ?: throw NotFoundException("games", id)
    }

    override fun getAll(pageable: Pageable): Page<GameDTO> {
        val games = gameRepository.findByOwner(pageable, userService.embeddableUser())
        return games.map { it.toDTO() }
    }

    override fun getLineupPlayers(pageable: Pageable, gameId: UUID, side: Side): Page<LineupPositionDTO> {
        val game = gameRepository.findByIdAndOwner(gameId, userService.embeddableUser()) ?: throw NotFoundException("games", gameId)
        val players = lineupPlayerRepository.findByGameAndSideAndOwner(pageable, game, side, userService.embeddableUser())
        return players.map { it.toDTO() }
    }

//    fun getCurrentPlateAppearance(gameId: Long): PlateAppearanceDTO {
//        //TODO: this seems like it could be simplified a lot with a custom query
//        val game = gameRepository.findByIdAndOwner(gameId)
//        val currentInning: InningEntity = inningRepository.findFirstByGameAndOwnerOrderByIdDesc(game) ?: throw Exception()
//        val currentSide: InningSideEntity = inningSideRepository.findFirstByInningAndOwnerOrderByIdDesc(currentInning) ?: throw Exception()
//        val appearance: PlateAppearanceEntity = plateAppearanceRepository.findFirstBySideAndOwnerOrderByIdDesc(currentSide) ?: throw Exception()
//        val inningAppearances = plateAppearanceRepository.findBySideAndOwner(appearance.side)
//        return getPlateAppearanceModel(inningAppearances, appearance)
//    }
//
//    private fun getOpposingPitcher(appearance: PlateAppearanceEntity): PersonEntity {
//        val oppositeSide = if (appearance.side.side == Side.TOP) Side.BOTTOM else Side.TOP
//        val currentPitcher = getPlayerByPosition(appearance.side.inningSide.game, oppositeSide, FieldPosition.PITCHER)
//        return currentPitcher
//    }
//
//    //TODO: handle basepathResults
//    fun addGameplayEvent(gameId: Long, gameplayEvent: GameplayEvent): PlateAppearanceDTO {
//        val game = gameRepository.findByIdAndOwner(gameId)
//        val appearance = getOrCreatePlateAppearance(game)
//        val pxEvent = gameplayEvent.toEntity(null, appearance)
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
//    private fun validateEvent(gameplayEvent: GameplayEvent, appearance: PlateAppearanceEntity, game: GameEntity) {
//        if (game.resultEntity != null) {
//            throw Exception("Can't add a resultEntity to a game that's already over")
//        }
//        val previousPitches = gameplayEventRepository.findByPlateAppearanceAndOwner(appearance)
//        if (gameplayEvent.basepathResults.isNotEmpty() && pitchEndsAtBatWithoutHit(gameplayEvent, previousPitches) && gameplayEvent.basepathResults.isNotEmpty()) {
//            throw Exception("Cannot record basepath event on at-bat-ending pitch")
//        }
//        if (gameplayEvent.pitch == PitchType.IN_PLAY && (gameplayEvent.ballInPlay == null || !gameplayEvent.basepathResults.map { it.lineupPlayer }.contains(appearance.batter.id))) {
//            throw Exception("Hit must include ballInPlay and basepathResult for batter")
//        }
//        validateOnBase(gameplayEvent, appearance)
//    }
//
//    private fun validateOnBase(gameplayEvent: GameplayEvent, appearance: PlateAppearanceEntity) {
//        if (gameplayEvent.pitch == PitchType.IN_PLAY && gameplayEvent.basepathResults.none { appearance.batter.id == it.lineupPlayer }) {
//            throw Exception("In play pitch must include a baserunning resultEntity for the batter")
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
//        return gameplayEvent.pitch == PitchType.HIT_BY_PITCH ||
//                gameplayEvent.pitch == PitchType.BALL && previousEvents.balls == LeagueRuleSetDTO.ballsPerWalk - 1 ||
//                gameplayEvent.pitch in arrayOf(PitchType.STRIKE_LOOKING, PitchType.STRIKE_SWINGING) && previousEvents.strikes == LeagueRuleSetDTO.strikesPerOut - 1
//    }
//
//    private fun processGameResult(game: GameEntity) {
//        val innings = inningRepository.findByGameAndOwner(game)
//        //TODO: Wow, this is tortured. Also, not entirely right. Maybe the game is tied. Maybe the bottom side doesn't need to be played.
//        if (innings.count { inningIsComplete(it) } >= LeagueRuleSetDTO.inningsPerGame) {
//            game.resultEntity = GameResultEntity(game, OffsetDateTime.now())
//        }
//    }
//
//    private fun inningIsComplete(inningSide: InningEntity): Boolean {
//        val inningHalves = inningSideRepository.findByInningAndSideAndOwner(inningSide)
//        return inningHalves.count { it.resultEntity != null } >= 2
//    }
//
//    private fun getPlateAppearanceModel(inningAppearances: MutableList<PlateAppearanceEntity>, appearance: PlateAppearanceEntity): PlateAppearanceDTO {
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
//        return appearance.toDTO(getOpposingPitcher(appearance), outs,hits,walks,errors,runs, balls, strikes, basepathResults.getCurrentOnBase())
//    }
//
//    private fun processSideResult(side: InningSideEntity, plateAppearances: List<PlateAppearanceEntity>) {
//        val basepathResults = basepathResultRepository.findByPlateAppearanceAndOwner(plateAppearances)
//        if (plateAppearances.getOuts(basepathResults) >= LeagueRuleSetDTO.outsPerInning) {
//            side.resultEntity = side.toResult(basepathResults)
//            inningSideRepository.save(side)
//        }
//    }
//
//    private fun processAppearanceResult(appearance: PlateAppearanceEntity, pitchEvents: List<PxGameplayEvent>, basepathResults: List<BasepathResultDTO>) {
//        if (shouldAddResult(pitchEvents)) {
//            val lastPitch = pitchEvents.last()
//            appearance.plateAppearanceResult = getPlateAppearanceResult(lastPitch)
//            when(appearance.plateAppearanceResult) {
//                PlateAppearanceResultType.BASE_ON_BALLS, PlateAppearanceResultType.HIT_BY_PITCH -> advanceRunnersOnBaseAwarded(lastPitch, appearance, getCurrentOnBase(appearance.side))
//                PlateAppearanceResultType.IN_PLAY -> advanceRunnersOnBallInPlay(lastPitch, basepathResults)
//                else -> {}
//            }
//        }
//    }
//
//    private fun getCurrentOnBase(side: InningSideEntity): List<PxBasepathResult> {
//        val plateAppearances = plateAppearanceRepository.findBySideAndOwner(side)
//        val results = plateAppearances.flatMap { gameplayEventRepository.findByPlateAppearanceAndOwner(it) }
//                .flatMap { basepathResultRepository.findByGameplayEventAndOwner(it) }
//        return results.sortedByDescending { it.id }.distinctBy { it.lineupPlayer }
//                .filterNot { it.location == BaseLocation.HOME || it.playResult == PlayResultType.OUT }
//    }
//
//    private fun advanceRunnersOnBaseAwarded(lastPitch: PxGameplayEvent, appearance: PlateAppearanceEntity, currentOnBase: List<PxBasepathResult>) {
//        for (base in BaseLocation.values().sortedArrayDescending()) {
//            val onBase = currentOnBase.firstOrNull { it.location == base }
//            if (onBase != null && currentOnBase.any{ it.location == BaseLocation.FIRST || it.location == BaseLocation.values()[base.ordinal - 1]}) {
//                basepathResultRepository.save(PxBasepathResult(lastPitch, onBase.lineupPlayer, BaseLocation.values()[base.ordinal + 1], PlayResultType.SAFE))
//            }
//        }
//        basepathResultRepository.save(PxBasepathResult(lastPitch, appearance.batter, BaseLocation.FIRST, PlayResultType.SAFE))
//    }
//
//    private fun advanceRunnersOnBallInPlay(resultEvent: PxGameplayEvent, basepathResults: List<BasepathResultDTO>) {
//        basepathResults.map { PxBasepathResult(resultEvent, lineupPlayerRepository.findOne(it.lineupPlayer), it.location, it.resultEntity) }
//                .forEach { basepathResultRepository.save(it) }
//    }
//
//    private fun shouldAddResult(events: List<PxGameplayEvent>): Boolean {
//        if (events.isNotEmpty() && events.last().pitch == PitchType.HIT_BY_PITCH) {
//            return true
//        } else if (events.last().pitch == PitchType.IN_PLAY) {
//            return true
//        } else {
//            return events.strikes >= LeagueRuleSetDTO.strikesPerOut ||
//                    events.balls >= LeagueRuleSetDTO.ballsPerWalk
//        }
//    }
//
//    private fun getPlateAppearanceResult(lastEvent: PxGameplayEvent): PlateAppearanceResultType {
//        when(lastEvent.pitch) {
//            PitchType.BALL -> return PlateAppearanceResultType.BASE_ON_BALLS
//            PitchType.STRIKE_SWINGING -> return PlateAppearanceResultType.STRIKEOUT_SWINGING
//            PitchType.STRIKE_LOOKING -> return PlateAppearanceResultType.STRIKEOUT_LOOKING
//            PitchType.HIT_BY_PITCH -> return PlateAppearanceResultType.HIT_BY_PITCH
//            PitchType.IN_PLAY -> return PlateAppearanceResultType.IN_PLAY
//            else -> throw NotImplementedError()
//        }
//    }
//
//    private fun getOrCreatePlateAppearance(game: GameEntity): PlateAppearanceEntity {
//        val inningSide: InningEntity = getOrCreateInning(game)
//        val side: InningSideEntity = getOrCreateSide(inningSide)
//        val appearance: PlateAppearanceEntity = getOrCreateAppearance(side)
//        return appearance
//    }
//
//    private fun getOrCreateInning(game: GameEntity): InningEntity {
//        var inningSide = inningRepository.findFirstByGameAndOwnerOrderByIdDesc(game)
//        if (inningSide == null) {
//            inningSide = InningEntity(game, 1)
//            inningRepository.save(inningSide)
//        } else {
//            val inningHalves = inningSideRepository.findByInningAndSideAndOwner(inningSide)
//            if (inningHalves.count { it.resultEntity != null } >= Side.values().size) {
//                inningSide = InningEntity(game, inningSide.inningNumber + 1)
//                inningRepository.save(inningSide)
//            }
//        }
//        return inningSide
//    }
//
//    private fun getOrCreateSide(inningSide: InningEntity): InningSideEntity {
//        var side = inningSideRepository.findFirstByInningAndOwnerOrderByIdDesc(inningSide)
//        if (side == null) {
//            side = InningSideEntity(inningSide, Side.TOP)
//            inningSideRepository.save(side)
//        } else if (side.resultEntity != null) {
//            side = InningSideEntity(inningSide, Side.BOTTOM)
//            inningSideRepository.save(side)
//        }
//        return side
//    }
//
//    private fun getOrCreateAppearance(side: InningSideEntity): PlateAppearanceEntity {
//        var appearance = plateAppearanceRepository.findFirstBySideAndOwnerOrderByIdDesc(side)
//        if (appearance == null) {
//            val batter: LineupPositionEntity
//            if (side.inningSide.inningNumber == 1) {
//                batter = getPlayerByBattingOrder(side.inningSide.game, side.side, 1)
//            } else {
//                val lastInning = inningRepository.findByGameAndOwner(side.inningSide.game).single { it.inningNumber == side.inningSide.inningNumber - 1 }
//                val lastSide = inningSideRepository.findByInningAndSideAndOwner(lastInning).single { it.side == side.side }
//                val lastAppearance = plateAppearanceRepository.findBySideAndOwner(lastSide).last { it.plateAppearanceResult != null }
//                batter = getPlayerByBattingOrder(lastInning.game, lastSide.side, (lastAppearance.batter.battingOrder % LeagueRuleSetDTO.battersPerLineup) + 1)
//            }
//            appearance = PlateAppearanceEntity(side, batter)
//            plateAppearanceRepository.save(appearance)
//        } else {
//            val pitchEvents = gameplayEventRepository.findByPlateAppearanceAndOwner(appearance)
//            if (pitchEvents.isNotEmpty() && appearance.plateAppearanceResult != null) {
//                val batter = getPlayerByBattingOrder(appearance.side.inningSide.game, appearance.side.side, (appearance.batter.battingOrder % LeagueRuleSetDTO.battersPerLineup) + 1)
//                appearance = PlateAppearanceEntity(side, batter)
//                plateAppearanceRepository.save(appearance)
//            }
//        }
//
//        return appearance
//    }
//
//    private fun getPlayerByBattingOrder(game: GameEntity, side: Side, battingOrder: Int): LineupPositionEntity {
//        return lineupPlayerRepository.findFirstByGameAndSideAndBattingOrderAndOwner(game, side, battingOrder)
//    }
//
//    private fun getPlayerByPosition(game: GameEntity, side: Side, fieldPosition:FieldPosition): PersonEntity {
//        return lineupPlayerRepository.findFirstByGameAndSideAndFieldPositionAndOwner(game, side, fieldPosition).player
//    }
}

