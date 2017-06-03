package net.alexanderkahn.longball.core.persistence.repository

import net.alexanderkahn.longball.core.persistence.EmbeddableUser
import net.alexanderkahn.longball.core.persistence.model.PxInning
import net.alexanderkahn.longball.core.persistence.model.PxInningSide
import net.alexanderkahn.longball.model.Side

interface InningSideRepository: LongballRepository<PxInningSide> {
    fun findByInningAndOwner(inning: PxInning, owner: EmbeddableUser): List<PxInningSide>
    fun findByInningAndSideAndOwner(inning: PxInning, side: Side, owner: EmbeddableUser): PxInningSide?
}