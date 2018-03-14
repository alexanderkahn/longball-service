package net.alexanderkahn.longball.rest.controller

import io.restassured.http.ContentType
import net.alexanderkahn.longball.rest.AbstractBypassTokenIntegrationTest
import net.alexanderkahn.service.commons.model.response.body.ErrorsResponse
import net.alexanderkahn.service.commons.model.response.body.meta.ResourceStatus
import org.apache.http.HttpStatus
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class InvalidRequestIntegrationTest : AbstractBypassTokenIntegrationTest() {

    @Test
    internal fun missingResourceReturnsErrorResponse() {
        val response = withBypassToken()
                .`when`().get("/lergs")
                .then().statusCode(HttpStatus.SC_NOT_FOUND)
                .extract().jsonPath()
        val errorsResponse = response.getObject("", ErrorsResponse::class.java)

        assertEquals(1, errorsResponse.errors.size)
        Assertions.assertEquals(ResourceStatus.NOT_FOUND, errorsResponse.errors.first().status)
    }

    @Test
    internal fun unsupportedMediaTypeReturnsErrorResponse() {
        val response = withBypassToken()
                .contentType(ContentType.TEXT)
                .`when`().post("/leagues")
                .then().statusCode(HttpStatus.SC_UNSUPPORTED_MEDIA_TYPE)
                .extract().jsonPath()
        val errorsResponse = response.getObject("", ErrorsResponse::class.java)

        assertEquals(1, errorsResponse.errors.size)
        Assertions.assertEquals(ResourceStatus.UNSUPPORTED_MEDIA_TYPE, errorsResponse.errors.first().status)
    }

    @Test
    internal fun malformedRequestBodyReturnsErrorResponse() {
        val response = withBypassToken()
                .`when`().body(""" "data": { "terp": "doop", } """).post("/leagues")
                .then().statusCode(HttpStatus.SC_BAD_REQUEST)
                .extract().jsonPath()
        val errorsResponse = response.getObject("", ErrorsResponse::class.java)

        assertEquals(1, errorsResponse.errors.size)
        Assertions.assertEquals(ResourceStatus.BAD_REQUEST, errorsResponse.errors.first().status)
    }

    @Test
    internal fun methodNotAllowedReturnsErrorResponse() {
        val response = withBypassToken()
                .`when`().patch("/leagues")
                .then().statusCode(HttpStatus.SC_METHOD_NOT_ALLOWED)
                .extract().jsonPath()
        val errorsResponse = response.getObject("", ErrorsResponse::class.java)

        assertEquals(1, errorsResponse.errors.size)
        Assertions.assertEquals(ResourceStatus.METHOD_NOT_ALLOWED, errorsResponse.errors.first().status)
    }
}