package net.alexanderkahn.longball.service.persistence.assembler

import net.alexanderkahn.base.servicebase.model.User
import net.alexanderkahn.longball.service.persistence.model.EmbeddableUser

fun User.toPersistence(): EmbeddableUser {
    return EmbeddableUser(issuer, userId)
}