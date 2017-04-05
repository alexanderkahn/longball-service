package net.alexanderkahn.longball.service.service

import net.alexanderkahn.base.servicebase.service.UserContext
import net.alexanderkahn.longball.service.model.*
import net.alexanderkahn.longball.service.service.assembler.toModel
import net.alexanderkahn.longball.service.service.assembler.toPersistence
import net.alexanderkahn.longball.service.service.assembler.toPlateAppearanceCount
import net.alexanderkahn.longball.service.persistence.model.entity.*
import net.alexanderkahn.longball.service.persistence.repository.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class GameService(@Autowired private val gameRepository: GameRepository,
                  @Autowired private val lineupPlayerRepository: LineupPlayerRepository,
                  @Autowired private val plateAppearanceRepository: PlateAppearanceRepository,
                  @Autowired private val gameplayEventRepository: GameplayEventRepository,
                  @Autowired private val gameplayEventResultRepository: GameplayEventResultRepository) {

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
        return appearance.toModel(currentPitcher)
    }

    fun addGameplayEvent(gameId: Long, gameplayEvent: GameplayEvent) {
        val game = gameRepository.findByIdAndOwner(gameId, UserContext.getPersistenceUser())
        val appearance = getOrCreatePlateAppearance(game)
        val pxEvent = gameplayEvent.toPersistence(null, appearance)
        appearance.events.add(pxEvent)
        gameplayEventRepository.save(pxEvent)
        processEventResult(appearance.events)
    }

    private fun processEventResult(events: List<PxGameplayEvent>) {
        if (shouldAddResult(events)) {
            addResult(events.last())
        }
    }

    private fun shouldAddResult(events: List<PxGameplayEvent>): Boolean {
        val count = events.toPlateAppearanceCount()
        return count.strikes >= LeagueRuleSet.STRIKES_PER_OUT ||
                count.balls >= LeagueRuleSet.BALLS_PER_WALK
    }

    //TODO: this could be an extension function. Lots of this stuff should eventually go into either top-level functions
    //TODO: or a new class
    private fun addResult(lastEvent: PxGameplayEvent) {
        val atBatResult = getAtBatResult(lastEvent)
        val result = PxGameplayEventResult(null, UserContext.getPersistenceUser(), lastEvent, atBatResult)
        gameplayEventResultRepository.save(result)
        lastEvent.result = result
    }

    private fun getAtBatResult(lastEvent: PxGameplayEvent): AtBatResult {
        when(lastEvent.pitch) {
            Pitch.BALL -> return AtBatResult.BASE_ON_BALLS
            Pitch.STRIKE_SWINGING -> return AtBatResult.STRIKEOUT_SWINGING
            Pitch.STRIKE_LOOKING -> return AtBatResult.STRIKEOUT_LOOKING
            else -> throw NotImplementedError()
        }
    }

    private fun getOrCreatePlateAppearance(game: PxGame): PxPlateAppearance {
        var appearance: PxPlateAppearance? = plateAppearanceRepository.findFirstByOwnerAndGameOrderByIdDesc(UserContext.getPersistenceUser(), game)
        if (appearance == null) {
            val batter = getPlayerByBattingOrder(game, InningHalf.TOP, 1)
            appearance = PxPlateAppearance(null, UserContext.getPersistenceUser(), game, 1, InningHalf.TOP, batter, mutableListOf())
            plateAppearanceRepository.save(appearance)
        } else if (appearance.events.isNotEmpty() && appearance.events.last().result != null) {
            val batter = getPlayerByBattingOrder(game, appearance.half, ((appearance.batter.battingOrder % LeagueRuleSet.BATTERS_PER_LINEUP) + 1).toShort())
            appearance = PxPlateAppearance(null, UserContext.getPersistenceUser(), game, appearance.inning, appearance.half, batter, mutableListOf())
            plateAppearanceRepository.save(appearance)

        }
        return appearance
    }

    private fun getPlayerByBattingOrder(game: PxGame, inningHalf: InningHalf, battingOrder: Short): PxLineupPlayer {
        return lineupPlayerRepository.findFirstByOwnerAndGameAndInningHalfAndBattingOrder(UserContext.getPersistenceUser(), game, inningHalf, battingOrder)
    }

    private fun getPlayerByPosition(game: PxGame, inningHalf: InningHalf, fieldPosition:FieldPosition): PxPlayer {
        return lineupPlayerRepository.findFirstByOwnerAndGameAndInningHalfAndFieldPosition(UserContext.getPersistenceUser(), game, inningHalf, fieldPosition).player
    }
}

