package net.alexanderkahn.longball.provider.persistence.repository

import net.alexanderkahn.longball.provider.assembler.pxUser
import net.alexanderkahn.longball.provider.persistence.EmbeddableUser
import net.alexanderkahn.longball.provider.persistence.model.PxRosterPlayer
import net.alexanderkahn.service.base.api.security.UserContext
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

interface RosterPlayerRepository: LongballRepository<PxRosterPlayer> {

    fun findByTeamIdAndOwner(pageable: Pageable, teamId: UUID, owner: EmbeddableUser = UserContext.pxUser): Page<PxRosterPlayer>

}

