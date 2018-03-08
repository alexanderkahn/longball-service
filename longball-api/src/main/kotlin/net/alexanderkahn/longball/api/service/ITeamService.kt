package net.alexanderkahn.longball.api.service

import net.alexanderkahn.longball.api.model.RequestTeam
import net.alexanderkahn.longball.api.model.ResponseTeam
import net.alexanderkahn.service.commons.model.request.parameter.RequestResourceFilter
import net.alexanderkahn.service.commons.model.request.parameter.RequestResourceSearch
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*
import javax.validation.Valid

interface ITeamService {

    fun getAll(pageable: Pageable, filters: Collection<RequestResourceFilter>, search: RequestResourceSearch? = null): Page<ResponseTeam>
    fun get(id: UUID): ResponseTeam
    fun save(@Valid team: RequestTeam): ResponseTeam
    fun delete(id: UUID)

}