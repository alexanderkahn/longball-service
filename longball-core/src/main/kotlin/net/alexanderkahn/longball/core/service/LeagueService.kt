package net.alexanderkahn.longball.core.service

import net.alexanderkahn.longball.api.exception.ResourceNotFoundException
import net.alexanderkahn.longball.model.RequestLeague
import net.alexanderkahn.longball.model.ResponseLeague
import net.alexanderkahn.longball.api.service.ILeagueService
import net.alexanderkahn.longball.core.assembler.LeagueAssembler
import net.alexanderkahn.longball.core.assembler.toResponse
import net.alexanderkahn.longball.core.entity.LeagueEntity
import net.alexanderkahn.longball.core.repository.LeagueRepository
import net.alexanderkahn.service.commons.model.request.parameter.RequestResourceSearch
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.validation.annotation.Validated
import java.util.*
import javax.validation.Valid

@Service
@Validated
open class LeagueService @Autowired constructor(
        private val userService: UserService,
        private val leagueRepository: LeagueRepository,
        private val leagueAssembler: LeagueAssembler
) : ILeagueService {

    override fun get(id: UUID): ResponseLeague {
        val league = leagueRepository.findByIdAndOwner(id, userService.userEntity())
        return league?.toResponse() ?: throw ResourceNotFoundException("leagues", id)
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

    override fun save(@Valid league: RequestLeague): ResponseLeague {
        val entity = leagueAssembler.toEntity(league)
        leagueRepository.save(entity)
        return entity.toResponse()
    }

    override fun delete(id: UUID) {
        if (!leagueRepository.existsById(id)) {
            throw ResourceNotFoundException("leagues", id)
        }
        leagueRepository.deleteById(id)
    }
}