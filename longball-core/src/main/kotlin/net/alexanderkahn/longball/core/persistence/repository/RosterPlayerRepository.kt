package net.alexanderkahn.longball.core.persistence.repository

import net.alexanderkahn.longball.core.persistence.EmbeddableUser
import net.alexanderkahn.longball.core.persistence.model.PxRosterPlayer
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface RosterPlayerRepository: LongballRepository<PxRosterPlayer> {

    fun findByTeamIdAndOwner(pageable: Pageable, teamId: Long, owner: EmbeddableUser): Page<PxRosterPlayer>

}

