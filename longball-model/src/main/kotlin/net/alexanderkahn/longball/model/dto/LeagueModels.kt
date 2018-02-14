package net.alexanderkahn.longball.model.dto

import net.alexanderkahn.service.commons.model.exception.BadRequestException
import net.alexanderkahn.service.commons.model.request.RequestResourceObject
import net.alexanderkahn.service.commons.model.response.body.data.ResponseResourceObject
import net.alexanderkahn.service.commons.model.response.body.meta.ModifiableResourceMeta
import java.util.*

data class RequestLeague(override val type: String, override val attributes: LeagueAttributes) : RequestResourceObject {
    override val relationships = null
    override fun validate() {
        assertType(ModelTypes.LEAGUES)
        if (attributes.name.length < MIN_NAME_FIELD_SIZE || attributes.name.length > MAX_NAME_FIELD_SIZE) {
            throw BadRequestException(invalidFieldLengthMessage("name"))
        }
    }
}

data class ResponseLeague(override val id: UUID, override val meta: ModifiableResourceMeta, override val attributes: LeagueAttributes) : ResponseResourceObject {
    override val type = ModelTypes.LEAGUES.display
    override val relationships = null
}

data class LeagueAttributes(val name: String)