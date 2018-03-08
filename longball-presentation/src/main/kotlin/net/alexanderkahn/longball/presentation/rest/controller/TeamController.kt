package net.alexanderkahn.longball.presentation.rest.controller

import net.alexanderkahn.longball.api.model.*
import net.alexanderkahn.longball.api.service.ITeamService
import net.alexanderkahn.longball.presentation.rest.helper.getFilterableFieldsFor
import net.alexanderkahn.longball.presentation.rest.helper.getFilters
import net.alexanderkahn.longball.presentation.rest.helper.getSearch
import net.alexanderkahn.longball.presentation.rest.helper.getSearchableFieldsFor
import net.alexanderkahn.service.commons.model.request.body.ObjectRequest
import net.alexanderkahn.service.commons.model.response.body.CollectionResponse
import net.alexanderkahn.service.commons.model.response.body.DeletedResponse
import net.alexanderkahn.service.commons.model.response.body.ObjectCreatedResponse
import net.alexanderkahn.service.commons.model.response.body.ObjectResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.*
import java.util.*


@RestController
@RequestMapping("/teams")
class TeamController(@Autowired private val teamService: ITeamService) {

    private val validTeamSearchFields = getSearchableFieldsFor(TeamAttributes::class)
    private val validTeamFilterFields = getFilterableFieldsFor(TeamRelationships::class)

    @GetMapping
    fun getAll(pageable: Pageable, @RequestParam(required = false) queryParams: MultiValueMap<String, String>?): CollectionResponse<ResponseTeam> {
        val filters = getFilters(queryParams, validTeamFilterFields)
        val search = getSearch(queryParams, validTeamSearchFields)
        val page = teamService.getAll(pageable, filters, search)
        return page.toCollectionResponse()
    }

    @GetMapping("/{id}")
    fun get(@PathVariable id: UUID): ObjectResponse<ResponseTeam> {
        return ObjectResponse(teamService.get(id))
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun post(@RequestBody teamRequest: ObjectRequest<RequestTeam>): ObjectCreatedResponse<ResponseTeam> {
//        teamRequest.data.validate()
        val created = teamService.save(teamRequest.data)
        return ObjectCreatedResponse(created)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID): DeletedResponse {
        teamService.delete(id)
        return DeletedResponse()
    }
}