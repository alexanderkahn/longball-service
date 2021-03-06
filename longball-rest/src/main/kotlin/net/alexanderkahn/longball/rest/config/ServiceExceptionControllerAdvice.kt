package net.alexanderkahn.longball.rest.config


import net.alexanderkahn.longball.api.exception.*
import net.alexanderkahn.service.commons.model.request.validation.ExpectedType
import net.alexanderkahn.service.commons.model.response.body.ErrorsResponse
import net.alexanderkahn.service.commons.model.response.body.error.ResponseError
import net.alexanderkahn.service.commons.model.response.body.meta.ObjectResponseMeta
import net.alexanderkahn.service.commons.model.response.body.meta.ResourceStatus
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.HttpMediaTypeNotSupportedException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.NoHandlerFoundException
import javax.validation.ConstraintViolation
import javax.validation.ConstraintViolationException
import javax.validation.constraints.Size

@RestControllerAdvice
class ServiceExceptionControllerAdvice {

    private val logger = LoggerFactory.getLogger(this::class.java)!!

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException::class, HttpMessageNotReadableException::class)
    fun handleBadRequest(e: Exception): ErrorsResponse {
        return ErrorsResponse(ResponseError(ResourceStatus.BAD_REQUEST, "Bad request", e.message.orEmpty()))
    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun handleMethodNotAllowed(e: Exception): ErrorsResponse {
        return ErrorsResponse(ResponseError(ResourceStatus.METHOD_NOT_ALLOWED, "Method not allowed", e.message.orEmpty()))
    }

    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler(HttpMediaTypeNotSupportedException::class)
    fun handleUnsupportedMediaType(e: Exception): ErrorsResponse {
        return ErrorsResponse(ResponseError(ResourceStatus.UNSUPPORTED_MEDIA_TYPE, "Unsupported media type", e.message.orEmpty()))
    }

    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    @ExceptionHandler(InvalidStateException::class, NotImplementedException::class)
    fun handleServerException(e: Exception): ErrorsResponse {
        return ErrorsResponse(ResponseError(ResourceStatus.NOT_IMPLEMENTED, "Not implemented", e.message.orEmpty()))
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException::class, NoHandlerFoundException::class)
    fun handleNotFound(e: Exception): ErrorsResponse {
        return ErrorsResponse(ResponseError(ResourceStatus.NOT_FOUND, "Resource not found", e.message.orEmpty()))
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(InvalidRelationshipsException::class)
    fun handleInvalidRelationships(e: InvalidRelationshipsException): ErrorsResponse {
        val responseErrors = e.causes.map { ResponseError(ResourceStatus.NOT_FOUND, "Related resource not found", it.message) }
        return ErrorsResponse(ObjectResponseMeta(), responseErrors)
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthenticatedException::class)
    fun handleUnauthorized(e: Exception): ErrorsResponse {
        return ErrorsResponse(ResponseError(ResourceStatus.UNAUTHORIZED, "Unauthorized", e.message.orEmpty()))
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ConflictException::class)
    fun handleConflict(e: Exception): ErrorsResponse {
        return ErrorsResponse(ResponseError(ResourceStatus.CONFLICT, "Conflict", e.message.orEmpty()))
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolation(e: ConstraintViolationException): ResponseEntity<ErrorsResponse> {
        val errors = e.constraintViolations.map { toResponseError(it) }.sortedBy { it.status.statusCode }
        val responseBody = ErrorsResponse(ObjectResponseMeta(), errors)
        return ResponseEntity(responseBody, getHttpStatus(errors))
    }

    private fun toResponseError(violation: ConstraintViolation<*>): ResponseError {
        return when(violation.constraintDescriptor.annotation.annotationClass) {
            ExpectedType::class -> ResponseError(ResourceStatus.CONFLICT, "Unexpected type", violation.message.orEmpty())
            Size::class -> ResponseError(ResourceStatus.BAD_REQUEST, "Invalid attribute length", violation.message.orEmpty())
            else -> ResponseError(ResourceStatus.INTERNAL_SERVER_ERROR, "Unknown error", violation.message.orEmpty())
        }
    }

    private fun getHttpStatus(errors: List<ResponseError>): HttpStatus {
        return HttpStatus.valueOf(getCommonStatus(errors).statusCode.toInt()) ?: HttpStatus.INTERNAL_SERVER_ERROR
    }

    private fun getCommonStatus(errors: List<ResponseError>): ResourceStatus {
        val errorStatuses = errors.map { it.status }
        return when {
            errorStatuses.isEmpty() -> ResourceStatus.INTERNAL_SERVER_ERROR
            errorStatuses.distinct().size == 1 -> errorStatuses.single()
            errorStatuses.map { toStatusSeries(it) }.distinct().size == 1 ->
                errorStatuses.map { toStatusSeries(it) }.first { it != null } ?: ResourceStatus.INTERNAL_SERVER_ERROR
            else -> ResourceStatus.BAD_REQUEST
        }
    }

    private fun toStatusSeries(httpStatus: ResourceStatus): ResourceStatus? {
        val code = httpStatus.statusCode.toInt()
        val seriesCode = (code - (code % 100)).toString()
        return ResourceStatus.fromCode(seriesCode)
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception::class)
    fun handleDefault(e: Exception): ErrorsResponse {
        logger.warn("Unexpected exception!", e)
        return ErrorsResponse(ResponseError(ResourceStatus.INTERNAL_SERVER_ERROR, "Internal server error", e.message.orEmpty()))
    }
}
