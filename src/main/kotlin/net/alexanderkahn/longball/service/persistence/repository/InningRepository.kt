package net.alexanderkahn.longball.service.persistence.repository

import net.alexanderkahn.servicebase.provider.security.UserContext
import net.alexanderkahn.longball.service.persistence.EmbeddableUser
import net.alexanderkahn.longball.service.persistence.entity.PxGame
import net.alexanderkahn.longball.service.persistence.entity.PxInning
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface InningRepository: LongballRepository<PxInning> {
    fun findFirstByGameAndOwnerOrderByIdDesc(game: PxGame, owner: EmbeddableUser = UserContext.getPersistenceUser()): PxInning?
    fun findByGameAndOwner(pageable: Pageable, game: PxGame, owner: EmbeddableUser = UserContext.getPersistenceUser()): Page<PxInning>
}