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
        val appearance = plateAppearanceRepository.findFirstByGameAndOwnerOrderByIdDesc(game)
                ?: throw Exception("Unable to find current plate appearance. Has the game started?")
        val currentPitcher = getOpposingPitcher(appearance.half, game)
        val outs = plateAppearanceRepository.findByGameAndInningAndHalfAndOwner(appearance.game, appearance.inning, appearance.half).toOuts()
        return appearance.toModel(currentPitcher, outs)
    }

    private fun getOpposingPitcher(inningHalf: InningHalf, game: PxGame): PxPlayer {
        val oppositeHalf = InningHalf.values().filter { it != inningHalf }.first()
        val currentPitcher = getPlayerByPosition(game, oppositeHalf, FieldPosition.PITCHER)
        return currentPitcher
    }
    fun addGameplayEvent(gameId: Long, gameplayEvent: GameplayEvent): PlateAppearance {
        val game = gameRepository.findByIdAndOwner(gameId)
        val appearance = getOrCreatePlateAppearance(game)
        val pxEvent = gameplayEvent.toPersistence(null, appearance)
        val inningAppearances = plateAppearanceRepository.findByGameAndInningAndHalfAndOwner(appearance.game, appearance.inning, appearance.half)
        appearance.events.add(pxEvent)
        val result = processAppearanceResult(appearance)
        if (result != null) {
            appearance.result = result
            plateAppearanceResultRepository.save(result)
        }
        gameplayEventRepository.save(pxEvent)
        plateAppearanceRepository.save(appearance)
        return appearance.toModel(getOpposingPitcher(appearance.half, appearance.game), inningAppearances.toOuts())
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
        var appearance: PxPlateAppearance? = plateAppearanceRepository.findFirstByGameAndOwnerOrderByIdDesc(game)
        if (appearance == null) {
            val batter = getPlayerByBattingOrder(game, InningHalf.TOP, 1)
            appearance = PxPlateAppearance(null, game, 1, InningHalf.TOP, batter)
            plateAppearanceRepository.save(appearance)
        } else if (appearance.events.isNotEmpty() && appearance.result != null) {
            val batter = getPlayerByBattingOrder(game, appearance.half, ((appearance.batter.battingOrder % LeagueRuleSet.BATTERS_PER_LINEUP) + 1).toShort())
            appearance = PxPlateAppearance(null, game, appearance.inning, appearance.half, batter)
            plateAppearanceRepository.save(appearance)

        }
        return appearance
    }

    private fun getPlayerByBattingOrder(game: PxGame, inningHalf: InningHalf, battingOrder: Short): PxLineupPlayer {
        return lineupPlayerRepository.findFirstByGameAndInningHalfAndBattingOrderAndOwner(game, inningHalf, battingOrder)
    }

    private fun getPlayerByPosition(game: PxGame, inningHalf: InningHalf, fieldPosition:FieldPosition): PxPlayer {
        return lineupPlayerRepository.findFirstByGameAndInningHalfAndFieldPositionAndOwner(game, inningHalf, fieldPosition).player
    }
}

