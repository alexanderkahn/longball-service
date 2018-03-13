package net.alexanderkahn.longball.api.service

import net.alexanderkahn.longball.model.RequestPerson
import net.alexanderkahn.longball.model.ResponsePerson
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*
import javax.validation.Valid

interface IPersonService {

    fun getAll(pageable: Pageable): Page<ResponsePerson>
    fun get(id: UUID): ResponsePerson
    fun save(@Valid person: RequestPerson): ResponsePerson
    fun delete(id: UUID)

}