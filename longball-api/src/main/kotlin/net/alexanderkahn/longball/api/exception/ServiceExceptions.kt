package net.alexanderkahn.longball.api.exception

import net.alexanderkahn.service.commons.model.response.body.data.RelationshipObject
import java.util.*

class BadRequestException(message: String) : RuntimeException(message)

class InvalidStateException(message: String) : RuntimeException(message)

class ResourceNotFoundException : RuntimeException {
    constructor(message: String) : super(message)
    constructor(type: String, id: UUID) : this("No object found of type $type with ID $id")
}

class NotImplementedException(message: String) : RuntimeException(message)

class UnauthenticatedException(message: String) : RuntimeException(message)

class ConflictException(message: String) : RuntimeException(message)

class InvalidRelationshipsException(val invalidIdentifiers: List<RelationshipObject.RelationshipObjectIdentifier>)
    : RuntimeException("Related entities not found: $invalidIdentifiers")