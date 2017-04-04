package net.alexanderkahn.longball.service.persistence.assembler

import net.alexanderkahn.base.servicebase.service.UserContext
import net.alexanderkahn.longball.service.model.GameplayEvent
import net.alexanderkahn.longball.service.persistence.model.PersistenceGameplayEvent
import net.alexanderkahn.longball.service.persistence.model.PersistencePlateAppearance
import net.alexanderkahn.longball.service.persistence.repository.getPersistenceUser

fun GameplayEvent.toPersistence(id: Long?, appearance: PersistencePlateAppearance): PersistenceGameplayEvent {
    return PersistenceGameplayEvent(id, UserContext.getPersistenceUser(), appearance, pitch)
}