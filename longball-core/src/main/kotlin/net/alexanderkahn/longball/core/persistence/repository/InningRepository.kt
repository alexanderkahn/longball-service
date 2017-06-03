package net.alexanderkahn.longball.core.persistence.repository

import net.alexanderkahn.longball.core.persistence.EmbeddableUser
import net.alexanderkahn.longball.core.persistence.model.PxGame
import net.alexanderkahn.longball.core.persistence.model.PxInning
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface InningRepository: LongballRepository<PxInning> {
    fun findFirstByOwnerAndGameOrderByIdDesc(owner: EmbeddableUser, game: PxGame): PxInning?
    fun findByOwnerAndGameAndInningNumber(owner: EmbeddableUser, game: PxGame, inningNumber: Number): PxInning?
    fun findByOwnerAndGame(pageable: Pageable, owner: EmbeddableUser, game: PxGame): Page<PxInning>
}