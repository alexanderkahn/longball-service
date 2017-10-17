package net.alexanderkahn.longball.provider.service

import net.alexanderkahn.longball.model.dto.PlayerDTO
import net.alexanderkahn.longball.model.dto.TeamDTO
import net.alexanderkahn.longball.provider.assembler.TeamAssembler
import net.alexanderkahn.longball.provider.assembler.pxUser
import net.alexanderkahn.longball.provider.assembler.toModel
import net.alexanderkahn.longball.provider.repository.PlayerRepository
import net.alexanderkahn.longball.provider.repository.TeamRepository
import net.alexanderkahn.service.base.api.exception.NotFoundException
import net.alexanderkahn.service.base.api.security.UserContext
import net.alexanderkahn.service.longball.api.ITeamService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*

@Service
class TeamService(
        @Autowired private val teamRepository: TeamRepository,
        @Autowired private val playerRepository: PlayerRepository,
        @Autowired private val teamAssembler: TeamAssembler) : ITeamService {

    override fun getAll(pageable: Pageable): Page<TeamDTO> {
        val teams = teamRepository.findByOwner(pageable, UserContext.pxUser)
        return teams.map { it.toModel() }
    }

    override fun get(id: UUID): TeamDTO {
        val team = teamRepository.findByIdAndOwner(id, UserContext.pxUser)
        return team?.toModel() ?: throw NotFoundException("players", id)
    }

    override fun save(team: TeamDTO): TeamDTO {
        val entity = teamAssembler.toPersistence(team)
        teamRepository.save(entity)
        return entity.toModel()
    }

    override fun delete(id: UUID) {
        if (!teamRepository.exists(id)) {
            throw NotFoundException("leagues", id)
        }
        teamRepository.delete(id)
    }

    override fun getRoster(teamId: UUID, pageable: Pageable): Page<PlayerDTO> {
        val teams = playerRepository.findByTeamIdAndOwner(pageable, teamId, UserContext.pxUser)
        return teams.map { it.toModel() }
    }
}

