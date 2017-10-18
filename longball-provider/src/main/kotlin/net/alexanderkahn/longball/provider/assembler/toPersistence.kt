package net.alexanderkahn.longball.provider.assembler

import net.alexanderkahn.longball.model.dto.LeagueDTO
import net.alexanderkahn.longball.model.dto.PersonDTO
import net.alexanderkahn.longball.provider.entity.EmbeddableUser
import net.alexanderkahn.longball.provider.entity.LeagueEntity
import net.alexanderkahn.longball.provider.entity.PersonEntity
import net.alexanderkahn.service.base.api.security.UserContext

val UserContext.Companion.embeddableUser: EmbeddableUser
    get() = UserContext.currentUser.let { EmbeddableUser(it.issuer, it.userId) }

fun LeagueDTO.toEntity(): LeagueEntity {
    return LeagueEntity(name)
}

fun PersonDTO.toEntity(): PersonEntity {
    return PersonEntity(first, last)
}