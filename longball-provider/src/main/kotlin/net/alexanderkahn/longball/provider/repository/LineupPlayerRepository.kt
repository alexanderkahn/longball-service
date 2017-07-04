package net.alexanderkahn.longball.provider.repository

import net.alexanderkahn.longball.provider.entity.EmbeddableUser
import net.alexanderkahn.longball.provider.entity.PxGame
import net.alexanderkahn.longball.provider.entity.PxLineupPlayer
import net.alexanderkahn.longball.model.Side
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface LineupPlayerRepository : LongballRepository<PxLineupPlayer> {

    fun findByGameAndSideAndOwner(pageable: Pageable, game: PxGame, side: Side, owner: EmbeddableUser): Page<PxLineupPlayer>
    fun findFirstByGameAndSideAndBattingOrderAndOwner(game: PxGame, side: Side, battingOrder: Int, owner: EmbeddableUser): PxLineupPlayer
    fun findFirstByGameAndSideAndFieldPositionAndOwner(game: PxGame, side: Side, fieldPosition: Int, owner: EmbeddableUser): PxLineupPlayer
}