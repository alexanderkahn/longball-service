package net.alexanderkahn.service.longball.api

import net.alexanderkahn.longball.model.RosterPlayer
import net.alexanderkahn.longball.model.Team
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface ITeamService {

    fun getAll(pageable: Pageable): Page<Team>
    fun get(id: Long): Team
    fun getRoster(teamId: Long, pageable: Pageable): Page<RosterPlayer>

}