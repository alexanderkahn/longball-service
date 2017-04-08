package net.alexanderkahn.longball.service.persistence.repository

import net.alexanderkahn.base.servicebase.service.UserContext
import net.alexanderkahn.longball.service.model.FieldPosition
import net.alexanderkahn.longball.service.model.InningHalf
import net.alexanderkahn.longball.service.persistence.model.EmbeddableUser
import net.alexanderkahn.longball.service.persistence.model.entity.PxGame
import net.alexanderkahn.longball.service.persistence.model.entity.PxLineupPlayer
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface LineupPlayerRepository : LongballRepository<PxLineupPlayer> {

    fun findByGameAndInningHalfAndOwner(pageable: Pageable, game: PxGame, inningHalf: InningHalf, owner: EmbeddableUser = UserContext.getPersistenceUser()): Page<PxLineupPlayer>

    fun findFirstByGameAndInningHalfAndBattingOrderAndOwner(game: PxGame, inningHalf: InningHalf, battingOrder: Int, owner: EmbeddableUser = UserContext.getPersistenceUser()): PxLineupPlayer

    fun findFirstByGameAndInningHalfAndFieldPositionAndOwner(game: PxGame, inningHalf: InningHalf, fieldPosition: FieldPosition, owner: EmbeddableUser = UserContext.getPersistenceUser()): PxLineupPlayer
}