package net.alexanderkahn.longball.provider.assembler

import net.alexanderkahn.longball.model.request.RequestLeague
import net.alexanderkahn.longball.provider.entity.EmbeddableUser
import net.alexanderkahn.longball.provider.entity.PxLeague
import net.alexanderkahn.service.base.api.security.UserContext

val UserContext.Companion.pxUser: EmbeddableUser
    get() = UserContext.currentUser.let { EmbeddableUser(it.issuer, it.userId) }

fun RequestLeague.toPersistence(): PxLeague {
    return PxLeague(name)
}