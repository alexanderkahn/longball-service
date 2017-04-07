package net.alexanderkahn.longball.service.service

import net.alexanderkahn.base.servicebase.service.UserContext
import net.alexanderkahn.longball.service.model.*
import net.alexanderkahn.longball.service.persistence.model.entity.*
import net.alexanderkahn.longball.service.persistence.repository.*
import net.alexanderkahn.longball.service.service.assembler.toModel
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
        val game = gameRepository.findByIdAndOwner(id, UserContext.getPersistenceUser())
        return game.toModel()
    }

    fun getAll(pageable: Pageable): Page<Game> {
        val games = gameRepository.findByOwner(pageable, UserContext.getPersistenceUser())
        return games.map { it.toModel() }
    }

    fun getLineupPlayers(pageable: Pageable, gameId: Long, inningHalf: InningHalf): Page<LineupPlayer> {
        val game = gameRepository.findByIdAndOwner(gameId, UserContext.getPersistenceUser())
        val players = lineupPlayerRepository.findByOwnerAndGameAndInningHalf(pageable, UserContext.getPersistenceUser(), game, inningHalf)
        return players.map { it.toModel() }
    }

    fun getCurrentPlateAppearance(gameId: Long): PlateAppearance {
        val game = gameRepository.findByIdAndOwner(gameId, UserContext.getPersistenceUser())
        val appearance = getOrCreatePlateAppearance(game)
        val oppositeHalf = InningHalf.values().filter { it != appearance.half }.first()
        val currentPitcher = getPlayerByPosition(game, oppositeHalf, FieldPosition.PITCHER)
        val outs: Short = getOuts(appearance.inning, appearance.half)
        return appearance.toModel(currentPitcher, outs)
    }

    fun addGameplayEvent(gameId: Long, gameplayEvent: GameplayEvent) {
        val game = gameRepository.findByIdAndOwner(gameId, UserContext.getPersistenceUser())
        val appearance = getOrCreatePlateAppearance(game)
        val pxEvent = gameplayEvent.toPersistence(null, appearance)
        appearance.events.add(pxEvent)
        gameplayEventRepository.save(pxEvent)
        processAppearanceResult(appearance)
    }

    private fun processAppearanceResult(appearance: PxPlateAppearance) {
        if (shouldAddResult(appearance.events)) {
            addResult(appearance)
        }
    }

    private fun shouldAddResult(events: List<PxGameplayEvent>): Boolean {
        val count = events.toPlateAppearanceCount()
        return count.strikes >= LeagueRuleSet.STRIKES_PER_OUT ||
                count.balls >= LeagueRuleSet.BALLS_PER_WALK
    }

    //TODO: this could be an extension function. Lots of this stuff should eventually go into either top-level functions
    //TODO: or a new class
    private fun addResult(appearance: PxPlateAppearance) {
        val plateAppearanceResult = getPlateAppearanceResult(appearance.events.last())
        val result = PxPlateAppearanceResult(null, UserContext.getPersistenceUser(), appearance, plateAppearanceResult)
        plateAppearanceResultRepository.save(result)
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
        var appearance: PxPlateAppearance? = plateAppearanceRepository.findFirstByOwnerAndGameOrderByIdDesc(UserContext.getPersistenceUser(), game)
        if (appearance == null) {
            val batter = getPlayerByBattingOrder(game, InningHalf.TOP, 1)
            appearance = PxPlateAppearance(null, UserContext.getPersistenceUser(), game, 1, InningHalf.TOP, batter)
            plateAppearanceRepository.save(appearance)
        } else if (appearance.events.isNotEmpty() && appearance.result != null) {
            val batter = getPlayerByBattingOrder(game, appearance.half, ((appearance.batter.battingOrder % LeagueRuleSet.BATTERS_PER_LINEUP) + 1).toShort())
            appearance = PxPlateAppearance(null, UserContext.getPersistenceUser(), game, appearance.inning, appearance.half, batter)
            plateAppearanceRepository.save(appearance)

        }
        return appearance
    }

    private fun getOuts(inning: Short, half: InningHalf): Short {
        return plateAppearanceRepository.findByOwnerAndInningAndHalf(UserContext.getPersistenceUser(), inning, half)
                .filter { it.result?.plateAppearanceResult in arrayOf(PlateAppearanceResult.STRIKEOUT_LOOKING, PlateAppearanceResult.STRIKEOUT_SWINGING) }
                .count().toShort()
    }

    private fun getPlayerByBattingOrder(game: PxGame, inningHalf: InningHalf, battingOrder: Short): PxLineupPlayer {
        return lineupPlayerRepository.findFirstByOwnerAndGameAndInningHalfAndBattingOrder(UserContext.getPersistenceUser(), game, inningHalf, battingOrder)
    }

    private fun getPlayerByPosition(game: PxGame, inningHalf: InningHalf, fieldPosition:FieldPosition): PxPlayer {
        return lineupPlayerRepository.findFirstByOwnerAndGameAndInningHalfAndFieldPosition(UserContext.getPersistenceUser(), game, inningHalf, fieldPosition).player
    }
}

