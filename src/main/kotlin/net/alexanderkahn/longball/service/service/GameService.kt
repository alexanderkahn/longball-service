package net.alexanderkahn.longball.service.service

import net.alexanderkahn.base.servicebase.service.UserContext
import net.alexanderkahn.longball.service.model.*
import net.alexanderkahn.longball.service.persistence.assembler.GameAssembler
import net.alexanderkahn.longball.service.persistence.assembler.LineupPositionAssembler
import net.alexanderkahn.longball.service.persistence.repository.GameRepository
import net.alexanderkahn.longball.service.persistence.repository.LineupPositionRepository
import net.alexanderkahn.longball.service.persistence.repository.getPersistenceUser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*

@Service
class GameService(@Autowired private val gameRepository: GameRepository, @Autowired private val lineupPositionRepository: LineupPositionRepository) {

    private val gameAssembler: GameAssembler = GameAssembler()
    private val lineupPositionAssembler: LineupPositionAssembler = LineupPositionAssembler()

    fun get(id: Long): Game {
        return gameAssembler.toModel(gameRepository.findByIdAndOwner(id, UserContext.getPersistenceUser()))
    }

    fun getAll(pageable: Pageable): Page<Game> {
        val games = gameRepository.findByOwner(pageable, UserContext.getPersistenceUser())
        return games.map { gameAssembler.toModel(it) }
    }

    fun getLineupPositions(pageable: Pageable, gameId: Long, inningHalf: InningHalf): Page<LineupPosition> {
        val positions = lineupPositionRepository.findByOwnerAndGameIdAndInningHalf(pageable, UserContext.getPersistenceUser(), gameId, inningHalf)
        return positions.map { lineupPositionAssembler.toModel(it) }
    }

    fun getFakeStatus(gameId: Long): GameStatus {
        val appearance = PlateAppearance(Player(1, "Pitch", "Guy"), Player(2, "Bat", "man"), PlateAppearanceCount(3, 0))
        val basePath = BasePath(Base(Player(3, "First", "Base")), Base(null), Base(Player(4, "Third", "Base")))
        val innings = (1..4).map { Inning(Random().nextInt(5), Random().nextInt(5)) }
        return GameStatus(appearance, basePath, InningSummary(innings))
    }
}