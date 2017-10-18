package net.alexanderkahn.longball.presentation.rest.controller

import net.alexanderkahn.longball.model.dto.RosterPositionDTO
import net.alexanderkahn.longball.presentation.rest.model.*
import net.alexanderkahn.service.base.presentation.request.ObjectRequest
import net.alexanderkahn.service.base.presentation.response.CollectionResponse
import net.alexanderkahn.service.base.presentation.response.CreatedResponse
import net.alexanderkahn.service.base.presentation.response.DeletedResponse
import net.alexanderkahn.service.base.presentation.response.ObjectResponse
import net.alexanderkahn.service.base.presentation.response.body.data.ResponseResourceCollection
import net.alexanderkahn.service.longball.api.ITeamService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
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

    @PostMapping
    fun post(@RequestBody teamRequest: ObjectRequest<RequestTeam>): CreatedResponse<ResponseTeam> {
        teamRequest.data.validate()
        val created = teamService.save(teamRequest.data.toDto())
        return CreatedResponse(created.toResponse())
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID): DeletedResponse {
        teamService.delete(id)
        return DeletedResponse()
    }

    @GetMapping("/{id}/roster")
    fun getRoster(@PathVariable id: UUID, pageable: Pageable): Page<RosterPositionDTO> {
        return teamService.getRoster(id, pageable)
    }
}