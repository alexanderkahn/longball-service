package net.alexanderkahn.longball.core.persistence.repository

import net.alexanderkahn.longball.core.persistence.EmbeddableUser
import net.alexanderkahn.longball.core.persistence.model.PxGame
import net.alexanderkahn.longball.core.persistence.model.PxLineupPlayer
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface LineupPlayerRepository : LongballRepository<PxLineupPlayer> {

    fun findByGameAndSideAndOwner(pageable: Pageable, game: PxGame, side: Int, owner: EmbeddableUser): Page<PxLineupPlayer>

    fun findFirstByGameAndSideAndBattingOrderAndOwner(game: PxGame, side: Int, battingOrder: Int, owner: EmbeddableUser): PxLineupPlayer

    fun findFirstByGameAndSideAndFieldPositionAndOwner(game: PxGame, side: Int, fieldPosition: Int, owner: EmbeddableUser): PxLineupPlayer
}