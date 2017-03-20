package net.alexanderkahn.longball.service.service

import net.alexanderkahn.longball.service.model.Team
import net.alexanderkahn.longball.service.persistence.TeamRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class TeamService(@Autowired private val teamRepository: TeamRepository) {

    fun getAll(pageable: Pageable): Page<Team> {
        return teamRepository.findAll(pageable)
    }

    fun get(id: String): Team {
        return teamRepository.findOne(id)
    }
}