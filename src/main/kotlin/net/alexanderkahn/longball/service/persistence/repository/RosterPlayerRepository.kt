package net.alexanderkahn.longball.service.persistence.repository

import net.alexanderkahn.servicebase.provider.security.UserContext
import net.alexanderkahn.longball.service.persistence.EmbeddableUser
import net.alexanderkahn.longball.service.persistence.entity.PxRosterPlayer
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface RosterPlayerRepository: LongballRepository<PxRosterPlayer> {

    fun findByTeamIdAndOwner(pageable: Pageable, teamId: Long, owner: EmbeddableUser = UserContext.getPersistenceUser()): Page<PxRosterPlayer>

}

