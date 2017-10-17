package net.alexanderkahn.longball.provider.assembler

import net.alexanderkahn.longball.model.League
import net.alexanderkahn.longball.model.Person
import net.alexanderkahn.longball.provider.entity.EmbeddableUser
import net.alexanderkahn.longball.provider.entity.PxLeague
import net.alexanderkahn.longball.provider.entity.PxPerson
import net.alexanderkahn.service.base.api.security.UserContext

val UserContext.Companion.pxUser: EmbeddableUser
    get() = UserContext.currentUser.let { EmbeddableUser(it.issuer, it.userId) }

fun League.toPersistence(): PxLeague {
    return PxLeague(name)
}

fun Person.toPersistence(): PxPerson {
    return PxPerson(first, last)
}