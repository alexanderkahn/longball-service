package net.alexanderkahn.longball.api.model

import net.alexanderkahn.service.commons.model.request.body.RequestResourceObject
import net.alexanderkahn.service.commons.model.response.body.data.RelationshipObject
import net.alexanderkahn.service.commons.model.response.body.data.ResourceObject
import net.alexanderkahn.service.commons.model.response.body.meta.ModifiableResourceMeta
import java.time.LocalDate
import java.util.*

data class RequestRosterPosition(
        override val type: String,
        override val attributes: RosterPositionAttributes,
        override val relationships: RosterPositionRelationships) : RequestResourceObject {

    //FIXME reimplement validation
//    override fun validate() {
//        assertType(ModelTypes.ROSTER_POSITIONS)
//        relationships.player.data.assertType(ModelTypes.PEOPLE)
//    }
}

data class ResponseRosterPosition(
        override val id: UUID,
        override val meta: ModifiableResourceMeta,
        override val attributes: RosterPositionAttributes,
        override val relationships: RosterPositionRelationships) : ResourceObject {
    override val type = ModelTypes.ROSTER_POSITIONS.display
}

data class RosterPositionAttributes(val jerseyNumber: Int, val startDate: LocalDate, val endDate: LocalDate? = null)

data class RosterPositionRelationships(val team: RelationshipObject, val player: RelationshipObject) {
    constructor(teamId: UUID, playerId: UUID): this(
            RelationshipObject("teams", teamId),
            RelationshipObject("people", playerId))
}