package net.alexanderkahn.longball.itest

import io.restassured.RestAssured
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
class UnrecognizedPathIntegrationTest {

    @LocalServerPort
    private var port: Int = -1

    @BeforeEach
    fun setUp() {
        RestAssured.port = port
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
    }

    @Test
    fun rejectUnauthenticated() {
        RestAssured
                .`when`().get("/unrecognizedpath")
                .then().statusCode(HttpStatus.SC_UNAUTHORIZED).body(Matchers.containsString("Unable to find authorization token"))
    }

    @Test
    fun allowAboutEndpointUnauthenticated() {
        RestAssured.`when`().get("/about").then().statusCode(HttpStatus.SC_OK)
    }
}