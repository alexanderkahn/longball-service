package net.alexanderkahn.longball.presentation.rest.controller

import net.alexanderkahn.longball.api.service.ITeamService
import net.alexanderkahn.longball.model.dto.RequestTeam
import net.alexanderkahn.longball.model.dto.ResponseTeam
import net.alexanderkahn.longball.model.dto.toCollectionResponse
import net.alexanderkahn.service.base.model.request.ObjectRequest
import net.alexanderkahn.service.base.model.response.CollectionResponse
import net.alexanderkahn.service.base.model.response.CreatedResponse
import net.alexanderkahn.service.base.model.response.DeletedResponse
import net.alexanderkahn.service.base.model.response.ObjectResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import java.util.*


@RestController
@RequestMapping("/teams")
class TeamController(@Autowired private val teamService: ITeamService) {

    @GetMapping
    fun getAll(pageable: Pageable): CollectionResponse<ResponseTeam> {
        val page = teamService.getAll(pageable)
        return page.toCollectionResponse()
    }

    @GetMapping("/{id}")
    fun get(@PathVariable id: UUID): ObjectResponse<ResponseTeam> {
        return ObjectResponse(teamService.get(id))
    }

    @PostMapping
    fun post(@RequestBody teamRequest: ObjectRequest<RequestTeam>): CreatedResponse<ResponseTeam> {
        teamRequest.data.validate()
        val created = teamService.save(teamRequest.data)
        return CreatedResponse(created)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID): DeletedResponse {
        teamService.delete(id)
        return DeletedResponse()
    }
}