package net.alexanderkahn.longball.provider.service

import net.alexanderkahn.longball.api.service.ILeagueService
import net.alexanderkahn.longball.model.dto.RequestLeague
import net.alexanderkahn.longball.model.dto.ResponseLeague
import net.alexanderkahn.longball.provider.assembler.LeagueAssembler
import net.alexanderkahn.longball.provider.assembler.toResponse
import net.alexanderkahn.longball.provider.entity.LeagueEntity
import net.alexanderkahn.longball.provider.repository.LeagueRepository
import net.alexanderkahn.service.base.model.exception.NotFoundException
import net.alexanderkahn.service.base.model.request.filter.RequestResourceSearch
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*

@Service
class LeagueService @Autowired constructor(
        private val userService: UserService,
        private val leagueRepository: LeagueRepository,
        private val leagueAssembler: LeagueAssembler
) : ILeagueService {

    override fun get(id: UUID): ResponseLeague {
        val league = leagueRepository.findByIdAndOwner(id, userService.userEntity())
        return league?.toResponse() ?: throw NotFoundException("leagues", id)
    }

    override fun getAll(pageable: Pageable, search: RequestResourceSearch?): Page<ResponseLeague> {
        val leagues = if (search == null) {
            leagueRepository.findByOwnerOrderByCreated(userService.userEntity(), pageable)
        } else {
            val specification = SpecificationBuilder.build<LeagueEntity>(userService.userEntity(), emptyList(), search)
            leagueRepository.findAll(specification, pageable)
        }
        return leagues.map { it.toResponse() }
    }

    override fun save(league: RequestLeague): ResponseLeague {
        val entity = leagueAssembler.toEntity(league)
        leagueRepository.save(entity)
        return entity.toResponse()
    }

    override fun delete(id: UUID) {
        if (!leagueRepository.exists(id)) {
            throw NotFoundException("leagues", id)
        }
        leagueRepository.delete(id)
    }
}