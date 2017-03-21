package net.alexanderkahn.longball.service.persistence

import net.alexanderkahn.longball.service.persistence.model.PersistenceRosterPlayer
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.PagingAndSortingRepository

interface RosterPlayerRepository: PagingAndSortingRepository<PersistenceRosterPlayer, String> {

    fun findByTeamId(teamId: String, pageable: Pageable): Page<PersistenceRosterPlayer>
}
