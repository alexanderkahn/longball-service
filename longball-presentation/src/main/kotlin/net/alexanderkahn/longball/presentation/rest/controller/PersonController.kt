package net.alexanderkahn.longball.presentation.rest.controller

import net.alexanderkahn.longball.presentation.rest.model.*
import net.alexanderkahn.service.base.presentation.request.ObjectRequest
import net.alexanderkahn.service.base.presentation.response.CollectionResponse
import net.alexanderkahn.service.base.presentation.response.CreatedResponse
import net.alexanderkahn.service.base.presentation.response.DeletedResponse
import net.alexanderkahn.service.base.presentation.response.ObjectResponse
import net.alexanderkahn.service.base.presentation.response.body.data.ResponseResourceCollection
import net.alexanderkahn.service.longball.api.IPersonService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/people")
class PersonController(@Autowired private val personService: IPersonService) {

    @GetMapping
    fun getAll(pageable: Pageable): CollectionResponse<ResponsePerson> {
        val people = personService.getAll(pageable)
        return people.map { it.toResponse() }.toCollectionResponse()
    }

    @PostMapping
    fun post(@RequestBody personRequest: ObjectRequest<RequestPerson>): CreatedResponse<ResponsePerson> {
        personRequest.data.validate()
        val created = personService.save(personRequest.data.toDto())
        return CreatedResponse(created.toResponse())
    }

    @GetMapping("/{id}")
    fun get(@PathVariable id: UUID): ObjectResponse<ResponsePerson> {
        return ObjectResponse(personService.get(id).toResponse())
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID): DeletedResponse {
        personService.delete(id)
        return DeletedResponse()
    }
}