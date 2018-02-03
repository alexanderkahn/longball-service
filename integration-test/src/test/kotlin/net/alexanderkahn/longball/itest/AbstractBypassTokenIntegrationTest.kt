package net.alexanderkahn.longball.itest

import com.google.gson.*
import io.restassured.RestAssured
import io.restassured.http.ContentType
import io.restassured.specification.RequestSpecification
import net.alexanderkahn.longball.provider.entity.UserEntity
import net.alexanderkahn.longball.provider.repository.LeagueRepository
import net.alexanderkahn.longball.provider.repository.TeamRepository
import net.alexanderkahn.longball.provider.service.UserService
import net.alexanderkahn.service.base.presentation.security.jwt.BypassTokenManager
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import java.lang.reflect.Type
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner::class)
@ActiveProfiles("test", "bypassToken")
abstract class AbstractBypassTokenIntegrationTest {

    @Autowired private lateinit var bypassTokenManager: BypassTokenManager
    @Autowired private lateinit var userService: UserService
    @Autowired private lateinit var leagueRepository: LeagueRepository
    @Autowired private lateinit var teamRepository: TeamRepository

    @LocalServerPort private var port: Int = -1

    protected lateinit var userEntity: UserEntity

    @Value("\${oauth.test.bypassToken}") private lateinit var configuredToken: String

    @Before
    fun setUpBase() {
        SecurityContextHolder.getContext().authentication = bypassTokenManager.tokenBypassCredentials
        SecurityContextHolder.getContext().authentication.isAuthenticated = true
        userEntity = userService.userEntity()
        RestAssured.port = port
        RestAssured.basePath = "/rest"
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
    }

    @After
    fun tearDownBase() {
        teamRepository.deleteAll()
        leagueRepository.deleteAll()
    }

    protected val gson = GsonBuilder()
            .registerTypeAdapter(LocalDate::class.java, LocalDateAdapter())
            .create()

    protected fun withBypassToken(): RequestSpecification {
        return RestAssured.given()
                .header("Authorization", "Bearer $configuredToken")
                .contentType(ContentType.JSON)
    }
}

internal class LocalDateAdapter : JsonSerializer<LocalDate> {

    override fun serialize(date: LocalDate, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        return JsonPrimitive(date.format(DateTimeFormatter.ISO_LOCAL_DATE)) // "yyyy-mm-dd"
    }
}