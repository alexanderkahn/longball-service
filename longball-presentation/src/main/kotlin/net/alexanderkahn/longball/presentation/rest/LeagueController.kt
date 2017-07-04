package net.alexanderkahn.longball.presentation.rest

import net.alexanderkahn.longball.model.League
import net.alexanderkahn.service.longball.api.ILeagueService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/leagues")
class LeagueController(@Autowired private val leagueService: ILeagueService) {

    @GetMapping
    fun getGames(pageable: Pageable): Page<League> {
        return leagueService.getAll(pageable)
    }

    @GetMapping("/{id}")
    fun getGame(@PathVariable id: Long): League {
        return leagueService.get(id)
    }
}