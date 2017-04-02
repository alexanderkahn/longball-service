package net.alexanderkahn.longball.service.service

import net.alexanderkahn.base.servicebase.service.UserContext
import net.alexanderkahn.longball.service.model.League
import net.alexanderkahn.longball.service.persistence.assembler.toModel
import net.alexanderkahn.longball.service.persistence.repository.LeagueRepository
import net.alexanderkahn.longball.service.persistence.repository.getPersistenceUser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class LeagueService(@Autowired private val leagueRepository: LeagueRepository) {

    fun get(id: Long): League {
        val league = leagueRepository.findByIdAndOwner(id, UserContext.getPersistenceUser())
        return league.toModel()
    }

    fun getAll(pageable: Pageable): Page<League> {
        val leagues = leagueRepository.findByOwner(pageable, UserContext.getPersistenceUser())
        return leagues.map { it.toModel() }
    }
}