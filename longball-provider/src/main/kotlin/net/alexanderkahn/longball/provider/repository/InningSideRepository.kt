package net.alexanderkahn.longball.provider.repository

import net.alexanderkahn.longball.provider.entity.EmbeddableUser
import net.alexanderkahn.longball.provider.entity.PxInning
import net.alexanderkahn.longball.provider.entity.PxInningSide
import net.alexanderkahn.longball.model.Side

interface InningSideRepository: LongballRepository<PxInningSide> {
    fun findByInningAndOwner(inning: PxInning, owner: EmbeddableUser): List<PxInningSide>
    fun findByInningAndSideAndOwner(inning: PxInning, side: Side, owner: EmbeddableUser): PxInningSide?
}