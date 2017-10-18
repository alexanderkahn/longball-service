package net.alexanderkahn.longball.provider.service

import net.alexanderkahn.longball.model.dto.LeagueDTO
import net.alexanderkahn.longball.provider.assembler.embeddableUser
import net.alexanderkahn.longball.provider.assembler.toDTO
import net.alexanderkahn.longball.provider.assembler.toEntity
import net.alexanderkahn.longball.provider.repository.LeagueRepository
import net.alexanderkahn.service.base.api.exception.NotFoundException
import net.alexanderkahn.service.base.api.security.UserContext
import net.alexanderkahn.service.longball.api.ILeagueService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*

@Service
class LeagueService(@Autowired private val leagueRepository: LeagueRepository) : ILeagueService {

    override fun get(id: UUID): LeagueDTO {
        val league = leagueRepository.findByIdAndOwner(id, UserContext.embeddableUser)
        return league?.toDTO() ?: throw NotFoundException("leagues", id)
    }

    override fun getAll(pageable: Pageable): Page<LeagueDTO> {
        val leagues = leagueRepository.findByOwner(pageable, UserContext.embeddableUser)
        return leagues.map { it.toDTO() }
    }

    override fun save(league: LeagueDTO): LeagueDTO {
        val entity = league.toEntity()
        leagueRepository.save(entity)
        return entity.toDTO()
    }

    override fun delete(id: UUID) {
        if (!leagueRepository.exists(id)) {
            throw NotFoundException("leagues", id)
        }
        leagueRepository.delete(id)
    }
}