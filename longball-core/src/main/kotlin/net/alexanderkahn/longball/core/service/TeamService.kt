package net.alexanderkahn.longball.core.service

import net.alexanderkahn.longball.core.assembler.pxUser
import net.alexanderkahn.longball.core.assembler.toModel
import net.alexanderkahn.longball.model.RosterPlayer
import net.alexanderkahn.longball.model.Team
import net.alexanderkahn.longball.persistence.repository.RosterPlayerRepository
import net.alexanderkahn.longball.persistence.repository.TeamRepository
import net.alexanderkahn.servicebase.core.security.UserContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class TeamService(
        @Autowired private val teamRepository: TeamRepository,
        @Autowired private val rosterPlayerRepository: RosterPlayerRepository) {

    fun getAll(pageable: Pageable): Page<Team> {
        val teams = teamRepository.findByOwner(pageable, UserContext.pxUser)
        return teams.map { it.toModel() }
    }

    fun get(id: Long): Team {
        val team = teamRepository.findByIdAndOwner(id, UserContext.pxUser)
        return team.toModel()
    }

    fun getRoster(teamId: Long, pageable: Pageable): Page<RosterPlayer> {
        val teams = rosterPlayerRepository.findByTeamIdAndOwner(pageable, teamId, UserContext.pxUser)
        return teams.map { it.toModel() }
    }
}

