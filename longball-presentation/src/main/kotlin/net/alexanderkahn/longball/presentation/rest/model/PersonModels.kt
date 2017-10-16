package net.alexanderkahn.longball.presentation.rest.model

import net.alexanderkahn.longball.model.Person
import net.alexanderkahn.service.base.presentation.request.RequestResourceObject
import net.alexanderkahn.service.base.presentation.response.body.data.ResponseResourceObject
import sun.plugin.dom.exception.InvalidStateException
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
        override val attributes: PersonAttributes) : ResponseResourceObject {
    override val relationships = null
    override val type = ModelTypes.TEAMS.display
}

data class PersonAttributes(val first: String, val last: String)

fun RequestPerson.toModel(): Person {
    return Person(null, attributes.first, attributes.last)
}

fun Person.toResponse(): ResponsePerson {
    val personId = id ?: throw InvalidStateException("No id found for person") //TODO extend with UnsavedObjectException or something
    return ResponsePerson(personId, PersonAttributes(first, last))
}
