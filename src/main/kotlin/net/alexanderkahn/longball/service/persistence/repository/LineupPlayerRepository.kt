package net.alexanderkahn.longball.service.persistence.repository

import net.alexanderkahn.base.servicebase.service.UserContext
import net.alexanderkahn.longball.service.model.FieldPosition
import net.alexanderkahn.longball.service.model.InningSide
import net.alexanderkahn.longball.service.persistence.model.EmbeddableUser
import net.alexanderkahn.longball.service.persistence.model.entity.PxGame
import net.alexanderkahn.longball.service.persistence.model.entity.PxLineupPlayer
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface LineupPlayerRepository : LongballRepository<PxLineupPlayer> {

    fun findByGameAndSideAndOwner(pageable: Pageable, game: PxGame, side: InningSide, owner: EmbeddableUser = UserContext.getPersistenceUser()): Page<PxLineupPlayer>

    fun findFirstByGameAndSideAndBattingOrderAndOwner(game: PxGame, side: InningSide, battingOrder: Int, owner: EmbeddableUser = UserContext.getPersistenceUser()): PxLineupPlayer

    fun findFirstByGameAndSideAndFieldPositionAndOwner(game: PxGame, side: InningSide, fieldPosition: FieldPosition, owner: EmbeddableUser = UserContext.getPersistenceUser()): PxLineupPlayer
}