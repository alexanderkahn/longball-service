package net.alexanderkahn.longball.itest

import io.restassured.RestAssured
import io.restassured.http.ContentType
import io.restassured.specification.RequestSpecification
import org.junit.Before
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner::class)
@ActiveProfiles("test", "bypassToken")
abstract class AbstractBypassTokenIntegrationTest {

    @LocalServerPort private var port: Int = -1
    @Value("\${oauth.test.bypassToken}") private lateinit var configuredToken: String

    @Before
    fun setUp() {
        RestAssured.port = port
        RestAssured.basePath = "/rest/v1"
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
    }

    protected fun withBypassToken(): RequestSpecification {
        return RestAssured.given()
                .header("Authorization", "Bearer $configuredToken")
                .contentType(ContentType.JSON)
    }
}