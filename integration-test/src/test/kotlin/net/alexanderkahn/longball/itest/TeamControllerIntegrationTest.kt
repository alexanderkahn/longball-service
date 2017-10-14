package net.alexanderkahn.longball.itest

import com.google.gson.Gson
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import net.alexanderkahn.longball.presentation.rest.model.RequestTeam
import net.alexanderkahn.longball.presentation.rest.model.ResponseTeam
import net.alexanderkahn.longball.presentation.rest.model.TeamAttributes
import net.alexanderkahn.longball.presentation.rest.model.TeamRelationships
import net.alexanderkahn.service.base.presentation.request.ObjectRequest
import org.apache.commons.lang3.RandomStringUtils.randomAlphabetic
import org.apache.http.HttpStatus
import org.junit.Test
import java.util.*

class TeamControllerIntegrationTest : AbstractBypassTokenIntegrationTest() {

    @Test
    fun postAndDelete() {
        val requestTeam = buildRequestTeam()
        val response = withBypassToken().body(requestTeam)
                .`when`().post("/teams")
                .then().statusCode(HttpStatus.SC_CREATED)
                .extract().response().jsonPath()
        val idString = response.getString("data.id")
        assertNotNull(idString)
        assertEquals(requestTeam.data.attributes.abbreviation, response.getString("data.attributes.abbreviation"))
        assertEquals(requestTeam.data.attributes.location, response.getString("data.attributes.location"))
        assertEquals(requestTeam.data.attributes.nickname, response.getString("data.attributes.nickname"))

        withBypassToken().body(requestTeam)
                .`when`().delete("/teams/$idString")
                .then().statusCode(HttpStatus.SC_OK)

        withBypassToken()
                .`when`().get("/teams/$idString")
                .then().statusCode(HttpStatus.SC_NOT_FOUND)
    }

    @Test
    fun postWrongType() {
        val requestPayload = Gson().toJson(buildRequestTeam()).replace("teams", "tames")
        withBypassToken().body(requestPayload)
                .`when`().post("/teams")
                .then().statusCode(HttpStatus.SC_CONFLICT)
    }

    @Test
    fun postWrongRelationshipType() {
        val requestPayload = Gson().toJson(buildRequestTeam()).replace("leagues", "legumes")
        withBypassToken().body(requestPayload)
                .`when`().post("/teams")
                .then().statusCode(HttpStatus.SC_CONFLICT)
    }

    @Test
    fun postBadLeagueId() {
        val requestTeam = buildRequestTeam(UUID.randomUUID())
        withBypassToken().body(requestTeam)
                .`when`().post("/teams")
                .then().statusCode(HttpStatus.SC_NOT_FOUND)
    }

    @Test
    fun getOne() {
        val requestTeam = buildRequestTeam()
        val postResponse = withBypassToken().body(requestTeam)
                .`when`().post("/teams")
                .then().statusCode(HttpStatus.SC_CREATED)
                .extract().response().jsonPath()
        val idString = postResponse.getString("data.id")
        val getResponse = withBypassToken().`when`().get("/teams/$idString")
                .then().statusCode(HttpStatus.SC_OK).extract().body().jsonPath()
        assertEquals(postResponse.get<ResponseTeam>("data"), getResponse.get("data"))
    }

    @Test
    fun getCollection() {
        val team1 = buildRequestTeam()
        withBypassToken().body(team1)
                .`when`().post("/teams")
                .then().statusCode(HttpStatus.SC_CREATED)

        val team2 = buildRequestTeam()
        withBypassToken().body(team2)
                .`when`().post("/teams")
                .then().statusCode(HttpStatus.SC_CREATED)

        val getResponse = withBypassToken().`when`().get("/teams")
                .then().statusCode(HttpStatus.SC_OK)
                .extract().response().jsonPath()

        assertEquals(2, getResponse.getList<ResponseTeam>("data").size)
    }

    private fun buildRequestTeam(leagueId: UUID = addLeague()): ObjectRequest<RequestTeam> {
        val attributes = TeamAttributes(randomAlphabetic(3).toUpperCase(), randomAlphabetic(10), randomAlphabetic(10))
        val relationships = TeamRelationships(leagueId)
        return ObjectRequest(RequestTeam("teams", attributes, relationships))
    }
}