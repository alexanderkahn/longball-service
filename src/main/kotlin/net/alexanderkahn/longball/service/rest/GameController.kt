package net.alexanderkahn.longball.service.rest

import net.alexanderkahn.longball.service.model.Game
import net.alexanderkahn.longball.service.model.GameStatus
import net.alexanderkahn.longball.service.service.FakeGameService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/games")
class GameController(@Autowired private val gameService: FakeGameService) {

    @GetMapping
    fun getGames(pageable: Pageable): Page<Game> {
        return gameService.getAll()
    }

    @GetMapping("/{id}")
    fun getGame(@PathVariable id: Long): Game {
        return gameService.get(id)
    }

    @GetMapping("/{id}/status")
    fun getGameStatus(@PathVariable id: Long): GameStatus {
        return gameService.getStatus(id)
    }
}