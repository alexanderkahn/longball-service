package net.alexanderkahn.longball.service.rest

import net.alexanderkahn.longball.service.model.Game
import net.alexanderkahn.longball.service.service.FakeGameService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/games")
class GameController(@Autowired private val gameService: FakeGameService) {

    @GetMapping("/{id}")
    fun getGame(@PathVariable id: String): Game {
        return gameService.get(id)
    }
}