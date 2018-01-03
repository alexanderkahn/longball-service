package net.alexanderkahn.longball.provider.service

import net.alexanderkahn.longball.api.service.ITeamService
import net.alexanderkahn.longball.model.dto.RequestTeam
import net.alexanderkahn.longball.model.dto.ResponseTeam
import net.alexanderkahn.longball.provider.assembler.TeamAssembler
import net.alexanderkahn.longball.provider.assembler.toResponse
import net.alexanderkahn.longball.provider.entity.TeamEntity
import net.alexanderkahn.longball.provider.repository.TeamRepository
import net.alexanderkahn.service.base.model.exception.NotFoundException
import net.alexanderkahn.service.base.model.request.filter.RequestResourceSearch
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*

@Service
class TeamService @Autowired constructor(
        private val userService: UserService,
        private val teamRepository: TeamRepository,
        private val teamAssembler: TeamAssembler) : ITeamService {

    override fun getAll(pageable: Pageable, search: RequestResourceSearch?): Page<ResponseTeam> {
        val teams = if (search == null) {
            teamRepository.findByOwnerOrderByCreated(userService.userEntity(), pageable)
        } else {
            val specification = SpecificationBuilder.matchSearch<TeamEntity>(userService.userEntity(), search)
            teamRepository.findAll(specification, pageable)
        }
        return teams.map { it.toResponse() }
    }

    override fun get(id: UUID): ResponseTeam {
        val team = teamRepository.findByIdAndOwner(id, userService.userEntity())
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

