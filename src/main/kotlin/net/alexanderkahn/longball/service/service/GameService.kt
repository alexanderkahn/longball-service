package net.alexanderkahn.longball.service.service

import net.alexanderkahn.base.servicebase.service.UserContext
import net.alexanderkahn.longball.service.model.Game
import net.alexanderkahn.longball.service.model.InningHalf
import net.alexanderkahn.longball.service.model.LineupPosition
import net.alexanderkahn.longball.service.model.PlateAppearance
import net.alexanderkahn.longball.service.persistence.assembler.toModel
import net.alexanderkahn.longball.service.persistence.model.PersistenceGame
import net.alexanderkahn.longball.service.persistence.model.PersistencePlateAppearance
import net.alexanderkahn.longball.service.persistence.model.PersistencePlayer
import net.alexanderkahn.longball.service.persistence.repository.GameRepository
import net.alexanderkahn.longball.service.persistence.repository.LineupPositionRepository
import net.alexanderkahn.longball.service.persistence.repository.PlateAppearanceRepository
import net.alexanderkahn.longball.service.persistence.repository.getPersistenceUser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class GameService(@Autowired private val gameRepository: GameRepository,
                  @Autowired private val lineupPositionRepository: LineupPositionRepository,
                  @Autowired private val plateAppearanceRepository: PlateAppearanceRepository) {

    //TODO: league rules
    private val FAKE_LEAGUE_ROSTER_LENGTH = 9

    fun get(id: Long): Game {
        val game = gameRepository.findByIdAndOwner(id, UserContext.getPersistenceUser())
        return game.toModel()
    }

    fun getAll(pageable: Pageable): Page<Game> {
        val games = gameRepository.findByOwner(pageable, UserContext.getPersistenceUser())
        return games.map { it.toModel() }
    }

    fun getLineupPositions(pageable: Pageable, gameId: Long, inningHalf: InningHalf): Page<LineupPosition> {
        val game = gameRepository.findByIdAndOwner(gameId, UserContext.getPersistenceUser())
        val positions = lineupPositionRepository.findByOwnerAndGameAndInningHalf(pageable, UserContext.getPersistenceUser(), game, inningHalf)
        return positions.map { it.toModel() }
    }

    fun getCurrentPlateAppearance(gameId: Long): PlateAppearance {
        val game = gameRepository.findByIdAndOwner(gameId, UserContext.getPersistenceUser())
        var appearance = plateAppearanceRepository.findLastByOwnerAndGame(UserContext.getPersistenceUser(), game)
        if (appearance == null) {
            val batter = getBatterAtLineupPosition(game, InningHalf.TOP, 0)
            appearance = PersistencePlateAppearance(null, UserContext.getPersistenceUser(), game, 1, InningHalf.TOP, batter)
            plateAppearanceRepository.save(appearance)
        }
        return appearance.toModel()
    }

    private fun getBatterAtLineupPosition(game: PersistenceGame, inningHalf: InningHalf, battingOrderNumber: Int): PersistencePlayer {
        val pageable = PageRequest(battingOrderNumber, FAKE_LEAGUE_ROSTER_LENGTH)
        val players = lineupPositionRepository.findByOwnerAndGameAndInningHalf(pageable, UserContext.getPersistenceUser(), game, inningHalf)
        val batter = players.content[battingOrderNumber].player
        return batter
    }
}