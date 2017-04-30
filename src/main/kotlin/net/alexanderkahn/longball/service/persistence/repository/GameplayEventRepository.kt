package net.alexanderkahn.longball.service.persistence.repository

import net.alexanderkahn.base.servicebase.service.UserContext
import net.alexanderkahn.longball.service.persistence.model.EmbeddableUser
import net.alexanderkahn.longball.service.persistence.model.entity.PxGameplayEvent
import net.alexanderkahn.longball.service.persistence.model.entity.PxPlateAppearance

interface GameplayEventRepository: LongballRepository<PxGameplayEvent> {
    fun findByPlateAppearanceAndOwner(appearance: PxPlateAppearance, owner: EmbeddableUser = UserContext.getPersistenceUser()): List<PxGameplayEvent>
}