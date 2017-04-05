package net.alexanderkahn.longball.service.persistence.repository

import net.alexanderkahn.base.servicebase.service.UserContext
import net.alexanderkahn.longball.service.service.assembler.toPersistence
import net.alexanderkahn.longball.service.persistence.model.EmbeddableUser

fun UserContext.Companion.getPersistenceUser(): EmbeddableUser {
    return UserContext.getCurrentUser().toPersistence()
}

