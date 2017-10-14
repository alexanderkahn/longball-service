package net.alexanderkahn.longball.itest


import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import net.alexanderkahn.longball.presentation.rest.model.LeagueAttributes
import net.alexanderkahn.longball.presentation.rest.model.RequestLeague
import net.alexanderkahn.longball.presentation.rest.model.ResponseLeague
import net.alexanderkahn.service.base.presentation.request.ObjectRequest
import org.apache.http.HttpStatus
import org.junit.Test

class LeagueControllerIntegrationTest : AbstractBypassTokenIntegrationTest() {

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
}