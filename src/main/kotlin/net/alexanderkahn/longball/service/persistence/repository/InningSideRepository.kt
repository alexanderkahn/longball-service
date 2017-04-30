package net.alexanderkahn.longball.service.persistence.repository

import net.alexanderkahn.base.servicebase.service.UserContext
import net.alexanderkahn.longball.service.persistence.model.EmbeddableUser
import net.alexanderkahn.longball.service.persistence.model.entity.PxInning
import net.alexanderkahn.longball.service.persistence.model.entity.PxInningSide

interface InningSideRepository : LongballRepository<PxInningSide> {
    fun  findFirstByInningAndOwnerOrderByIdDesc(inning: PxInning, owner: EmbeddableUser = UserContext.getPersistenceUser()): PxInningSide?
    fun findByInningAndOwner(inning: PxInning, owner: EmbeddableUser = UserContext.getPersistenceUser()): List<PxInningSide>
}