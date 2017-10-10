package net.alexanderkahn.longball.presentation.rest.model

import net.alexanderkahn.longball.model.League
import net.alexanderkahn.service.base.api.exception.InvalidStateException
import net.alexanderkahn.service.base.presentation.request.RequestResourceObject
import net.alexanderkahn.service.base.presentation.response.body.data.ResponseResourceObject
import java.util.*

data class RequestLeague(override val type: String, override val attributes: LeagueAttributes) : RequestResourceObject {
    override val relationships = null
}

data class ResponseLeague(override val id: UUID, override val attributes: LeagueAttributes) :ResponseResourceObject {
    override val type = ModelTypes.LEAGUES.display
    override val relationships = null
}

data class LeagueAttributes(val name: String)

fun League.toResponse(): ResponseLeague {
    val leagueId = id ?: throw InvalidStateException("Response must contain a valid ID")
    return ResponseLeague(leagueId, LeagueAttributes(name))
}

fun RequestLeague.toDto(): League {
    return League(null, this.attributes.name)
}