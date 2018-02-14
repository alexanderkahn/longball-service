package net.alexanderkahn.longball.model.dto

import net.alexanderkahn.service.commons.model.request.RequestResourceObject
import net.alexanderkahn.service.commons.model.response.body.data.ResponseResourceObject
import net.alexanderkahn.service.commons.model.response.body.meta.ModifiableResourceMeta
import java.util.*

data class RequestPerson(
        override val type: String,
        override val attributes: PersonAttributes) : RequestResourceObject {
    override val relationships = null

    override fun validate() {
        assertType(ModelTypes.PEOPLE)
    }
}

data class ResponsePerson(
        override val id: UUID,
        override val meta: ModifiableResourceMeta,
        override val attributes: PersonAttributes) : ResponseResourceObject {
    override val relationships: Nothing? = null
    override val type = ModelTypes.PEOPLE.display
}

data class PersonAttributes(val first: String, val last: String)
