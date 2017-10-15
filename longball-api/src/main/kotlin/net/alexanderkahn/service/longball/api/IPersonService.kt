package net.alexanderkahn.service.longball.api

import net.alexanderkahn.longball.model.Person
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

interface IPersonService {

    fun getAll(pageable: Pageable): Page<Person>
    fun get(id: UUID): Person

}