package net.alexanderkahn.longball.provider.service

import net.alexanderkahn.longball.api.service.ITeamService
import net.alexanderkahn.longball.model.dto.RequestTeam
import net.alexanderkahn.longball.model.dto.ResponseTeam
import net.alexanderkahn.longball.provider.assembler.TeamAssembler
import net.alexanderkahn.longball.provider.assembler.toResponse
import net.alexanderkahn.longball.provider.entity.TeamEntity
import net.alexanderkahn.longball.provider.repository.TeamRepository
import net.alexanderkahn.service.commons.model.exception.NotFoundException
import net.alexanderkahn.service.commons.model.request.parameter.RequestResourceFilter
import net.alexanderkahn.service.commons.model.request.parameter.RequestResourceSearch
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

    override fun getAll(pageable: Pageable, filters: Collection<RequestResourceFilter>, search: RequestResourceSearch?): Page<ResponseTeam> {
        val teams = if (filters.isEmpty() && search == null) {
            teamRepository.findByOwnerOrderByCreated(userService.userEntity(), pageable)
        } else {
            val specification = SpecificationBuilder.build<TeamEntity>(userService.userEntity(), filters, search)
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
        if (!teamRepository.existsById(id)) {
            throw NotFoundException("leagues", id)
        }
        teamRepository.deleteById(id)
    }
}

