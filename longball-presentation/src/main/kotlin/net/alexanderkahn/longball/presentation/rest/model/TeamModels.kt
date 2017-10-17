package net.alexanderkahn.longball.presentation.rest.model

import net.alexanderkahn.longball.model.TeamDTO
import net.alexanderkahn.service.base.api.exception.InvalidStateException
import net.alexanderkahn.service.base.presentation.request.RequestResourceObject
import net.alexanderkahn.service.base.presentation.response.body.data.ResourceIdentifier
import net.alexanderkahn.service.base.presentation.response.body.data.ResourceObjectRelationship
import net.alexanderkahn.service.base.presentation.response.body.data.ResponseResourceObject
import java.util.*

data class RequestTeam(
        override val type: String,
        override val attributes: TeamAttributes,
        override val relationships: TeamRelationships) : RequestResourceObject {
    override fun validate() {
        assertType(ModelTypes.TEAMS)
        relationships.league.data.assertType(ModelTypes.LEAGUES)
    }
}

data class ResponseTeam(
        override val id: UUID,
        override val attributes: TeamAttributes,
        override val relationships: TeamRelationships) : ResponseResourceObject {
    override val type = ModelTypes.TEAMS.display
}

data class TeamAttributes(val abbreviation: String, val location: String, val nickname: String)

data class TeamRelationships(val league: ResourceObjectRelationship) {
    constructor(leagueId: UUID): this(ResourceObjectRelationship(ResourceIdentifier(ModelTypes.LEAGUES.display, leagueId)))
}

fun TeamDTO.toResponse(): ResponseTeam {
    val teamId = id ?: throw InvalidStateException("Response must contain a valid ID")
    val attributes = TeamAttributes(abbreviation, location, nickname)
    val relationships = TeamRelationships(league)
    return ResponseTeam(teamId, attributes, relationships)
}

fun RequestTeam.toDto(): TeamDTO {
    return TeamDTO(null, relationships.league.data.id, attributes.abbreviation, attributes.location, attributes.nickname)
}