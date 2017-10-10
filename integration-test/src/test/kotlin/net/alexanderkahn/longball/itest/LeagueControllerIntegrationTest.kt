package net.alexanderkahn.longball.itest

import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import net.alexanderkahn.longball.presentation.rest.model.LeagueAttributes
import net.alexanderkahn.longball.presentation.rest.model.RequestLeague
import net.alexanderkahn.longball.presentation.rest.model.ResponseLeague
import net.alexanderkahn.service.base.presentation.request.ObjectRequest
import org.apache.commons.lang3.RandomStringUtils
import org.apache.http.HttpStatus
import org.junit.After
import org.junit.Test

class LeagueControllerIntegrationTest : AbstractBypassTokenIntegrationTest() {

    private val addedLeagueIds = mutableListOf<String>()

    @After
    fun tearDown() {
        deleteLeagues()
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
        deleteLeagues()
    }

    //TODO: test pageInfo

    @Test
    fun deleteLeague() {
        addLeagues(1)
        val leaguesInFirstResponse = withBypassToken()
                .`when`().get("/leagues")
                .then().statusCode(200)
                .extract().response().jsonPath().getList<ResponseLeague>("data").size
        assertEquals(1, leaguesInFirstResponse)

        deleteLeagues()

        val leaguesInSecondResponse = withBypassToken()
                .`when`().get("/leagues")
                .then().statusCode(200)
                .extract().response().jsonPath().getList<ResponseLeague>("data").size
        assertEquals(0, leaguesInSecondResponse)
    }

    @Test
    fun addLeagueWrongType() {
        withBypassToken().body(ObjectRequest(RequestLeague("otherType", LeagueAttributes("name"))))
                .`when`().post("/leagues")
                .then().statusCode(HttpStatus.SC_CONFLICT)
    }

    private fun addLeagues(number: Int) {
        val leagues = IntRange(0, number - 1).map { RequestLeague("leagues", LeagueAttributes(RandomStringUtils.randomAlphabetic(10))) }
        leagues.forEach { addLeague(it) }


    }

    private fun addLeague(league: RequestLeague): String {
        val response = withBypassToken().body(ObjectRequest(league))
                .`when`().post("/leagues")
                .then().statusCode(HttpStatus.SC_CREATED)
                .extract().response()
        val leagueId = response.jsonPath().getString("data.id")
        assertNotNull(leagueId)
        addedLeagueIds.add(leagueId)
        return leagueId
    }

    private fun deleteLeagues() {
        val leaguesToDelete = addedLeagueIds.toList()
        leaguesToDelete.forEach { deleteLeague(it) }
    }

    private fun deleteLeague(id: String) {
        withBypassToken()
                .`when`().delete("/leagues/$id")
                .then().statusCode(HttpStatus.SC_OK)
        addedLeagueIds.remove(id)
    }
}