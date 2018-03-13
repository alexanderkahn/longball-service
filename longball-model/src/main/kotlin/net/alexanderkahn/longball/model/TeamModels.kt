package net.alexanderkahn.longball.model

import net.alexanderkahn.service.commons.model.request.body.RequestResourceObject
import net.alexanderkahn.service.commons.model.request.validation.ExpectedType
import net.alexanderkahn.service.commons.model.response.body.data.ModifiableResourceObject
import net.alexanderkahn.service.commons.model.response.body.data.RelationshipObject
import net.alexanderkahn.service.commons.model.response.body.meta.ModifiableResourceMeta
import java.util.*
import javax.validation.Valid

data class RequestTeam(
        @field:ExpectedType(ModelTypes.TEAMS) override val type: String,
        @field:Valid override val attributes: TeamAttributes,
        @field:Valid override val relationships: TeamRelationships) : RequestResourceObject {
}

data class ResponseTeam(
        override val id: UUID,
        override val meta: ModifiableResourceMeta,
        override val attributes: TeamAttributes,
        override val relationships: TeamRelationships) : ModifiableResourceObject {
    override val type = ModelTypes.TEAMS
}

//TODO: figure out what validation is needed for these
data class TeamAttributes(val abbreviation: String, val location: String, val nickname: String)

data class TeamRelationships(@ExpectedType(ModelTypes.LEAGUES) val league: RelationshipObject) {
    constructor(leagueId: UUID): this(RelationshipObject(ModelTypes.LEAGUES, leagueId))
}