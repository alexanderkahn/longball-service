package net.alexanderkahn.longball.provider.repository

import net.alexanderkahn.longball.provider.entity.UserEntity
import net.alexanderkahn.longball.provider.entity.GameEntity
import net.alexanderkahn.longball.provider.entity.InningEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface InningRepository: LongballRepository<InningEntity> {
    fun findFirstByOwnerAndGameOrderByIdDesc(owner: UserEntity, game: GameEntity): InningEntity?
    fun findByOwnerAndGameAndInningNumber(owner: UserEntity, game: GameEntity, inningNumber: Number): InningEntity?
    fun findByOwnerAndGame(pageable: Pageable, owner: UserEntity, game: GameEntity): Page<InningEntity>
}