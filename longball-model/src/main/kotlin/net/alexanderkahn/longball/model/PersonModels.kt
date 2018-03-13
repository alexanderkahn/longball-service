package net.alexanderkahn.longball.model

import net.alexanderkahn.service.commons.model.request.body.RequestResourceObject
import net.alexanderkahn.service.commons.model.request.validation.ExpectedType
import net.alexanderkahn.service.commons.model.response.body.data.ModifiableResourceObject
import net.alexanderkahn.service.commons.model.response.body.meta.ModifiableResourceMeta
import java.util.*

data class RequestPerson(
        @ExpectedType(ModelTypes.PEOPLE) override val type: String,
        override val attributes: PersonAttributes) : RequestResourceObject {
    override val relationships: Nothing? = null
}

data class ResponsePerson(
        override val id: UUID,
        override val meta: ModifiableResourceMeta,
        override val attributes: PersonAttributes) : ModifiableResourceObject {
    override val relationships: Nothing? = null
    override val type = ModelTypes.PEOPLE
}

data class PersonAttributes(val first: String, val last: String)
