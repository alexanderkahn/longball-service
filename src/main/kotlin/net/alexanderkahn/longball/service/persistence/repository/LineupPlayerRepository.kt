package net.alexanderkahn.longball.service.persistence.repository

import net.alexanderkahn.base.servicebase.service.UserContext
import net.alexanderkahn.longball.service.model.FieldPosition
import net.alexanderkahn.longball.service.model.Side
import net.alexanderkahn.longball.service.persistence.EmbeddableUser
import net.alexanderkahn.longball.service.persistence.entity.PxGame
import net.alexanderkahn.longball.service.persistence.entity.PxLineupPlayer
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface LineupPlayerRepository : LongballRepository<PxLineupPlayer> {

    fun findByGameAndSideAndOwner(pageable: Pageable, game: PxGame, side: Side, owner: EmbeddableUser = UserContext.getPersistenceUser()): Page<PxLineupPlayer>

    fun findFirstByGameAndSideAndBattingOrderAndOwner(game: PxGame, side: Side, battingOrder: Int, owner: EmbeddableUser = UserContext.getPersistenceUser()): PxLineupPlayer

    fun findFirstByGameAndSideAndFieldPositionAndOwner(game: PxGame, side: Side, fieldPosition: FieldPosition, owner: EmbeddableUser = UserContext.getPersistenceUser()): PxLineupPlayer
}