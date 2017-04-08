package net.alexanderkahn.longball.service.service.assembler

import net.alexanderkahn.base.servicebase.service.UserContext
import net.alexanderkahn.longball.service.model.GameplayEvent
import net.alexanderkahn.longball.service.persistence.model.entity.PxGameplayEvent
import net.alexanderkahn.longball.service.persistence.model.entity.PxPlateAppearance
import net.alexanderkahn.longball.service.persistence.repository.getPersistenceUser

fun GameplayEvent.toPersistence(id: Long?, appearance: PxPlateAppearance): PxGameplayEvent {
    return PxGameplayEvent(id, appearance, pitch, UserContext.getPersistenceUser())
}