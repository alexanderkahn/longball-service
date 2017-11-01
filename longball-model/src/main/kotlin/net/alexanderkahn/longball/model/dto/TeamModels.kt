package net.alexanderkahn.longball.model.dto

import net.alexanderkahn.service.base.model.request.RequestResourceObject
import net.alexanderkahn.service.base.model.response.body.data.ResourceIdentifier
import net.alexanderkahn.service.base.model.response.body.data.ResourceObjectRelationship
import net.alexanderkahn.service.base.model.response.body.data.ResponseResourceObject
import net.alexanderkahn.service.base.model.response.body.meta.ModifiableResourceMeta
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
        override val meta: ModifiableResourceMeta,
        override val attributes: TeamAttributes,
        override val relationships: TeamRelationships) : ResponseResourceObject {
    override val type = ModelTypes.TEAMS.display
}

data class TeamAttributes(val abbreviation: String, val location: String, val nickname: String)

data class TeamRelationships(val league: ResourceObjectRelationship) {
    constructor(leagueId: UUID): this(ResourceObjectRelationship(ResourceIdentifier(ModelTypes.LEAGUES.display, leagueId)))
}