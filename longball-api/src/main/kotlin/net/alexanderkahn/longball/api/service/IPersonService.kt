package net.alexanderkahn.longball.api.service

import net.alexanderkahn.longball.model.dto.RequestPerson
import net.alexanderkahn.longball.model.dto.ResponsePerson
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

interface IPersonService {

    fun getAll(pageable: Pageable): Page<ResponsePerson>
    fun get(id: UUID): ResponsePerson
    fun save(person: RequestPerson): ResponsePerson
    fun delete(id: UUID)

}