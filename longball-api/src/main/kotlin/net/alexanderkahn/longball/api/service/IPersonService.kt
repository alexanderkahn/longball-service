package net.alexanderkahn.longball.api.service

import net.alexanderkahn.longball.api.model.RequestPerson
import net.alexanderkahn.longball.api.model.ResponsePerson
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

interface IPersonService {

    fun getAll(pageable: Pageable): Page<ResponsePerson>
    fun get(id: UUID): ResponsePerson
    fun save(person: RequestPerson): ResponsePerson
    fun delete(id: UUID)

}