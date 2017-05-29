package net.alexanderkahn.longball.service.persistence.repository

import net.alexanderkahn.longball.service.persistence.EmbeddableUser
import net.alexanderkahn.longball.service.service.assembler.toPersistence
import net.alexanderkahn.servicebase.provider.security.UserContext

fun UserContext.Companion.getPersistenceUser(): EmbeddableUser {
    return UserContext.getCurrentUser().toPersistence()
}

