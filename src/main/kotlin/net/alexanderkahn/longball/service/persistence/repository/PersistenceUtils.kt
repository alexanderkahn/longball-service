package net.alexanderkahn.longball.service.persistence.repository

import net.alexanderkahn.base.servicebase.service.UserContext
import net.alexanderkahn.longball.service.persistence.EmbeddableUser
import net.alexanderkahn.longball.service.service.assembler.toPersistence

fun UserContext.Companion.getPersistenceUser(): EmbeddableUser {
    return UserContext.getCurrentUser().toPersistence()
}

