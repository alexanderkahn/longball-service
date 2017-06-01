package net.alexanderkahn.longball.core.assembler

import net.alexanderkahn.longball.persistence.EmbeddableUser
import net.alexanderkahn.servicebase.core.security.UserContext

val UserContext.Companion.pxUser: EmbeddableUser
    get() = UserContext.currentUser.let { EmbeddableUser(it.issuer, it.userId) }