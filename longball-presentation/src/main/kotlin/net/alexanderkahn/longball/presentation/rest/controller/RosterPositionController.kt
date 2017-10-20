package net.alexanderkahn.longball.presentation.rest.controller

import net.alexanderkahn.longball.presentation.rest.model.*
import net.alexanderkahn.service.base.presentation.request.ObjectRequest
import net.alexanderkahn.service.base.presentation.response.CollectionResponse
import net.alexanderkahn.service.base.presentation.response.CreatedResponse
import net.alexanderkahn.service.base.presentation.response.DeletedResponse
import net.alexanderkahn.service.base.presentation.response.ObjectResponse
import net.alexanderkahn.service.base.presentation.response.body.data.ResponseResourceCollection
import net.alexanderkahn.service.longball.api.IRosterPositionService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/rosterpositions")
class RosterPositionController(@Autowired private val rosterPositionService: IRosterPositionService) {

    @GetMapping
    fun getAll(pageable: Pageable): CollectionResponse<ResponseRosterPosition> {
        val positions = rosterPositionService.getAll(pageable)
        return positions.map { it.toResponse() }.toCollectionResponse()
    }

    @PostMapping
    fun post(@RequestBody rosterRequest: ObjectRequest<RequestRosterPosition>): CreatedResponse<ResponseRosterPosition> {
        rosterRequest.data.validate()
        val created = rosterPositionService.save(rosterRequest.data.toDto())
        return CreatedResponse(created.toResponse())
    }

    @GetMapping("/{id}")
    fun get(@PathVariable id: UUID): ObjectResponse<ResponseRosterPosition> {
        return ObjectResponse(rosterPositionService.get(id).toResponse())
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID): DeletedResponse {
        rosterPositionService.delete(id)
        return DeletedResponse()
    }
}