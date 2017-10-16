package net.alexanderkahn.longball.presentation.rest

import net.alexanderkahn.longball.presentation.rest.model.ResponsePerson
import net.alexanderkahn.longball.presentation.rest.model.toMetaPage
import net.alexanderkahn.longball.presentation.rest.model.toResponse
import net.alexanderkahn.service.base.presentation.response.CollectionResponse
import net.alexanderkahn.service.base.presentation.response.ObjectResponse
import net.alexanderkahn.service.base.presentation.response.body.data.ResponseResourceCollection
import net.alexanderkahn.service.longball.api.IPersonService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/people")
class PersonController(@Autowired private val personService: IPersonService) {

    @GetMapping
    fun getAll(pageable: Pageable): CollectionResponse<ResponsePerson> {
        val people = personService.getAll(pageable)
        return CollectionResponse(ResponseResourceCollection( people.content.map { it.toResponse() }), people.toMetaPage())
    }

    @GetMapping("/{id}")
    fun get(@PathVariable id: UUID): ObjectResponse<ResponsePerson> {
        return ObjectResponse(personService.get(id).toResponse())
    }
}