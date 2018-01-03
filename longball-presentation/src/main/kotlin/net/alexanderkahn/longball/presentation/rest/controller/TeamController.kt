package net.alexanderkahn.longball.presentation.rest.controller

import net.alexanderkahn.longball.api.service.ITeamService
import net.alexanderkahn.longball.model.dto.RequestTeam
import net.alexanderkahn.longball.model.dto.ResponseTeam
import net.alexanderkahn.longball.model.dto.TeamAttributes
import net.alexanderkahn.longball.model.dto.toCollectionResponse
import net.alexanderkahn.longball.presentation.rest.helper.getSearch
import net.alexanderkahn.service.base.model.request.ObjectRequest
import net.alexanderkahn.service.base.model.response.CollectionResponse
import net.alexanderkahn.service.base.model.response.CreatedResponse
import net.alexanderkahn.service.base.model.response.DeletedResponse
import net.alexanderkahn.service.base.model.response.ObjectResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.*
import java.util.*
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.javaType


@RestController
@RequestMapping("/teams")
class TeamController(@Autowired private val teamService: ITeamService) {

    private val validTeamSearchFields = TeamAttributes::class.memberProperties.filter { it.returnType.javaType == String::class.java }.map { it.name }

    @GetMapping
    fun getAll(pageable: Pageable, @RequestParam(required = false) queryParams: MultiValueMap<String, String>?): CollectionResponse<ResponseTeam> {
        val search = getSearch(queryParams, validTeamSearchFields)
        val page = teamService.getAll(pageable, search)
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