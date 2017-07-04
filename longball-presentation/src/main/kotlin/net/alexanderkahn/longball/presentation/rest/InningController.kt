package net.alexanderkahn.longball.presentation.rest

import net.alexanderkahn.longball.model.Inning
import net.alexanderkahn.longball.model.InningSide
import net.alexanderkahn.longball.presentation.getSideFromParam
import net.alexanderkahn.service.longball.api.IInningService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class InningController(
        @Autowired private val inningService: IInningService) {

    @GetMapping("/games/{gameId}/innings")
    fun getInnings(pageable: Pageable, @PathVariable gameId: Long): Page<Inning> {
        return inningService.getInningsForGame(pageable, gameId)
    }

    @GetMapping("/games/{gameId}/innings/{inningNumber}")
    fun getInning(@PathVariable gameId: Long, @PathVariable inningNumber: Int): Inning {
        return inningService.getInning(gameId, inningNumber)
    }

    @GetMapping("/games/{gameId}/innings/{inningNumber}/{side:top|bottom}")
    fun getInningSide(@PathVariable gameId: Long, @PathVariable inningNumber: Int, @PathVariable side: String): InningSide {
        return inningService.getInningSide(gameId, inningNumber, getSideFromParam(side))
    }

    @PostMapping("/games/{gameId}/innings/next")
    fun advanceInning(@PathVariable gameId: Long) {
        inningService.advanceInning(gameId)
    }

}