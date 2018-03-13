package net.alexanderkahn.longball.api.exception

import net.alexanderkahn.service.commons.model.response.body.data.RelationshipObject
import java.util.*

class BadRequestException(message: String) : RuntimeException(message)

class InvalidStateException(message: String) : RuntimeException(message)

class ResourceNotFoundException(val missingResource: RelationshipObject.RelationshipObjectIdentifier) : RuntimeException() {
    constructor(type: String, id: UUID) : this(RelationshipObject.RelationshipObjectIdentifier(type, id))
    override val message: String = "No entity of type ${missingResource.type} found with ID ${missingResource.id}"
}

class NotImplementedException(message: String) : RuntimeException(message)

class UnauthenticatedException(message: String) : RuntimeException(message)

class ConflictException(message: String) : RuntimeException(message)

class InvalidRelationshipsException(val causes: List<ResourceNotFoundException>)
    : RuntimeException("Related entities not found: ${causes.map { it.message }}") {
}