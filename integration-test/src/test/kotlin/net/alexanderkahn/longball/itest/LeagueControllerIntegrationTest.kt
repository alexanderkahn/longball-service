package net.alexanderkahn.longball.itest


import junit.framework.TestCase
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import net.alexanderkahn.longball.presentation.rest.model.LeagueAttributes
import net.alexanderkahn.longball.presentation.rest.model.RequestLeague
import net.alexanderkahn.longball.presentation.rest.model.ResponseLeague
import net.alexanderkahn.longball.provider.repository.LeagueRepository
import net.alexanderkahn.service.base.presentation.request.ObjectRequest
import org.apache.commons.lang3.RandomStringUtils
import org.apache.http.HttpStatus
import org.junit.After
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import java.util.*

class LeagueControllerIntegrationTest : AbstractBypassTokenIntegrationTest() {

    @Autowired lateinit var leagueRepository: LeagueRepository

    @After
    fun tearDown() {
        leagueRepository.deleteAll()
    }

    @Test
    fun getLeagues() {
        addLeagues(5)
        val response = withBypassToken()
                .`when`().get("/leagues")
                .then().statusCode(200)
                .extract().response()
        val leagues: List<ResponseLeague> = response.jsonPath().getList<ResponseLeague>("data")
        assertEquals(5, leagues.size)
        assertNotNull(response.jsonPath().getString("data[0].attributes.name"))
    }

    //TODO: test pageInfo

    @Test
    fun deleteLeague() {
        addLeagues(1)
        val firstResponse = withBypassToken()
                .`when`().get("/leagues")
                .then().statusCode(200)
                .extract().response().jsonPath()
        assertEquals(1, firstResponse.getList<ResponseLeague>("data").size)

        val leagueId = firstResponse.getUUID("data[0].id")
        withBypassToken()
                .`when`().delete("/leagues/$leagueId")
                .then().statusCode(HttpStatus.SC_OK)

        val secondResponse = withBypassToken()
                .`when`().get("/leagues")
                .then().statusCode(200)
                .extract().response().jsonPath()
        assertEquals(0, secondResponse.getList<ResponseLeague>("data").size)
    }

    @Test
    fun addLeagueWrongType() {
        withBypassToken().body(ObjectRequest(RequestLeague("otherType", LeagueAttributes("name"))))
                .`when`().post("/leagues")
                .then().statusCode(HttpStatus.SC_CONFLICT)
    }

    private fun addLeagues(number: Int) {
        IntRange(0, number - 1).forEach { addLeague() }
    }

    private fun addLeague(league: RequestLeague = randomRequestLeague()): UUID {
        val response = withBypassToken().body(ObjectRequest(league))
                .`when`().post("/leagues")
                .then().statusCode(HttpStatus.SC_CREATED)
                .extract().response()
        val leagueId = UUID.fromString(response.jsonPath().getString("data.id"))
        TestCase.assertNotNull(leagueId)
        return leagueId
    }

    private fun randomRequestLeague() = RequestLeague("leagues", LeagueAttributes(RandomStringUtils.randomAlphabetic(10)))
}