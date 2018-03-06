package net.alexanderkahn.longball.presentation.rest.controller

import net.alexanderkahn.longball.api.service.IPersonService
import net.alexanderkahn.longball.model.dto.RequestPerson
import net.alexanderkahn.longball.model.dto.ResponsePerson
import net.alexanderkahn.longball.model.dto.toCollectionResponse
import net.alexanderkahn.service.commons.model.request.body.ObjectRequest
import net.alexanderkahn.service.commons.model.response.body.CollectionResponse
import net.alexanderkahn.service.commons.model.response.body.DeletedResponse
import net.alexanderkahn.service.commons.model.response.body.ObjectCreatedResponse
import net.alexanderkahn.service.commons.model.response.body.ObjectResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
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
    @ResponseStatus(HttpStatus.CREATED)
    fun post(@RequestBody personRequest: ObjectRequest<RequestPerson>): ObjectCreatedResponse<ResponsePerson> {
//        personRequest.data.validate()
        val created = personService.save(personRequest.data)
        return ObjectCreatedResponse(created)
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