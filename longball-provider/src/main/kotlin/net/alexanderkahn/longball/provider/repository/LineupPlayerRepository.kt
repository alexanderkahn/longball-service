package net.alexanderkahn.longball.provider.repository

import net.alexanderkahn.longball.provider.entity.EmbeddableUser
import net.alexanderkahn.longball.provider.entity.GameEntity
import net.alexanderkahn.longball.provider.entity.LineupPositionEntity
import net.alexanderkahn.longball.model.Side
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface LineupPlayerRepository : LongballRepository<LineupPositionEntity> {

    fun findByGameAndSideAndOwner(pageable: Pageable, game: GameEntity, side: Side, owner: EmbeddableUser): Page<LineupPositionEntity>
    fun findFirstByGameAndSideAndBattingOrderAndOwner(game: GameEntity, side: Side, battingOrder: Int, owner: EmbeddableUser): LineupPositionEntity
    fun findFirstByGameAndSideAndFieldPositionAndOwner(game: GameEntity, side: Side, fieldPosition: Int, owner: EmbeddableUser): LineupPositionEntity
}