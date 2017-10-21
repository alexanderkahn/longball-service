package net.alexanderkahn.longball.presentation.rest.controller

import net.alexanderkahn.longball.model.dto.RosterPositionDTO
import net.alexanderkahn.longball.presentation.rest.helper.IncludeHelper
import net.alexanderkahn.longball.presentation.rest.model.*
import net.alexanderkahn.service.base.presentation.request.ObjectRequest
import net.alexanderkahn.service.base.presentation.response.CollectionResponse
import net.alexanderkahn.service.base.presentation.response.CreatedResponse
import net.alexanderkahn.service.base.presentation.response.DeletedResponse
import net.alexanderkahn.service.base.presentation.response.ObjectResponse
import net.alexanderkahn.service.base.presentation.response.body.data.ResponseResourceObject
import net.alexanderkahn.service.longball.api.IRosterPositionService
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
        return positions.map { it.toResponse() }.toCollectionResponse(included)
    }

    @PostMapping
    fun post(@RequestBody rosterRequest: ObjectRequest<RequestRosterPosition>): CreatedResponse<ResponseRosterPosition> {
        rosterRequest.data.validate()
        val created = rosterPositionService.save(rosterRequest.data.toDto())
        return CreatedResponse(created.toResponse())
    }

    @GetMapping("/{id}")
    fun get(@PathVariable id: UUID, @RequestParam("include", required = false) includeTypes: List<String>?): ObjectResponse<ResponseRosterPosition> {
        val position = rosterPositionService.get(id)
        val included = getIncluded(includeTypes, listOf(position))
        return ObjectResponse(position.toResponse(), included)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID): DeletedResponse {
        rosterPositionService.delete(id)
        return DeletedResponse()
    }

    private fun getIncluded(includeTypes: List<String>?, positions: List<RosterPositionDTO>): List<ResponseResourceObject>? {
        return if (includeTypes?.contains("player") == true) {
            includeHelper.include(mapOf(ModelTypes.PEOPLE to positions.map { it.player }))
        } else {
            null
        }
    }
}