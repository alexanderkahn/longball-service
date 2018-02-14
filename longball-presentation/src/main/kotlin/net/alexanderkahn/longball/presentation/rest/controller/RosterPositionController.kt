package net.alexanderkahn.longball.presentation.rest.controller

import net.alexanderkahn.longball.api.service.IRosterPositionService
import net.alexanderkahn.longball.model.dto.ModelTypes
import net.alexanderkahn.longball.model.dto.RequestRosterPosition
import net.alexanderkahn.longball.model.dto.ResponseRosterPosition
import net.alexanderkahn.longball.model.dto.toCollectionResponse
import net.alexanderkahn.longball.presentation.rest.helper.IncludeHelper
import net.alexanderkahn.service.commons.model.request.ObjectRequest
import net.alexanderkahn.service.commons.model.response.CollectionResponse
import net.alexanderkahn.service.commons.model.response.CreatedResponse
import net.alexanderkahn.service.commons.model.response.DeletedResponse
import net.alexanderkahn.service.commons.model.response.ObjectResponse
import net.alexanderkahn.service.commons.model.response.body.data.ResponseResourceObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
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
    fun post(@RequestBody rosterRequest: ObjectRequest<RequestRosterPosition>): CreatedResponse<ResponseRosterPosition> {
        rosterRequest.data.validate()
        val created = rosterPositionService.save(rosterRequest.data)
        return CreatedResponse(created)
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

    private fun getIncluded(includeTypes: List<String>?, positions: List<ResponseRosterPosition>): List<ResponseResourceObject>? {
        return if (includeTypes?.contains("player") == true) {
            includeHelper.include(mapOf(ModelTypes.PEOPLE to positions.map { it.relationships.player.data.id }))
        } else {
            null
        }
    }
}