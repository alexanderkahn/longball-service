package net.alexanderkahn.longball.api.exception

import net.alexanderkahn.service.commons.model.response.body.meta.ResourceStatus
import java.util.*

class BadRequestException : ResourceStatusException {
    constructor() : super()
    constructor(message: String) : super(message)

    override val status: ResourceStatus = ResourceStatus.BAD_REQUEST
}

class InvalidStateException : ResourceStatusException {
    constructor() : super()
    constructor(message: String) : super(message)

    override val status: ResourceStatus = ResourceStatus.INTERNAL_SERVER_ERROR

}

class NotFoundException : ResourceStatusException {
    constructor() : super()
    constructor(message: String) : super(message)
    constructor(type: String, id: UUID) : this("No object found of type $type with ID $id")

    override val status: ResourceStatus = ResourceStatus.NOT_FOUND

}

class NotImplementedException : ResourceStatusException {
    constructor() : super()
    constructor(message: String) : super(message)

    override val status: ResourceStatus = ResourceStatus.NOT_IMPLEMENTED

}

class UnauthenticatedException : ResourceStatusException {
    constructor() : super()
    constructor(message: String) : super(message)

    override val status: ResourceStatus = ResourceStatus.UNAUTHORIZED
}

class ConflictException: ResourceStatusException {
    constructor() : super()
    constructor(message: String) : super(message)

    override val status: ResourceStatus = ResourceStatus.CONFLICT
}

abstract class ResourceStatusException : RuntimeException {
    constructor() : super()
    constructor(message: String) : super(message)

    abstract val status: ResourceStatus
}

fun Exception.toResponse(): ResourceStatusException {
    return this as? ResourceStatusException ?: InvalidStateException(this.message.orEmpty())
}