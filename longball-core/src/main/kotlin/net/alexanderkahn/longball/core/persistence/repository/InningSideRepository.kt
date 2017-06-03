package net.alexanderkahn.longball.core.persistence.repository

import net.alexanderkahn.longball.core.persistence.EmbeddableUser
import net.alexanderkahn.longball.core.persistence.model.PxInning
import net.alexanderkahn.longball.core.persistence.model.PxInningSide

interface InningSideRepository: LongballRepository<PxInningSide> {
    fun findByInningAndOwner(inning: PxInning, owner: EmbeddableUser): List<PxInningSide>
    fun findByInningAndSideAndOwner(inning: PxInning, side: Int, owner: EmbeddableUser): PxInningSide?
}