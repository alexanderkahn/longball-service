package net.alexanderkahn.longball.provider.repository

import net.alexanderkahn.longball.provider.entity.EmbeddableUser
import net.alexanderkahn.longball.provider.entity.RosterPositionEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

interface RosterPositionRepository : LongballRepository<RosterPositionEntity> {

    fun findByTeamIdAndOwner(pageable: Pageable, teamId: UUID, owner: EmbeddableUser): Page<RosterPositionEntity>

}

