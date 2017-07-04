package net.alexanderkahn.longball.provider.persistence.repository

import net.alexanderkahn.longball.provider.persistence.EmbeddableUser
import net.alexanderkahn.longball.provider.persistence.model.PxRosterPlayer
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface RosterPlayerRepository: LongballRepository<PxRosterPlayer> {

    fun findByTeamIdAndOwner(pageable: Pageable, teamId: Long, owner: EmbeddableUser): Page<PxRosterPlayer>

}

