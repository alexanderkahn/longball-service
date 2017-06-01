package net.alexanderkahn.longball.presentation.rest

import net.alexanderkahn.longball.core.service.TeamService
import net.alexanderkahn.longball.model.RosterPlayer
import net.alexanderkahn.longball.model.Team
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/teams")
class TeamController(@Autowired private val teamService: TeamService) {

    @GetMapping
    fun getAll(pageable: Pageable): Page<Team> {
        return teamService.getAll(pageable)
    }

    @GetMapping("/{id}")
    fun get(@PathVariable id: Long): Team {
        return teamService.get(id)
    }

    @GetMapping("/{id}/roster")
    fun getRoster(@PathVariable id: Long, pageable: Pageable): Page<RosterPlayer> {
        return teamService.getRoster(id, pageable)
    }
}