package net.alexanderkahn.longball.persistence.repository

import net.alexanderkahn.longball.persistence.EmbeddableUser
import net.alexanderkahn.longball.persistence.model.PxGame
import net.alexanderkahn.longball.persistence.model.PxInning
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface InningRepository: LongballRepository<PxInning> {
    fun findFirstByGameAndOwnerOrderByIdDesc(game: PxGame, owner: EmbeddableUser): PxInning?
    fun findByGameAndOwner(pageable: Pageable, game: PxGame, owner: EmbeddableUser): Page<PxInning>
}