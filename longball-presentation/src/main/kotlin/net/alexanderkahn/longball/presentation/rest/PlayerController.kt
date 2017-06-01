package net.alexanderkahn.longball.presentation.rest

import net.alexanderkahn.longball.core.service.PlayerService
import net.alexanderkahn.longball.model.Player
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/players")
class PlayerController(@Autowired private val playerService: PlayerService) {

    @GetMapping
    fun getAll(pageable: Pageable): Page<Player> {
        return playerService.getAll(pageable)
    }

    @GetMapping("/{id}")
    fun get(@PathVariable id: Long): Player {
        return playerService.get(id)
    }
}