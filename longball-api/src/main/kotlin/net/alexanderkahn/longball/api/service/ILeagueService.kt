package net.alexanderkahn.longball.api.service

import net.alexanderkahn.longball.model.dto.RequestLeague
import net.alexanderkahn.longball.model.dto.ResponseLeague
import net.alexanderkahn.service.base.model.request.RequestResourceSearch
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

interface ILeagueService {

    fun get(id: UUID): ResponseLeague
    fun getAll(pageable: Pageable, search: RequestResourceSearch? = null): Page<ResponseLeague>
    fun save(league: RequestLeague): ResponseLeague
    fun delete(id: UUID)
}