package net.alexanderkahn.longball.model.dto


import net.alexanderkahn.service.base.model.request.RequestResourceObject
import net.alexanderkahn.service.base.model.response.body.data.ResourceIdentifier
import net.alexanderkahn.service.base.model.response.body.data.ResourceObjectRelationship
import net.alexanderkahn.service.base.model.response.body.data.ResponseResourceObject
import net.alexanderkahn.service.base.model.response.body.meta.ModifiableResourceMeta
import java.time.LocalDate
import java.util.*

data class RequestRosterPosition(
        override val type: String,
        override val attributes: RosterPositionAttributes,
        override val relationships: RosterPositionRelationships) : RequestResourceObject {


    override fun validate() {
        assertType(ModelTypes.ROSTER_POSITIONS)
        relationships.player.data.assertType(ModelTypes.PEOPLE)
    }
}

data class ResponseRosterPosition(
        override val id: UUID,
        override val meta: ModifiableResourceMeta,
        override val attributes: RosterPositionAttributes,
        override val relationships: RosterPositionRelationships) : ResponseResourceObject {
    override val type = ModelTypes.ROSTER_POSITIONS.display
}

data class RosterPositionAttributes(val jerseyNumber: Int, val startDate: LocalDate, val endDate: LocalDate? = null)

data class RosterPositionRelationships(val team: ResourceObjectRelationship, val player: ResourceObjectRelationship) {
    constructor(teamId: UUID, playerId: UUID): this(
            ResourceObjectRelationship(ResourceIdentifier("teams", teamId)),
            ResourceObjectRelationship(ResourceIdentifier("people", playerId)))
}