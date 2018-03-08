package net.alexanderkahn.longball.presentation.rest.controller

import net.alexanderkahn.longball.api.service.IRosterPositionService
import net.alexanderkahn.longball.api.model.ModelTypes
import net.alexanderkahn.longball.api.model.RequestRosterPosition
import net.alexanderkahn.longball.api.model.ResponseRosterPosition
import net.alexanderkahn.longball.api.model.toCollectionResponse
import net.alexanderkahn.longball.presentation.rest.helper.IncludeHelper
import net.alexanderkahn.service.commons.model.request.body.ObjectRequest
import net.alexanderkahn.service.commons.model.response.body.CollectionResponse
import net.alexanderkahn.service.commons.model.response.body.DeletedResponse
import net.alexanderkahn.service.commons.model.response.body.ObjectCreatedResponse
import net.alexanderkahn.service.commons.model.response.body.ObjectResponse
import net.alexanderkahn.service.commons.model.response.body.data.ResourceObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/rosterpositions")
class RosterPositionController(
        @Autowired private val rosterPositionService: IRosterPositionService,
        @Autowired private val includeHelper: IncludeHelper) {

    @GetMapping
    fun getAll(pageable: Pageable, @RequestParam("include", required = false) includeTypes: List<String>?): CollectionResponse<ResponseRosterPosition> {
        val positions = rosterPositionService.getAll(pageable)
        val included = getIncluded(includeTypes, positions.content)
        return positions.toCollectionResponse(included)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun post(@RequestBody rosterRequest: ObjectRequest<RequestRosterPosition>): ObjectCreatedResponse<ResponseRosterPosition> {
//        rosterRequest.data.validate()
        val created = rosterPositionService.save(rosterRequest.data)
        return ObjectCreatedResponse(created)
    }

    @GetMapping("/{id}")
    fun get(@PathVariable id: UUID, @RequestParam("include", required = false) includeTypes: List<String>?): ObjectResponse<ResponseRosterPosition> {
        val position = rosterPositionService.get(id)
        val included = getIncluded(includeTypes, listOf(position))
        return ObjectResponse(position, included)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID): DeletedResponse {
        rosterPositionService.delete(id)
        return DeletedResponse()
    }

    private fun getIncluded(includeTypes: List<String>?, positions: List<ResponseRosterPosition>): List<ResourceObject>? {
        return if (includeTypes?.contains("player") == true) {
            includeHelper.include(mapOf(ModelTypes.PEOPLE to positions.map { it.relationships.player.data.id }))
        } else {
            null
        }
    }
}