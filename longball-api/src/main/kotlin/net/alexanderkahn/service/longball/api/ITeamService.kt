package net.alexanderkahn.service.longball.api

import net.alexanderkahn.longball.model.Player
import net.alexanderkahn.longball.model.Team
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

interface ITeamService {

    fun getAll(pageable: Pageable): Page<Team>
    fun get(id: UUID): Team
    fun save(team: Team): Team
    fun delete(id: UUID)
    fun getRoster(teamId: UUID, pageable: Pageable): Page<Player>

}