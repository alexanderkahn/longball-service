package net.alexanderkahn.longball.provider.repository

import net.alexanderkahn.longball.provider.entity.EmbeddableUser
import net.alexanderkahn.longball.provider.entity.GameEntity
import net.alexanderkahn.longball.provider.entity.InningEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface InningRepository: LongballRepository<InningEntity> {
    fun findFirstByOwnerAndGameOrderByIdDesc(owner: EmbeddableUser, game: GameEntity): InningEntity?
    fun findByOwnerAndGameAndInningNumber(owner: EmbeddableUser, game: GameEntity, inningNumber: Number): InningEntity?
    fun findByOwnerAndGame(pageable: Pageable, owner: EmbeddableUser, game: GameEntity): Page<InningEntity>
}