package net.alexanderkahn.longball.presentation.rest.controller

import net.alexanderkahn.longball.api.service.IPersonService
import net.alexanderkahn.longball.model.dto.RequestPerson
import net.alexanderkahn.longball.model.dto.ResponsePerson
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
@RequestMapping("/people")
class PersonController(@Autowired private val personService: IPersonService) {

    @GetMapping
    fun getAll(pageable: Pageable): CollectionResponse<ResponsePerson> {
        val people = personService.getAll(pageable)
        return people.toCollectionResponse()
    }

    @PostMapping
    fun post(@RequestBody personRequest: ObjectRequest<RequestPerson>): CreatedResponse<ResponsePerson> {
        personRequest.data.validate()
        val created = personService.save(personRequest.data)
        return CreatedResponse(created)
    }

    @GetMapping("/{id}")
    fun get(@PathVariable id: UUID): ObjectResponse<ResponsePerson> {
        return ObjectResponse(personService.get(id))
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID): DeletedResponse {
        personService.delete(id)
        return DeletedResponse()
    }
}