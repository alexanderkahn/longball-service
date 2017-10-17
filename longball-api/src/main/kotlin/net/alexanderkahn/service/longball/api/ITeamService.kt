package net.alexanderkahn.service.longball.api

import net.alexanderkahn.longball.model.dto.PlayerDTO
import net.alexanderkahn.longball.model.dto.TeamDTO
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

interface ITeamService {

    fun getAll(pageable: Pageable): Page<TeamDTO>
    fun get(id: UUID): TeamDTO
    fun save(team: TeamDTO): TeamDTO
    fun delete(id: UUID)
    fun getRoster(teamId: UUID, pageable: Pageable): Page<PlayerDTO>

}