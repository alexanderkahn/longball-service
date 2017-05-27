package net.alexanderkahn.longball.service.persistence.repository

import net.alexanderkahn.base.servicebase.service.UserContext
import net.alexanderkahn.longball.service.model.Side
import net.alexanderkahn.longball.service.persistence.EmbeddableUser
import net.alexanderkahn.longball.service.persistence.entity.PxInning
import net.alexanderkahn.longball.service.persistence.entity.PxInningSide

interface InningSideRepository: LongballRepository<PxInningSide> {
    fun findByInningAndOwner(inning: PxInning, owner: EmbeddableUser = UserContext.getPersistenceUser()): List<PxInningSide>
    fun findByInningAndSideAndOwner(inning: PxInning, side: Side, owner: EmbeddableUser = UserContext.getPersistenceUser()): PxInningSide?
}