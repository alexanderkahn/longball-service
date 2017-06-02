package net.alexanderkahn.longball.persistence.repository

import net.alexanderkahn.longball.persistence.EmbeddableUser
import net.alexanderkahn.longball.persistence.model.PxGame
import net.alexanderkahn.longball.persistence.model.PxInning
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface InningRepository: LongballRepository<PxInning> {
    fun findFirstByOwnerAndGameOrderByIdDesc(owner: EmbeddableUser, game: PxGame): PxInning?
    fun findByOwnerAndGameAndInningNumber(owner: EmbeddableUser, game: PxGame, inningNumber: Number): PxInning?
    fun findByOwnerAndGame(pageable: Pageable, owner: EmbeddableUser, game: PxGame): Page<PxInning>
}