package net.alexanderkahn.longball.itest

import io.restassured.RestAssured
import io.restassured.http.ContentType
import io.restassured.specification.RequestSpecification
import net.alexanderkahn.longball.provider.repository.LeagueRepository
import net.alexanderkahn.longball.provider.repository.TeamRepository
import net.alexanderkahn.service.base.api.security.UserContext
import net.alexanderkahn.service.base.presentation.security.jwt.BypassTokenManager
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import java.util.concurrent.atomic.AtomicBoolean

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner::class)
@ActiveProfiles("test", "bypassToken")
abstract class AbstractBypassTokenIntegrationTest {

    @Autowired private lateinit var bypassTokenManager: BypassTokenManager
    @Autowired private lateinit var leagueRepository: LeagueRepository
    @Autowired private lateinit var teamRepository: TeamRepository

    @LocalServerPort private var port: Int = -1
    @Value("\${oauth.test.bypassToken}") private lateinit var configuredToken: String

    @Before
    fun setUpBase() {
        UserContext.currentUser = bypassTokenManager.tokenBypassUser
        if (!isInitialized.getAndSet(true)) {
            RestAssured.port = port
            RestAssured.basePath = "/rest/v1"
            RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
        }
    }

    @After
    fun tearDownBase() {
        teamRepository.deleteAll()
        leagueRepository.deleteAll()
        UserContext.clearCurrentUser()
    }

    protected fun withBypassToken(): RequestSpecification {
        return RestAssured.given()
                .header("Authorization", "Bearer $configuredToken")
                .contentType(ContentType.JSON)
    }
}

val isInitialized = AtomicBoolean(false)