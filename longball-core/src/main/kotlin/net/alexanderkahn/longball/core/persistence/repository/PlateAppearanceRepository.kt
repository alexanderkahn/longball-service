package net.alexanderkahn.longball.core.persistence.repository

import net.alexanderkahn.longball.core.persistence.EmbeddableUser
import net.alexanderkahn.longball.core.persistence.model.PxInningSide
import net.alexanderkahn.longball.core.persistence.model.PxPlateAppearance

interface PlateAppearanceRepository: LongballRepository<PxPlateAppearance> {
    fun findFirstBySideAndOwnerOrderByIdDesc(side: PxInningSide, owner: EmbeddableUser): PxPlateAppearance?
    fun findBySideAndOwner(side: PxInningSide, owner: EmbeddableUser): MutableList<PxPlateAppearance>
}