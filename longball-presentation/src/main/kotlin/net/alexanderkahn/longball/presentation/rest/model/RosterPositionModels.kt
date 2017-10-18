package net.alexanderkahn.longball.presentation.rest.model

import net.alexanderkahn.longball.model.dto.RosterPositionDTO
import net.alexanderkahn.service.base.api.exception.InvalidStateException
import net.alexanderkahn.service.base.presentation.request.RequestResourceObject
import net.alexanderkahn.service.base.presentation.response.body.data.ResourceIdentifier
import net.alexanderkahn.service.base.presentation.response.body.data.ResourceObjectRelationship
import net.alexanderkahn.service.base.presentation.response.body.data.ResponseResourceObject
import java.time.OffsetDateTime
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
        override val attributes: RosterPositionAttributes,
        override val relationships: RosterPositionRelationships) : ResponseResourceObject {
    override val type = ModelTypes.ROSTER_POSITIONS.display
}

data class RosterPositionAttributes(val jerseyNumber: Number, val startDate: OffsetDateTime, val endDate: OffsetDateTime?)

data class RosterPositionRelationships(val team: ResourceObjectRelationship, val player: ResourceObjectRelationship) {
    constructor(teamId: UUID, playerId: UUID): this(
            ResourceObjectRelationship(ResourceIdentifier("teams", teamId)),
            ResourceObjectRelationship(ResourceIdentifier("players", playerId)))
}

fun RequestRosterPosition.toDto(): RosterPositionDTO {
    return RosterPositionDTO(
            null,
            relationships.team.data.id,
            relationships.player.data.id,
            attributes.jerseyNumber,
            attributes.startDate,
            attributes.endDate)
}

fun RosterPositionDTO.toResponse(): ResponseRosterPosition {
    val positionId = id ?: throw InvalidStateException("No id found for roster position")
    return ResponseRosterPosition(positionId,
            RosterPositionAttributes(jerseyNumber, startDate, endDate),
            RosterPositionRelationships(team, player)
    )
}