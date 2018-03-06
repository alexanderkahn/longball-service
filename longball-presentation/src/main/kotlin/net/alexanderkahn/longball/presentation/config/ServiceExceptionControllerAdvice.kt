package net.alexanderkahn.longball.presentation.config


import net.alexanderkahn.longball.api.exception.*
import net.alexanderkahn.service.commons.model.response.body.ErrorsResponse
import net.alexanderkahn.service.commons.model.response.body.error.ResponseError
import net.alexanderkahn.service.commons.model.response.body.meta.ResourceStatus
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ServiceExceptionControllerAdvice {

    private val logger = LoggerFactory.getLogger(this::class.java)!!

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException::class, HttpMessageNotReadableException::class)
    fun handleBadRequest(e: ResourceStatusException): ErrorsResponse {
        return ErrorsResponse(ResponseError(ResourceStatus.BAD_REQUEST, "Bad Request", e.message.orEmpty()))
    }

    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    @ExceptionHandler(InvalidStateException::class, NotImplementedException::class)
    fun handleServerException(e: ResourceStatusException): ErrorsResponse {
        return ErrorsResponse(ResponseError(ResourceStatus.NOT_IMPLEMENTED, "Not Implemented", e.message.orEmpty()))

    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException::class)
    fun handleNotFound(e: ResourceStatusException): ErrorsResponse {
        return ErrorsResponse(ResponseError(ResourceStatus.NOT_FOUND, "Not Found", e.message.orEmpty()))
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthenticatedException::class)
    fun handleUnauthorized(e: ResourceStatusException): ErrorsResponse {
        return ErrorsResponse(ResponseError(ResourceStatus.UNAUTHORIZED, "Unauthorized", e.message.orEmpty()))
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ConflictException::class)
    fun handleConflict(e: ResourceStatusException): ErrorsResponse {
        return ErrorsResponse(ResponseError(ResourceStatus.CONFLICT, "Conflict", e.message.orEmpty()))
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception::class)
    fun handleDefault(e: Exception): ErrorsResponse {
        logger.warn("Unexpected exception!", e)
        return ErrorsResponse(ResponseError(ResourceStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e.message.orEmpty()))
    }
}
