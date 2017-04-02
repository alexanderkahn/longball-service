package net.alexanderkahn.longball.service.persistence.repository

import net.alexanderkahn.longball.service.model.InningHalf
import net.alexanderkahn.longball.service.persistence.model.EmbeddableUser
import net.alexanderkahn.longball.service.persistence.model.PersistenceLineupPosition
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface LineupPositionRepository: LongballRepository<PersistenceLineupPosition> {

    fun findByOwnerAndGameIdAndInningHalf(pageable: Pageable, owner: EmbeddableUser, gameId: Long, inningHalf: InningHalf): Page<PersistenceLineupPosition>

}