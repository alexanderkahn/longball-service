package net.alexanderkahn.longball.model

import net.alexanderkahn.service.commons.model.request.body.RequestResourceObject
import net.alexanderkahn.service.commons.model.request.validation.ExpectedType
import net.alexanderkahn.service.commons.model.response.body.data.ModifiableResourceObject
import net.alexanderkahn.service.commons.model.response.body.meta.ModifiableResourceMeta
import java.util.*
import javax.validation.Valid
import javax.validation.constraints.Size


data class RequestLeague(
        @field:ExpectedType(ModelTypes.LEAGUES) override val type: String,
        @field:Valid override val attributes: LeagueAttributes
) : RequestResourceObject {
    override val relationships: Nothing? = null
}

data class ResponseLeague(
        override val id: UUID,
        override val meta: ModifiableResourceMeta,
        override val attributes: LeagueAttributes
) : ModifiableResourceObject {
    override val type = ModelTypes.LEAGUES
    override val relationships: Nothing? = null
}

data class LeagueAttributes(@field:Size(min = MIN_NAME_FIELD_SIZE, max = MAX_NAME_FIELD_SIZE) val name: String)