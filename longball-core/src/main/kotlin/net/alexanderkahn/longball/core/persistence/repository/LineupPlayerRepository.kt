package net.alexanderkahn.longball.core.persistence.repository

import net.alexanderkahn.longball.core.persistence.EmbeddableUser
import net.alexanderkahn.longball.core.persistence.model.PxGame
import net.alexanderkahn.longball.core.persistence.model.PxLineupPlayer
import net.alexanderkahn.longball.model.Side
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface LineupPlayerRepository : LongballRepository<PxLineupPlayer> {

    fun findByGameAndSideAndOwner(pageable: Pageable, game: PxGame, side: Side, owner: EmbeddableUser): Page<PxLineupPlayer>
    fun findFirstByGameAndSideAndBattingOrderAndOwner(game: PxGame, side: Side, battingOrder: Int, owner: EmbeddableUser): PxLineupPlayer
    fun findFirstByGameAndSideAndFieldPositionAndOwner(game: PxGame, side: Side, fieldPosition: Int, owner: EmbeddableUser): PxLineupPlayer
}