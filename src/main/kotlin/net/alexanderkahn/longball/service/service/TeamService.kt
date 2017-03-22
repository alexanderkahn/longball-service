package net.alexanderkahn.longball.service.service

import net.alexanderkahn.longball.service.model.RosterPlayer
import net.alexanderkahn.longball.service.model.Team
import net.alexanderkahn.longball.service.persistence.RosterPlayerRepository
import net.alexanderkahn.longball.service.persistence.TeamRepository
import net.alexanderkahn.longball.service.persistence.assembler.RosterPlayerAssembler
import net.alexanderkahn.longball.service.persistence.assembler.TeamAssembler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class TeamService(@Autowired private val teamRepository: TeamRepository, @Autowired private val rosterPlayerRespository: RosterPlayerRepository) {

    private val teamAssembler: TeamAssembler = TeamAssembler()
    private val rosterPlayerAssembler: RosterPlayerAssembler = RosterPlayerAssembler()

    fun getAll(pageable: Pageable): Page<Team> {
        val teams = teamRepository.findAll(pageable)
        return teams.map { teamAssembler.toModel(it)}
    }

    fun get(id: String): Team {
        val team = teamRepository.findOne(id)
        return teamAssembler.toModel(team)
    }

    fun getRoster(teamId: String, pageable: Pageable): Page<RosterPlayer> {
        val teams = rosterPlayerRespository.findByTeamId(teamId, pageable)
        return teams.map { rosterPlayerAssembler.toModel(it) }
    }
}

