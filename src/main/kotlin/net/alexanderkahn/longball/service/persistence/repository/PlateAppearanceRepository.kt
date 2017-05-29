package net.alexanderkahn.longball.service.persistence.repository

import net.alexanderkahn.servicebase.provider.security.UserContext
import net.alexanderkahn.longball.service.persistence.EmbeddableUser
import net.alexanderkahn.longball.service.persistence.entity.PxInningSide
import net.alexanderkahn.longball.service.persistence.entity.PxPlateAppearance

interface PlateAppearanceRepository: LongballRepository<PxPlateAppearance> {
    fun findFirstBySideAndOwnerOrderByIdDesc(side: PxInningSide, owner: EmbeddableUser = UserContext.getPersistenceUser()): PxPlateAppearance?
    fun findBySideAndOwner(side: PxInningSide, owner: EmbeddableUser = UserContext.getPersistenceUser()): MutableList<PxPlateAppearance>
}