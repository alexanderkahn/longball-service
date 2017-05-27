package net.alexanderkahn.longball.service.rest

import net.alexanderkahn.longball.service.model.Inning
import net.alexanderkahn.longball.service.service.InningService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class InningController(
        @Autowired private val inningService: InningService) {

    @GetMapping("/games/{id}/innings")
    fun getInnings(pageable: Pageable, @PathVariable id: Long): Page<Inning> {
        return (inningService.getInningsForGame(pageable, id))
    }

    @PostMapping("/games/{gameId}/innings/next")
    fun advanceInning(@PathVariable gameId: Long) {
        inningService.advanceInning(gameId)
    }

}