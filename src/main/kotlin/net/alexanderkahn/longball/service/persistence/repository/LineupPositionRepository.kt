package net.alexanderkahn.longball.service.persistence.repository

import net.alexanderkahn.longball.service.model.FieldPosition
import net.alexanderkahn.longball.service.model.InningHalf
import net.alexanderkahn.longball.service.persistence.model.EmbeddableUser
import net.alexanderkahn.longball.service.persistence.model.PersistenceGame
import net.alexanderkahn.longball.service.persistence.model.PersistenceLineupPosition
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface LineupPositionRepository: LongballRepository<PersistenceLineupPosition> {

    fun findByOwnerAndGameAndInningHalf(pageable: Pageable, owner: EmbeddableUser, game: PersistenceGame, inningHalf: InningHalf): Page<PersistenceLineupPosition>

    fun findFirstByOwnerAndGameAndInningHalfAndBattingOrder(owner: EmbeddableUser, game: PersistenceGame, inningHalf: InningHalf, battingOrder: Short): PersistenceLineupPosition

    fun findFirstByOwnerAndGameAndInningHalfAndFieldPosition(owner: EmbeddableUser, game: PersistenceGame, inningHalf: InningHalf, fieldPosition: FieldPosition): PersistenceLineupPosition
}