package net.alexanderkahn.longball.presentation.config


import net.alexanderkahn.service.commons.model.exception.*
import net.alexanderkahn.service.commons.model.response.ErrorResponse
import org.slf4j.LoggerFactory
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ServiceExceptionControllerAdvice {

    val logger = LoggerFactory.getLogger(this::class.java)

    @ExceptionHandler(BadRequestException::class, HttpMessageNotReadableException::class)
    fun handleBadRequest(e: ResponseStatusException): ErrorResponse {
        return ErrorResponse(e)
    }

    @ExceptionHandler(InvalidStateException::class, NotImplementedException::class)
    fun handleServerException(e: ResponseStatusException): ErrorResponse {
        return ErrorResponse(e)
    }

    @ExceptionHandler(NotFoundException::class)
    fun handleNotFound(e: ResponseStatusException): ErrorResponse {
        return ErrorResponse(e)
    }

    @ExceptionHandler(UnauthenticatedException::class)
    fun handleUnauthorized(e: ResponseStatusException): ErrorResponse {
        return ErrorResponse(e)
    }

    @ExceptionHandler(ConflictException::class)
    fun handleConflict(e: ResponseStatusException): ErrorResponse {
        return ErrorResponse(e)
    }

    @ExceptionHandler(Exception::class)
    fun handleDefault(e: Exception): ErrorResponse {
        logger.warn("Unexpected exception!", e)
        return ErrorResponse(e.toResponse())
    }
}
