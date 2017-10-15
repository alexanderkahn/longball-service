package net.alexanderkahn.longball.presentation.rest

import net.alexanderkahn.longball.model.Person
import net.alexanderkahn.service.longball.api.IPersonService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
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
    fun getAll(pageable: Pageable): Page<Person> {
        return personService.getAll(pageable)
    }

    @GetMapping("/{id}")
    fun get(@PathVariable id: UUID): Person {
        return personService.get(id)
    }
}