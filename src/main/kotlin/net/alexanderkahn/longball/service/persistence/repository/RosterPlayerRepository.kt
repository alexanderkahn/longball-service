package net.alexanderkahn.longball.service.persistence.repository

import net.alexanderkahn.longball.service.persistence.model.PersistenceRosterPlayer
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.PagingAndSortingRepository

interface RosterPlayerRepository: PagingAndSortingRepository<PersistenceRosterPlayer, Long> {

    fun findByTeamId(teamId: Long, pageable: Pageable): Page<PersistenceRosterPlayer>
}

