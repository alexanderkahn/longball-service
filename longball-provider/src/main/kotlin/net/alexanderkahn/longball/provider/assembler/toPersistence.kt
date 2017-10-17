package net.alexanderkahn.longball.provider.assembler

import net.alexanderkahn.longball.model.League
import net.alexanderkahn.longball.model.Person
import net.alexanderkahn.longball.provider.entity.EmbeddableUser
import net.alexanderkahn.longball.provider.entity.LeagueEntity
import net.alexanderkahn.longball.provider.entity.PersonEntity
import net.alexanderkahn.service.base.api.security.UserContext

val UserContext.Companion.pxUser: EmbeddableUser
    get() = UserContext.currentUser.let { EmbeddableUser(it.issuer, it.userId) }

fun League.toPersistence(): LeagueEntity {
    return LeagueEntity(name)
}

fun Person.toPersistence(): PersonEntity {
    return PersonEntity(first, last)
}