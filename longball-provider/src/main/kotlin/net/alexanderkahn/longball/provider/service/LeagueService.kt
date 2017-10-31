package net.alexanderkahn.longball.provider.service

import net.alexanderkahn.longball.model.dto.LeagueDTO
import net.alexanderkahn.longball.provider.assembler.LeagueAssembler
import net.alexanderkahn.longball.provider.assembler.toDTO
import net.alexanderkahn.longball.provider.repository.LeagueRepository
import net.alexanderkahn.service.base.api.exception.NotFoundException
import net.alexanderkahn.service.longball.api.ILeagueService
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

    override fun get(id: UUID): LeagueDTO {
        val league = leagueRepository.findByIdAndOwner(id, userService.embeddableUser())
        return league?.toDTO() ?: throw NotFoundException("leagues", id)
    }

    override fun getAll(pageable: Pageable): Page<LeagueDTO> {
        val leagues = leagueRepository.findByOwner(pageable, userService.embeddableUser())
        return leagues.map { it.toDTO() }
    }

    override fun save(league: LeagueDTO): LeagueDTO {
        val entity = leagueAssembler.toEntity(league)
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