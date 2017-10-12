package net.alexanderkahn.longball.presentation.rest

import net.alexanderkahn.longball.model.RosterPlayer
import net.alexanderkahn.longball.presentation.rest.model.ResponseTeam
import net.alexanderkahn.longball.presentation.rest.model.toMetaPage
import net.alexanderkahn.longball.presentation.rest.model.toResponse
import net.alexanderkahn.service.base.presentation.response.CollectionResponse
import net.alexanderkahn.service.base.presentation.response.ObjectResponse
import net.alexanderkahn.service.base.presentation.response.body.data.ResponseResourceCollection
import net.alexanderkahn.service.longball.api.ITeamService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*


@RestController
@RequestMapping("/teams")
class TeamController(@Autowired private val teamService: ITeamService) {

    @GetMapping
    fun getAll(pageable: Pageable): CollectionResponse<ResponseTeam> {
        val page = teamService.getAll(pageable)
        val objectCollection = ResponseResourceCollection(page.content.map { it.toResponse() })
        return CollectionResponse(objectCollection, page.toMetaPage())
    }

    @GetMapping("/{id}")
    fun get(@PathVariable id: UUID): ObjectResponse<ResponseTeam> {
        return ObjectResponse(teamService.get(id).toResponse())
    }

    @GetMapping("/{id}/roster")
    fun getRoster(@PathVariable id: UUID, pageable: Pageable): Page<RosterPlayer> {
        return teamService.getRoster(id, pageable)
    }
}