package net.alexanderkahn.longball.core.service

import net.alexanderkahn.longball.core.assembler.pxUser
import net.alexanderkahn.longball.core.assembler.toModel
import net.alexanderkahn.longball.model.League
import net.alexanderkahn.longball.core.persistence.repository.LeagueRepository
import net.alexanderkahn.servicebase.core.security.UserContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class LeagueService(@Autowired private val leagueRepository: LeagueRepository) {

    fun get(id: Long): League {
        val league = leagueRepository.findByIdAndOwner(id, UserContext.pxUser)
        return league.toModel()
    }

    fun getAll(pageable: Pageable): Page<League> {
        val leagues = leagueRepository.findByOwner(pageable, UserContext.pxUser)
        return leagues.map { it.toModel() }
    }
}