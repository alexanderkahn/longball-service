package net.alexanderkahn.longball.persistence.repository

import net.alexanderkahn.longball.persistence.EmbeddableUser
import net.alexanderkahn.longball.persistence.model.PxInning
import net.alexanderkahn.longball.persistence.model.PxInningSide

interface InningSideRepository: LongballRepository<PxInningSide> {
    fun findByInningAndOwner(inning: PxInning, owner: EmbeddableUser): List<PxInningSide>
    fun findByInningAndSideAndOwner(inning: PxInning, side: Int, owner: EmbeddableUser): PxInningSide?
}