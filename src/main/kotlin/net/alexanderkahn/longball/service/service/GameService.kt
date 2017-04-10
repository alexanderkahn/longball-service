package net.alexanderkahn.longball.service.service

import net.alexanderkahn.longball.service.model.*
import net.alexanderkahn.longball.service.persistence.model.entity.*
import net.alexanderkahn.longball.service.persistence.repository.*
import net.alexanderkahn.longball.service.service.assembler.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class GameService(@Autowired private val gameRepository: GameRepository,
                  @Autowired private val lineupPlayerRepository: LineupPlayerRepository,
                  @Autowired private val inningRepository: InningRepository,
                  @Autowired private val inningHalfRepository: InningHalfRepository,
                  @Autowired private val plateAppearanceRepository: PlateAppearanceRepository,
                  @Autowired private val gameplayEventRepository: GameplayEventRepository) {

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
    fun addGameplayEvent(gameId: Long, gameplayEvent: GameplayEvent): PlateAppearance {
        val game = gameRepository.findByIdAndOwner(gameId)
        val appearance = getOrCreatePlateAppearance(game)
        val pxEvent = gameplayEvent.toPersistence(null, appearance)
        val inningAppearances = plateAppearanceRepository.findByInningHalfAndOwner(appearance.inningHalf)
        appearance.pitchEvents.add(pxEvent)
        processAppearanceResult(appearance)
        proccessInningHalfResult(appearance.inningHalf)
        gameRepository.save(game)
        return getPlateAppearanceModel(inningAppearances, appearance)
    }

    private fun getPlateAppearanceModel(inningAppearances: MutableList<PxPlateAppearance>, appearance: PxPlateAppearance): PlateAppearance {
        val outs = inningAppearances.toOuts()
        val basePathResults = inningAppearances.toCurrentOnBase()
        return appearance.toModel(getOpposingPitcher(appearance), outs, basePathResults)
    }

    private fun proccessInningHalfResult(inningHalf: PxInningHalf) {
        if (inningHalf.plateAppearances.toOuts() >= LeagueRuleSet.OUTS_PER_INNING) {
            inningHalf.result = inningHalf.toResult()
            inningHalfRepository.save(inningHalf)
        }
    }

    private fun processAppearanceResult(appearance: PxPlateAppearance) {
        if (shouldAddResult(appearance.pitchEvents)) {
            val lastPitch = appearance.pitchEvents.last()
            appearance.plateAppearanceResult = getPlateAppearanceResult(lastPitch)
            when(appearance.plateAppearanceResult) {
                PlateAppearanceResult.BASE_ON_BALLS -> lastPitch.basepathResults.add(PxBasePathResult(lastPitch, appearance.batter, PlayLocation.FIRST, PlayResult.SAFE))
                else -> {}
            }
        }
    }

    private fun shouldAddResult(events: List<PxGameplayEvent>): Boolean {
        val count = events.toPlateAppearanceCount()
        return count.strikes >= LeagueRuleSet.STRIKES_PER_OUT ||
                count.balls >= LeagueRuleSet.BALLS_PER_WALK
    }

    private fun getPlateAppearanceResult(lastEvent: PxGameplayEvent): PlateAppearanceResult {
        when(lastEvent.pitch) {
            Pitch.BALL -> return PlateAppearanceResult.BASE_ON_BALLS
            Pitch.STRIKE_SWINGING -> return PlateAppearanceResult.STRIKEOUT_SWINGING
            Pitch.STRIKE_LOOKING -> return PlateAppearanceResult.STRIKEOUT_LOOKING
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
        } else if (inning.inningHalves.filter { it.result != null }.count() >= InningHalf.values().size ) {
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

