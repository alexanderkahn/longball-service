package net.alexanderkahn.longball.service.service

import net.alexanderkahn.longball.service.model.League
import net.alexanderkahn.longball.service.persistence.assembler.LeagueAssembler
import net.alexanderkahn.longball.service.persistence.repository.LeagueRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class LeagueService(@Autowired private val leagueRepository: LeagueRepository) {

    private val leagueAssembler: LeagueAssembler = LeagueAssembler()

    fun get(id: Long): League {
        val league = leagueRepository.findOne(id)
        return leagueAssembler.toModel(league)
    }

    fun getAll(pageable: Pageable): Page<League> {
        val leagues = leagueRepository.findAll(pageable)
        return leagues.map { leagueAssembler.toModel(it) }
    }
}