package net.alexanderkahn.longball.service.persistence.repository

import net.alexanderkahn.longball.service.model.FieldPosition
import net.alexanderkahn.longball.service.model.InningHalf
import net.alexanderkahn.longball.service.persistence.model.EmbeddableUser
import net.alexanderkahn.longball.service.persistence.model.entity.PxGame
import net.alexanderkahn.longball.service.persistence.model.entity.PxLineupPlayer
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface LineupPlayerRepository : LongballRepository<PxLineupPlayer> {

    fun findByOwnerAndGameAndInningHalf(pageable: Pageable, owner: EmbeddableUser, game: PxGame, inningHalf: InningHalf): Page<PxLineupPlayer>

    fun findFirstByOwnerAndGameAndInningHalfAndBattingOrder(owner: EmbeddableUser, game: PxGame, inningHalf: InningHalf, battingOrder: Short): PxLineupPlayer

    fun findFirstByOwnerAndGameAndInningHalfAndFieldPosition(owner: EmbeddableUser, game: PxGame, inningHalf: InningHalf, fieldPosition: FieldPosition): PxLineupPlayer
}