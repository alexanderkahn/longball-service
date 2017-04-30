package net.alexanderkahn.longball.service.persistence.repository

import net.alexanderkahn.base.servicebase.service.UserContext
import net.alexanderkahn.longball.service.persistence.model.EmbeddableUser
import net.alexanderkahn.longball.service.persistence.model.entity.PxGame
import net.alexanderkahn.longball.service.persistence.model.entity.PxInning

interface InningRepository: LongballRepository<PxInning> {
    fun findFirstByGameAndOwnerOrderByIdDesc(game: PxGame, owner: EmbeddableUser = UserContext.getPersistenceUser()): PxInning?
    fun findByGameAndOwner(game: PxGame, owner: EmbeddableUser = UserContext.getPersistenceUser()): List<PxInning>
}