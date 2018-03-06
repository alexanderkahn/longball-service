package net.alexanderkahn.longball.model.dto

import net.alexanderkahn.service.commons.model.request.body.RequestResourceObject
import net.alexanderkahn.service.commons.model.response.body.data.RelationshipObject
import net.alexanderkahn.service.commons.model.response.body.data.ResourceObject
import net.alexanderkahn.service.commons.model.response.body.meta.ModifiableResourceMeta
import java.util.*

data class RequestTeam(
        override val type: String,
        override val attributes: TeamAttributes,
        override val relationships: TeamRelationships) : RequestResourceObject {

    //FIXME reimplement validation
//    override fun validate() {
//        assertType(ModelTypes.TEAMS)
//        relationships.league.data.assertType(ModelTypes.LEAGUES)
//    }
}

data class ResponseTeam(
        override val id: UUID,
        override val meta: ModifiableResourceMeta,
        override val attributes: TeamAttributes,
        override val relationships: TeamRelationships) : ResourceObject {
    override val type = ModelTypes.TEAMS.display
}

data class TeamAttributes(val abbreviation: String, val location: String, val nickname: String)

data class TeamRelationships(val league: RelationshipObject) {
    constructor(leagueId: UUID): this(RelationshipObject(ModelTypes.LEAGUES.display, leagueId))
}