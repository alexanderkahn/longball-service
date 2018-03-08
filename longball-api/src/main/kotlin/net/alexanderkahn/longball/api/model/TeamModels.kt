package net.alexanderkahn.longball.api.model

import net.alexanderkahn.service.commons.model.request.body.RequestResourceObject
import net.alexanderkahn.service.commons.model.request.validation.ExpectedType
import net.alexanderkahn.service.commons.model.response.body.data.RelationshipObject
import net.alexanderkahn.service.commons.model.response.body.data.ResourceObject
import net.alexanderkahn.service.commons.model.response.body.meta.ModifiableResourceMeta
import java.util.*
import javax.validation.Valid

data class RequestTeam(
        @field:ExpectedType("teams") override val type: String,
        @field:Valid override val attributes: TeamAttributes,
        @field:Valid override val relationships: TeamRelationships) : RequestResourceObject {
}

data class ResponseTeam(
        override val id: UUID,
        override val meta: ModifiableResourceMeta,
        override val attributes: TeamAttributes,
        override val relationships: TeamRelationships) : ResourceObject {
    override val type = ModelTypes.TEAMS.display
}

//TODO: figure out what validation is needed for these
data class TeamAttributes(val abbreviation: String, val location: String, val nickname: String)

//TODO: validate relationships exist
data class TeamRelationships(@ExpectedType("leagues") val league: RelationshipObject) {
    constructor(leagueId: UUID): this(RelationshipObject(ModelTypes.LEAGUES.display, leagueId))
}