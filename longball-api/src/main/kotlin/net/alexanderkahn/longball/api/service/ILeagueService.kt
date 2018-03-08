package net.alexanderkahn.longball.api.service

import net.alexanderkahn.longball.api.model.RequestLeague
import net.alexanderkahn.longball.api.model.ResponseLeague
import net.alexanderkahn.service.commons.model.request.parameter.RequestResourceSearch
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*
import javax.validation.Valid

interface ILeagueService {

    fun get(id: UUID): ResponseLeague
    fun getAll(pageable: Pageable, search: RequestResourceSearch? = null): Page<ResponseLeague>
    fun save(@Valid league: RequestLeague): ResponseLeague
    fun delete(id: UUID)
}