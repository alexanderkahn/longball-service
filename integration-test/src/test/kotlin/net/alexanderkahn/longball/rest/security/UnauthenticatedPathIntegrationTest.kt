package net.alexanderkahn.longball.rest.security

import io.restassured.RestAssured
import junit.framework.TestCase.assertTrue
import org.apache.http.HttpStatus
import org.hamcrest.Matchers
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.junit.jupiter.SpringExtension

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension::class)
class UnauthenticatedPathIntegrationTest {

    @LocalServerPort
    private var port: Int = -1

    @BeforeEach
    fun setUp() {
        RestAssured.port = port
        RestAssured.basePath = "/rest"
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
    }

    @Test
    fun rejectUnauthenticated() {
        RestAssured
                .`when`().get("/unrecognizedpath")
                .then().statusCode(HttpStatus.SC_UNAUTHORIZED).body(Matchers.containsString("Unable to find authorization token"))
    }

    @Test
    fun allowInfoEndpointUnauthenticated() {
        val info = RestAssured.`when`().get("/actuator/info").then().statusCode(HttpStatus.SC_OK).extract().response().jsonPath()
        println(info.prettify())
        assertTrue(info.getString("app.name").isNotBlank())
        assertTrue(info.getString("app.version").isNotBlank())
        assertTrue(info.getString("app.buildDate").isNotBlank())
    }

    @Test
    fun allowHealthEndpointUnauthenticated() {
        val health = RestAssured.`when`().get("/actuator/health").then().statusCode(HttpStatus.SC_OK).extract().response().jsonPath()
        assertTrue(health.getString("status").isNotBlank())
    }
}