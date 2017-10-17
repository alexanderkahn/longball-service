package net.alexanderkahn.service.longball.api

import net.alexanderkahn.longball.model.dto.PersonDTO
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

interface IPersonService {

    fun getAll(pageable: Pageable): Page<PersonDTO>
    fun get(id: UUID): PersonDTO
    fun save(person: PersonDTO): PersonDTO
    fun delete(id: UUID)

}