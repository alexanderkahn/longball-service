package net.alexanderkahn.longball.provider.service

import net.alexanderkahn.longball.model.dto.RosterPositionDTO
import net.alexanderkahn.longball.model.dto.TeamDTO
import net.alexanderkahn.longball.provider.assembler.TeamAssembler
import net.alexanderkahn.longball.provider.assembler.toDTO
import net.alexanderkahn.longball.provider.repository.RosterPositionRepository
import net.alexanderkahn.longball.provider.repository.TeamRepository
import net.alexanderkahn.service.base.api.exception.NotFoundException
import net.alexanderkahn.service.longball.api.ITeamService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*

@Service
class TeamService @Autowired constructor(
        private val userService: UserService,
        private val teamRepository: TeamRepository,
        private val rosterPositionRepository: RosterPositionRepository,
        private val teamAssembler: TeamAssembler) : ITeamService {

    override fun getAll(pageable: Pageable): Page<TeamDTO> {
        val teams = teamRepository.findByOwner(pageable, userService.embeddableUser())
        return teams.map { it.toDTO() }
    }

    override fun get(id: UUID): TeamDTO {
        val team = teamRepository.findByIdAndOwner(id, userService.embeddableUser())
        return team?.toDTO() ?: throw NotFoundException("players", id)
    }

    override fun save(team: TeamDTO): TeamDTO {
        val entity = teamAssembler.toEntity(team)
        teamRepository.save(entity)
        return entity.toDTO()
    }

    override fun delete(id: UUID) {
        if (!teamRepository.exists(id)) {
            throw NotFoundException("leagues", id)
        }
        teamRepository.delete(id)
    }

    override fun getRoster(teamId: UUID, pageable: Pageable): Page<RosterPositionDTO> {
        val teamRoster = rosterPositionRepository.findByTeamIdAndOwner(pageable, teamId, userService.embeddableUser())
        return teamRoster.map { it.toDTO() }
    }
}

