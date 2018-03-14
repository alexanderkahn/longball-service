package net.alexanderkahn.longball.model

import net.alexanderkahn.service.commons.model.request.body.RequestResourceObject
import net.alexanderkahn.service.commons.model.request.validation.ExpectedType
import net.alexanderkahn.service.commons.model.response.body.data.ModifiableResourceObject
import net.alexanderkahn.service.commons.model.response.body.data.RelationshipObject
import net.alexanderkahn.service.commons.model.response.body.meta.ModifiableResourceMeta
import java.time.LocalDate
import java.util.*
import javax.validation.Valid
import javax.validation.constraints.Max
import javax.validation.constraints.Min

data class RequestRosterPosition(
        @ExpectedType(ModelTypes.ROSTER_POSITIONS) override val type: String,
        @field:Valid override val attributes: RosterPositionAttributes,
        @field:Valid override val relationships: RosterPositionRelationships) : RequestResourceObject

data class ResponseRosterPosition(
        override val id: UUID,
        override val meta: ModifiableResourceMeta,
        override val attributes: RosterPositionAttributes,
        override val relationships: RosterPositionRelationships) : ModifiableResourceObject {
    override val type = ModelTypes.ROSTER_POSITIONS
}

data class RosterPositionAttributes(
        @field:Min(0) @field:Max(99) val jerseyNumber: Int,
        val startDate: LocalDate,
        val endDate: LocalDate? = null)

data class RosterPositionRelationships(
        @ExpectedType(ModelTypes.TEAMS) val team: RelationshipObject,
        @ExpectedType(ModelTypes.PEOPLE) val player: RelationshipObject
) {
    constructor(teamId: UUID, playerId: UUID): this(
            RelationshipObject(ModelTypes.TEAMS, teamId),
            RelationshipObject(ModelTypes.PEOPLE, playerId))
}