package net.alexanderkahn.longball.service.service

import net.alexanderkahn.longball.service.model.*
import net.alexanderkahn.longball.service.persistence.model.entity.*
import net.alexanderkahn.longball.service.persistence.repository.*
import net.alexanderkahn.longball.service.service.assembler.toModel
import net.alexanderkahn.longball.service.service.assembler.toOuts
import net.alexanderkahn.longball.service.service.assembler.toPersistence
import net.alexanderkahn.longball.service.service.assembler.toPlateAppearanceCount
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
                  @Autowired private val gameplayEventRepository: GameplayEventRepository,
                  @Autowired private val plateAppearanceResultRepository: PlateAppearanceResultRepository) {

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
        val currentPitcher = getOpposingPitcher(appearance)
        val outs = plateAppearanceRepository.findByInningHalfAndOwner(appearance.inningHalf).toOuts()
        return appearance.toModel(currentPitcher, outs)
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
        appearance.events.add(pxEvent)
        val result = processAppearanceResult(appearance)
        if (result != null) {
            appearance.result = result
            plateAppearanceResultRepository.save(result)
        }
        gameplayEventRepository.save(pxEvent)
        plateAppearanceRepository.save(appearance)
        return appearance.toModel(getOpposingPitcher(appearance), inningAppearances.toOuts())
    }

    private fun processAppearanceResult(appearance: PxPlateAppearance): PxPlateAppearanceResult? {
        if (shouldAddResult(appearance.events)) {
            return addResult(appearance)
        }
        return null
    }

    private fun shouldAddResult(events: List<PxGameplayEvent>): Boolean {
        val count = events.toPlateAppearanceCount()
        return count.strikes >= LeagueRuleSet.STRIKES_PER_OUT ||
                count.balls >= LeagueRuleSet.BALLS_PER_WALK
    }

    //TODO: this could be an extension function. Lots of this stuff should eventually go into either top-level functions
    //TODO: or a new class
    private fun addResult(appearance: PxPlateAppearance): PxPlateAppearanceResult {
        val plateAppearanceResult = getPlateAppearanceResult(appearance.events.last())
        return PxPlateAppearanceResult(null, appearance, plateAppearanceResult)

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
        var inning: PxInning? = game.innings.lastOrNull()
        if (inning == null) {
            inning = PxInning(null, game, 1)
            inningRepository.save(inning)
            game.innings.add(inning)
        }
        var inningHalf: PxInningHalf? = inning.inningHalves.lastOrNull()
        if (inningHalf == null) {
            inningHalf = PxInningHalf(null, inning, InningHalf.TOP)
            inningHalfRepository.save(inningHalf)
            inning.inningHalves.add(inningHalf)
        }
        var appearance: PxPlateAppearance? = inningHalf.plateAppearances.lastOrNull()
        if (appearance == null) {
            val batter = getPlayerByBattingOrder(game, InningHalf.TOP, 1) //TODO this is only true if inning is 1
            appearance = PxPlateAppearance(null, inningHalf, batter)
            plateAppearanceRepository.save(appearance)
        } else if (appearance.events.isNotEmpty() && appearance.result != null) {
            val batter = getPlayerByBattingOrder(game, appearance.inningHalf.half, (appearance.batter.battingOrder % LeagueRuleSet.BATTERS_PER_LINEUP) + 1)
            appearance = PxPlateAppearance(null, inningHalf, batter)
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

