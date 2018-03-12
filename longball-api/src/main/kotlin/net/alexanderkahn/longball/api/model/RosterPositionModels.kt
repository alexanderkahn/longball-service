package net.alexanderkahn.longball.api.model

import net.alexanderkahn.service.commons.model.request.body.RequestResourceObject
import net.alexanderkahn.service.commons.model.request.validation.ExpectedType
import net.alexanderkahn.service.commons.model.response.body.data.RelationshipObject
import net.alexanderkahn.service.commons.model.response.body.data.ResourceObject
import net.alexanderkahn.service.commons.model.response.body.meta.ModifiableResourceMeta
import java.time.LocalDate
import java.util.*
import javax.validation.Valid

data class RequestRosterPosition(
        @ExpectedType("rosterpositions") override val type: String,
        override val attributes: RosterPositionAttributes,
        @field:Valid override val relationships: RosterPositionRelationships) : RequestResourceObject {
}

data class ResponseRosterPosition(
        override val id: UUID,
        override val meta: ModifiableResourceMeta,
        override val attributes: RosterPositionAttributes,
        override val relationships: RosterPositionRelationships) : ResourceObject {
    override val type = ModelTypes.ROSTER_POSITIONS.display
}

data class RosterPositionAttributes(val jerseyNumber: Int, val startDate: LocalDate, val endDate: LocalDate? = null)

data class RosterPositionRelationships(
        @ExpectedType("teams") val team: RelationshipObject,
        @ExpectedType("people") val player: RelationshipObject
) {
    constructor(teamId: UUID, playerId: UUID): this(
            RelationshipObject("teams", teamId),
            RelationshipObject("people", playerId))
}