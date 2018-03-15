package net.alexanderkahn.longball.core.assembler

import net.alexanderkahn.longball.model.*
import net.alexanderkahn.longball.core.entity.*
import net.alexanderkahn.service.commons.model.response.body.meta.ModifiableResourceMeta


fun LeagueEntity.toResponse(): ResponseLeague {
    val attributes = LeagueAttributes(name)
    return ResponseLeague(id, toMeta(), attributes)
}

fun PersonEntity.toResponse(): ResponsePerson {
    val attributes = PersonAttributes(first, last)
    return ResponsePerson(id, toMeta(), attributes)
}

fun RosterPositionEntity.toResponse(): ResponseRosterPosition {
    val attributes = RosterPositionAttributes(jerseyNumber, startDate.fromPersistence(), endDate?.fromPersistence())
    val relationships = RosterPositionRelationships(team.id, player.id)
    return ResponseRosterPosition(id, toMeta(), attributes, relationships)
}

fun TeamEntity.toResponse(): ResponseTeam {
    val attributes = TeamAttributes(abbreviation, location, nickname)
    val relationships = TeamRelationships(league.id)
    return ResponseTeam(id, toMeta(), attributes, relationships)
}

fun OwnedEntity.toMeta(): ModifiableResourceMeta {
    return ModifiableResourceMeta(created, lastModified)
}