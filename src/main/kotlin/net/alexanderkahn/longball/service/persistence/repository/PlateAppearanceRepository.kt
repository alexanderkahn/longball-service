package net.alexanderkahn.longball.service.persistence.repository

import net.alexanderkahn.base.servicebase.service.UserContext
import net.alexanderkahn.longball.service.persistence.model.EmbeddableUser
import net.alexanderkahn.longball.service.persistence.model.entity.PxInningHalf
import net.alexanderkahn.longball.service.persistence.model.entity.PxPlateAppearance

interface PlateAppearanceRepository: LongballRepository<PxPlateAppearance> {
    fun findFirstByInningHalfAndOwnerOrderByIdDesc(inningHalf: PxInningHalf, owner: EmbeddableUser = UserContext.getPersistenceUser()): PxPlateAppearance?
    fun findByInningHalfAndOwner(inningHalf: PxInningHalf, owner: EmbeddableUser = UserContext.getPersistenceUser()): MutableList<PxPlateAppearance>
}