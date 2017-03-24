package net.alexanderkahn.longball.service.service

import net.alexanderkahn.longball.service.model.*
import net.alexanderkahn.longball.service.persistence.assembler.GameAssembler
import net.alexanderkahn.longball.service.persistence.repository.GameRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*

@Service
class GameService(@Autowired private val gameRepository: GameRepository) {

    private val gameAssembler: GameAssembler = GameAssembler()

    fun get(id: Long): Game {
        return gameAssembler.toModel(gameRepository.findOne(id))
    }

    fun getAll(pageable: Pageable): Page<Game> {
        val games = gameRepository.findAll(pageable)
        return games.map { gameAssembler.toModel(it) }
    }

    fun getFakeStatus(gameId: Long): GameStatus {
        val appearance = PlateAppearance(Player(1, "Pitch", "Guy"), Player(2, "Bat", "man"), PlateAppearanceCount(3, 0))
        val basePath = BasePath(Base(Player(3, "First", "Base")), Base(null), Base(Player(4, "Third", "Base")))
        val innings = (1..4).map { Inning(Random().nextInt(5), Random().nextInt(5)) }
        return GameStatus(appearance, basePath, InningSummary(innings))
    }
}