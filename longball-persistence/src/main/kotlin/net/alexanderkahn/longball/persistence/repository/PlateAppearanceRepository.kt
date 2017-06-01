package net.alexanderkahn.longball.persistence.repository

import net.alexanderkahn.longball.persistence.EmbeddableUser
import net.alexanderkahn.longball.persistence.model.PxInningSide
import net.alexanderkahn.longball.persistence.model.PxPlateAppearance

interface PlateAppearanceRepository: LongballRepository<PxPlateAppearance> {
    fun findFirstBySideAndOwnerOrderByIdDesc(side: PxInningSide, owner: EmbeddableUser): PxPlateAppearance?
    fun findBySideAndOwner(side: PxInningSide, owner: EmbeddableUser): MutableList<PxPlateAppearance>
}