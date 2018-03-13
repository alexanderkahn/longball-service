package net.alexanderkahn.longball.itest

import com.google.gson.*
import io.restassured.RestAssured
import io.restassured.http.ContentType
import io.restassured.specification.RequestSpecification
import net.alexanderkahn.longball.api.exception.InvalidStateException
import net.alexanderkahn.longball.core.entity.UserEntity
import net.alexanderkahn.longball.core.repository.LeagueRepository
import net.alexanderkahn.longball.core.repository.TeamRepository
import net.alexanderkahn.longball.core.repository.UserRepository
import net.alexanderkahn.longball.rest.config.JwsFIlterConfiguration
import org.apache.http.HttpStatus
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.lang.reflect.Type
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension::class)
@ActiveProfiles("test", "bypassToken")
abstract class AbstractBypassTokenIntegrationTest {

    @Autowired private lateinit var userRepository: UserRepository
    @Autowired private lateinit var leagueRepository: LeagueRepository
    @Autowired private lateinit var teamRepository: TeamRepository

    @Autowired private lateinit var jwsConfig: JwsFIlterConfiguration.LongballFirebaseJwsConfig

    @LocalServerPort
    private var port: Int = -1

    protected lateinit var userEntity: UserEntity

    @BeforeEach
    fun setUpBase() {
        RestAssured.port = port
        RestAssured.basePath = "/rest"
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
        userEntity = retrieveBypassUser()
    }

    private fun retrieveBypassUser(): UserEntity {
        val response = withBypassToken()
                .`when`().get("/users/current")
                .then().statusCode(HttpStatus.SC_OK)
                .extract().response()
        val id = response.jsonPath().getUUID("data.id")
        return userRepository.findById(id).orElseThrow { InvalidStateException("Something went wrong trying to retrieve a bypass token for testing") }
    }

    @AfterEach
    fun tearDownBase() {
        teamRepository.deleteAll()
        leagueRepository.deleteAll()
    }

    protected val gson: Gson = GsonBuilder()
            .registerTypeAdapter(LocalDate::class.java, LocalDateAdapter())
            .create()

    protected fun withBypassToken(): RequestSpecification {
        return RestAssured.given()
                .header("Authorization", "Bearer ${jwsConfig.bypassToken.token}")
                .contentType(ContentType.JSON)
    }
}

internal class LocalDateAdapter : JsonSerializer<LocalDate> {

    override fun serialize(date: LocalDate, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        return JsonPrimitive(date.format(DateTimeFormatter.ISO_LOCAL_DATE)) // "yyyy-mm-dd"
    }
}