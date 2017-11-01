package net.alexanderkahn.longball.presentation.rest.model

import net.alexanderkahn.longball.model.dto.LeagueDTO
import net.alexanderkahn.service.base.api.exception.InvalidStateException
import net.alexanderkahn.service.base.presentation.request.RequestResourceObject
import net.alexanderkahn.service.base.presentation.response.body.data.ResponseResourceObject
import net.alexanderkahn.service.base.presentation.response.body.meta.ModifiableResourceMeta
import java.time.OffsetDateTime
import java.util.*

data class RequestLeague(override val type: String, override val attributes: LeagueAttributes) : RequestResourceObject {
    override val relationships = null
    override fun validate() {
        assertType(ModelTypes.LEAGUES)
    }
}

data class ResponseLeague(override val id: UUID, override val meta: ModifiableResourceMeta, override val attributes: LeagueAttributes) : ResponseResourceObject {
    override val type = ModelTypes.LEAGUES.display
    override val relationships = null
}

data class LeagueAttributes(val name: String)

fun LeagueDTO.toResponse(): ResponseLeague {
    val leagueId = id ?: throw InvalidStateException("Response must contain a valid ID")
    return ResponseLeague(leagueId, ModifiableResourceMeta(created, lastModified), LeagueAttributes(name))
}

fun RequestLeague.toDto(): LeagueDTO {
    return LeagueDTO(null, OffsetDateTime.now(), OffsetDateTime.now(), this.attributes.name)
}