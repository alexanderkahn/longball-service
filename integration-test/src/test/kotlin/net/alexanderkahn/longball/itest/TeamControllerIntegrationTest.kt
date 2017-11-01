package net.alexanderkahn.longball.itest

import junit.framework.TestCase.assertEquals
import net.alexanderkahn.longball.model.dto.RequestTeam
import net.alexanderkahn.longball.model.dto.ResponseTeam
import net.alexanderkahn.longball.model.dto.TeamAttributes
import net.alexanderkahn.longball.model.dto.TeamRelationships
import net.alexanderkahn.longball.provider.entity.LeagueEntity
import net.alexanderkahn.longball.provider.entity.TeamEntity
import net.alexanderkahn.longball.provider.repository.LeagueRepository
import net.alexanderkahn.longball.provider.repository.TeamRepository
import net.alexanderkahn.service.base.model.request.ObjectRequest
import org.apache.commons.lang3.RandomStringUtils.randomAlphabetic
import org.apache.http.HttpStatus
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import java.util.*

class TeamControllerIntegrationTest : AbstractBypassTokenIntegrationTest() {

    @Autowired private lateinit var leagueRepository: LeagueRepository
    @Autowired private lateinit var teamRepository: TeamRepository

    private lateinit var parentLeague: LeagueEntity

    @Before
    fun setup() {
        parentLeague = LeagueEntity(randomAlphabetic(10), embeddableUser)
        leagueRepository.save(parentLeague)
    }

    @After
    fun tearDown() {
        teamRepository.deleteAll()
        leagueRepository.deleteAll()
    }

    @Test
    fun post() {
        val requestTeam = buildRequestTeam()
        val response = withBypassToken().body(requestTeam)
                .`when`().post("/teams")
                .then().statusCode(HttpStatus.SC_CREATED)
                .extract().response().jsonPath().getObject("data", ResponseTeam::class.java)
        assertEquals(requestTeam.data.attributes, response.attributes)
        assertEquals(requestTeam.data.relationships, response.relationships)
    }

    @Test
    fun postWrongType() {
        val requestPayload = gson.toJson(buildRequestTeam()).replace("teams", "tames")
        withBypassToken().body(requestPayload)
                .`when`().post("/teams")
                .then().statusCode(HttpStatus.SC_CONFLICT)
    }

    @Test
    fun postWrongRelationshipType() {
        val requestPayload = gson.toJson(buildRequestTeam()).replace("leagues", "legumes")
        withBypassToken().body(requestPayload)
                .`when`().post("/teams")
                .then().statusCode(HttpStatus.SC_CONFLICT)
    }

    @Test
    fun postBadLeagueId() {
        val requestTeam = buildRequestTeam()
        val badIdTeam = gson.toJson(requestTeam).replace(requestTeam.data.relationships.league.data.id.toString(), UUID.randomUUID().toString())
        withBypassToken().body(badIdTeam)
                .`when`().post("/teams")
                .then().statusCode(HttpStatus.SC_NOT_FOUND)
    }

    @Test
    fun delete() {
        val team = TeamEntity(parentLeague, randomAlphabetic(3), "location", "nickname", embeddableUser)
        teamRepository.save(team)
        withBypassToken()
                .`when`().delete("/teams/${team.id}")
                .then().statusCode(HttpStatus.SC_OK)

        withBypassToken()
                .`when`().get("/teams/${team.id}")
                .then().statusCode(HttpStatus.SC_NOT_FOUND)
    }

    @Test
    fun getOne() {
        val team = TeamEntity(parentLeague, randomAlphabetic(3), "location", "nickname", embeddableUser)
        teamRepository.save(team)
        val response = withBypassToken().`when`().get("/teams/${team.id}")
                .then().statusCode(HttpStatus.SC_OK).extract().body().jsonPath().getObject("data", ResponseTeam::class.java)

        assertEquals(team.id, response.id)
        assertEquals(team.league.id, response.relationships.league.data.id)
        assertEquals(team.abbreviation, response.attributes.abbreviation)
        assertEquals(team.location, response.attributes.location)
        assertEquals(team.nickname, response.attributes.nickname)
    }

    @Test
    fun getCollection() {
        listOf(buildRequestTeam(), buildRequestTeam()).forEach {
            val team = TeamEntity(parentLeague, randomAlphabetic(3), randomAlphabetic(5), randomAlphabetic(5), embeddableUser)
            teamRepository.save(team)
        }

        val getResponse = withBypassToken().`when`().get("/teams")
                .then().statusCode(HttpStatus.SC_OK)
                .extract().response().jsonPath()

        assertEquals(2, getResponse.getList<ResponseTeam>("data").size)
    }

    private fun buildRequestTeam(): ObjectRequest<RequestTeam> {
        val attributes = TeamAttributes(randomAlphabetic(3).toUpperCase(), randomAlphabetic(10), randomAlphabetic(10))
        val relationships = TeamRelationships(parentLeague.id)
        return ObjectRequest(RequestTeam("teams", attributes, relationships))
    }

}