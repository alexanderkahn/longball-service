package net.alexanderkahn.longball.provider.persistence.repository

import net.alexanderkahn.longball.provider.persistence.EmbeddableUser
import net.alexanderkahn.longball.provider.persistence.model.PxInning
import net.alexanderkahn.longball.provider.persistence.model.PxInningSide
import net.alexanderkahn.longball.model.Side

interface InningSideRepository: LongballRepository<PxInningSide> {
    fun findByInningAndOwner(inning: PxInning, owner: EmbeddableUser): List<PxInningSide>
    fun findByInningAndSideAndOwner(inning: PxInning, side: Side, owner: EmbeddableUser): PxInningSide?
}