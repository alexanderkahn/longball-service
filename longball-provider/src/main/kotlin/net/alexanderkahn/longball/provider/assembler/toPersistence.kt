package net.alexanderkahn.longball.provider.assembler

import net.alexanderkahn.longball.provider.entity.EmbeddableUser
import net.alexanderkahn.service.base.api.security.UserContext

val UserContext.Companion.pxUser: EmbeddableUser
    get() = UserContext.currentUser.let { EmbeddableUser(it.issuer, it.userId) }