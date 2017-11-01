package net.alexanderkahn.longball.api.service

import net.alexanderkahn.longball.model.dto.RequestTeam
import net.alexanderkahn.longball.model.dto.ResponseTeam
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

interface ITeamService {

    fun getAll(pageable: Pageable): Page<ResponseTeam>
    fun get(id: UUID): ResponseTeam
    fun save(team: RequestTeam): ResponseTeam
    fun delete(id: UUID)

}