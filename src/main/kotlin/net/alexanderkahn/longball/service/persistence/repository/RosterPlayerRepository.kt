package net.alexanderkahn.longball.service.persistence.repository

import net.alexanderkahn.longball.service.persistence.model.EmbeddableUser
import net.alexanderkahn.longball.service.persistence.model.entity.PxRosterPlayer
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface RosterPlayerRepository: LongballRepository<PxRosterPlayer> {

    fun findByOwnerAndTeamId(owner: EmbeddableUser, teamId: Long, pageable: Pageable): Page<PxRosterPlayer>

}

