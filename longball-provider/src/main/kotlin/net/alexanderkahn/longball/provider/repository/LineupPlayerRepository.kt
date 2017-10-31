package net.alexanderkahn.longball.provider.repository

import net.alexanderkahn.longball.provider.entity.UserEntity
import net.alexanderkahn.longball.provider.entity.GameEntity
import net.alexanderkahn.longball.provider.entity.LineupPositionEntity
import net.alexanderkahn.longball.model.type.Side
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface LineupPlayerRepository : LongballRepository<LineupPositionEntity> {

    fun findByGameAndSideAndOwner(pageable: Pageable, game: GameEntity, side: Side, owner: UserEntity): Page<LineupPositionEntity>
    fun findFirstByGameAndSideAndBattingOrderAndOwner(game: GameEntity, side: Side, battingOrder: Int, owner: UserEntity): LineupPositionEntity
    fun findFirstByGameAndSideAndFieldPositionAndOwner(game: GameEntity, side: Side, fieldPosition: Int, owner: UserEntity): LineupPositionEntity
}