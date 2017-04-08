package net.alexanderkahn.longball.service.service

import net.alexanderkahn.longball.service.model.RosterPlayer
import net.alexanderkahn.longball.service.model.Team
import net.alexanderkahn.longball.service.persistence.repository.RosterPlayerRepository
import net.alexanderkahn.longball.service.persistence.repository.TeamRepository
import net.alexanderkahn.longball.service.service.assembler.toModel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class TeamService(@Autowired private val teamRepository: TeamRepository, @Autowired private val rosterPlayerRepository: RosterPlayerRepository) {

    fun getAll(pageable: Pageable): Page<Team> {
        val teams = teamRepository.findByOwner(pageable)
        return teams.map { it.toModel() }
    }

    fun get(id: Long): Team {
        val team = teamRepository.findByIdAndOwner(id)
        return team.toModel()
    }

    fun getRoster(teamId: Long, pageable: Pageable): Page<RosterPlayer> {
        val teams = rosterPlayerRepository.findByTeamIdAndOwner(pageable, teamId)
        return teams.map { it.toModel() }
    }
}

