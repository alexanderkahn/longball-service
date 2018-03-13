package net.alexanderkahn.longball.core.repository

import net.alexanderkahn.longball.core.entity.UserEntity
import net.alexanderkahn.longball.core.entity.RosterPositionEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

interface RosterPositionRepository : LongballRepository<RosterPositionEntity> {

    fun findByTeamIdAndOwner(pageable: Pageable, teamId: UUID, owner: UserEntity): Page<RosterPositionEntity>

}

