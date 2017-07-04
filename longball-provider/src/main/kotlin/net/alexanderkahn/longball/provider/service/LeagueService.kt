package net.alexanderkahn.longball.provider.service

import javassist.NotFoundException
import net.alexanderkahn.longball.model.League
import net.alexanderkahn.longball.provider.assembler.pxUser
import net.alexanderkahn.longball.provider.repository.LeagueRepository
import net.alexanderkahn.service.base.api.security.UserContext
import net.alexanderkahn.service.longball.api.ILeagueService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*

@Service
class LeagueService(@Autowired private val leagueRepository: LeagueRepository) : ILeagueService {

    override fun get(id: UUID): League {
        val league = leagueRepository.findByIdAndOwner(id, UserContext.pxUser)
        return league?.toModel() ?: throw NotFoundException("Unable to find league with id: $id")
    }

    override fun getAll(pageable: Pageable): Page<League> {
        val leagues = leagueRepository.findByOwner(pageable, UserContext.pxUser)
        return leagues.map { it.toModel() }
    }
}