package net.alexanderkahn.longball.presentation.config


import net.alexanderkahn.service.commons.model.exception.*
import net.alexanderkahn.service.commons.model.response.body.ErrorResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ServiceExceptionControllerAdvice {

    val logger = LoggerFactory.getLogger(this::class.java)

    // Business-logic exceptions
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException::class, HttpMessageNotReadableException::class)
    fun handleBadRequest(e: ResponseStatusException): ErrorResponse {
        return ErrorResponse(e)
    }

    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    @ExceptionHandler(InvalidStateException::class, NotImplementedException::class)
    fun handleServerException(e: ResponseStatusException): ErrorResponse {
        return ErrorResponse(e)
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException::class)
    fun handleNotFound(e: ResponseStatusException): ErrorResponse {
        return ErrorResponse(e)
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthenticatedException::class)
    fun handleUnauthorized(e: ResponseStatusException): ErrorResponse {
        return ErrorResponse(e)
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ConflictException::class)
    fun handleConflict(e: ResponseStatusException): ErrorResponse {
        return ErrorResponse(e)
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception::class)
    fun handleDefault(e: Exception): ErrorResponse {
        logger.warn("Unexpected exception!", e)
        return ErrorResponse(e.toResponse())
    }
}
