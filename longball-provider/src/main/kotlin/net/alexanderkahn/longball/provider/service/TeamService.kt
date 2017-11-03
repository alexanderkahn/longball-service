package net.alexanderkahn.longball.provider.service

import net.alexanderkahn.longball.api.service.ITeamService
import net.alexanderkahn.longball.model.dto.RequestTeam
import net.alexanderkahn.longball.model.dto.ResponseTeam
import net.alexanderkahn.longball.provider.assembler.TeamAssembler
import net.alexanderkahn.longball.provider.assembler.toResponse
import net.alexanderkahn.longball.provider.repository.RosterPositionRepository
import net.alexanderkahn.longball.provider.repository.TeamRepository
import net.alexanderkahn.service.base.model.exception.NotFoundException
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

    override fun getAll(pageable: Pageable): Page<ResponseTeam> {
        val teams = teamRepository.findByOwnerOrderByCreated(pageable, userService.embeddableUser())
        return teams.map { it.toResponse() }
    }

    override fun get(id: UUID): ResponseTeam {
        val team = teamRepository.findByIdAndOwner(id, userService.embeddableUser())
        return team?.toResponse() ?: throw NotFoundException("players", id)
    }

    override fun save(team: RequestTeam): ResponseTeam {
        val entity = teamAssembler.toEntity(team)
        teamRepository.save(entity)
        return entity.toResponse()
    }

    override fun delete(id: UUID) {
        if (!teamRepository.exists(id)) {
            throw NotFoundException("leagues", id)
        }
        teamRepository.delete(id)
    }
}

