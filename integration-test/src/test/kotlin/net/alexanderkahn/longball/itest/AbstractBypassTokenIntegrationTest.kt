package net.alexanderkahn.longball.itest

import io.restassured.RestAssured
import io.restassured.http.ContentType
import io.restassured.specification.RequestSpecification
import junit.framework.TestCase
import net.alexanderkahn.longball.presentation.rest.model.LeagueAttributes
import net.alexanderkahn.longball.presentation.rest.model.RequestLeague
import net.alexanderkahn.longball.provider.repository.TeamRepository
import net.alexanderkahn.service.base.api.security.UserContext
import net.alexanderkahn.service.base.presentation.request.ObjectRequest
import net.alexanderkahn.service.base.presentation.security.jwt.BypassTokenManager
import org.apache.commons.lang3.RandomStringUtils
import org.apache.http.HttpStatus
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner::class)
@ActiveProfiles("test", "bypassToken")
abstract class AbstractBypassTokenIntegrationTest {

    @Autowired private lateinit var bypassTokenManager: BypassTokenManager
    @Autowired private lateinit var teamRepository: TeamRepository


    @LocalServerPort private var port: Int = -1
    @Value("\${oauth.test.bypassToken}") private lateinit var configuredToken: String

    private val addedLeagueIds = mutableListOf<UUID>()

    @Before
    fun setUp() {
        UserContext.currentUser = bypassTokenManager.tokenBypassUser
        if (!isInitialized.getAndSet(true)) {
            RestAssured.port = port
            RestAssured.basePath = "/rest/v1"
            RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
        }
    }

    @After
    fun tearDown() {
        teamRepository.deleteAll()
        deleteLeagues()
        UserContext.clearCurrentUser()
    }

    protected fun withBypassToken(): RequestSpecification {
        return RestAssured.given()
                .header("Authorization", "Bearer $configuredToken")
                .contentType(ContentType.JSON)
    }

    protected fun addLeagues(number: Int) {
        IntRange(0, number - 1).map { randomRequestLeague() }.forEach { addLeague(it) }
    }

    protected fun addLeague(league: RequestLeague = randomRequestLeague()): UUID {
        val response = withBypassToken().body(ObjectRequest(league))
                .`when`().post("/leagues")
                .then().statusCode(HttpStatus.SC_CREATED)
                .extract().response()
        val leagueId = UUID.fromString(response.jsonPath().getString("data.id"))
        TestCase.assertNotNull(leagueId)
        addedLeagueIds.add(leagueId)
        return leagueId
    }

    private fun randomRequestLeague() = RequestLeague("leagues", LeagueAttributes(RandomStringUtils.randomAlphabetic(10)))

    protected fun deleteLeagues() {
        val leaguesToDelete = addedLeagueIds.toList()
        leaguesToDelete.forEach { deleteLeague(it) }
    }

    private fun deleteLeague(id: UUID) {
        withBypassToken()
                .`when`().delete("/leagues/$id")
                .then().statusCode(HttpStatus.SC_OK)
        addedLeagueIds.remove(id)
    }
}

val isInitialized = AtomicBoolean(false)