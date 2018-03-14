package net.alexanderkahn.longball.model

import net.alexanderkahn.service.commons.model.request.body.RequestResourceObject
import net.alexanderkahn.service.commons.model.request.validation.ExpectedType
import net.alexanderkahn.service.commons.model.response.body.data.ModifiableResourceObject
import net.alexanderkahn.service.commons.model.response.body.meta.ModifiableResourceMeta
import java.util.*
import javax.validation.Valid
import javax.validation.constraints.Size

data class RequestPerson(
        @ExpectedType(ModelTypes.PEOPLE) override val type: String,
        @field:Valid override val attributes: PersonAttributes
) : RequestResourceObject {
    override val relationships: Nothing? = null
}

data class ResponsePerson(
        override val id: UUID,
        override val meta: ModifiableResourceMeta,
        override val attributes: PersonAttributes) : ModifiableResourceObject {
    override val relationships: Nothing? = null
    override val type = ModelTypes.PEOPLE
}

data class PersonAttributes(
        @field:Size(min = 3, max = 25) val first: String,
        @field:Size(min = 3, max = 25) val last: String
)
