package net.alexanderkahn.longball.itest

import junit.framework.TestCase
import junit.framework.TestCase.assertEquals
import net.alexanderkahn.longball.presentation.rest.model.*
import net.alexanderkahn.longball.provider.entity.LeagueEntity
import net.alexanderkahn.longball.provider.entity.PersonEntity
import net.alexanderkahn.longball.provider.entity.RosterPositionEntity
import net.alexanderkahn.longball.provider.entity.TeamEntity
import net.alexanderkahn.longball.provider.repository.LeagueRepository
import net.alexanderkahn.longball.provider.repository.PersonRepository
import net.alexanderkahn.longball.provider.repository.RosterPositionRepository
import net.alexanderkahn.longball.provider.repository.TeamRepository
import net.alexanderkahn.service.base.presentation.request.ObjectRequest
import org.apache.commons.lang3.RandomUtils
import org.apache.http.HttpStatus
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDate
import java.util.*

class RosterPositionControllerIntegrationTest : AbstractBypassTokenIntegrationTest() {

    @Autowired private lateinit var personRepository: PersonRepository
    @Autowired private lateinit var leagueRepository: LeagueRepository
    @Autowired private lateinit var teamRepository: TeamRepository
    @Autowired private lateinit var rosterPositionRepository: RosterPositionRepository

    private lateinit var babe: PersonEntity
    private lateinit var team: TeamEntity

    @Before
    fun setUp() {
        val league = LeagueEntity("National Association of Professional Base Ball Players")
        babe = PersonEntity("George", "Ruth")
        team = TeamEntity(league, "BOB", "Boston", "Braves") //that's right. Babe Ruth, the famous Brave.

        leagueRepository.save(league)
        teamRepository.save(team)
        personRepository.save(babe)
    }

    @After
    fun tearDown() {
        rosterPositionRepository.deleteAll()
        teamRepository.deleteAll()
        personRepository.deleteAll()
        leagueRepository.deleteAll()
    }

    @Test
    fun post() {
        val requestRosterPos = requestRosterPosition()
        val responseRosterPos = withBypassToken().body(ObjectRequest(requestRosterPos))
                .`when`().post("/rosterpositions")
                .then().statusCode(HttpStatus.SC_CREATED)
                .extract().response().jsonPath().getObject("data", ResponseRosterPosition::class.java)
        TestCase.assertEquals(requestRosterPos.attributes, responseRosterPos.attributes)
        TestCase.assertEquals(requestRosterPos.relationships, responseRosterPos.relationships)
    }

    @Test
    fun postWrongType() {
        val badRequest = gson.toJson(ObjectRequest(requestRosterPosition())).replace("rosterpositions", "romperpartitions")
        withBypassToken().body(badRequest)
                .`when`().post("/rosterpositions")
                .then().statusCode(HttpStatus.SC_CONFLICT)
    }

    @Test
    fun postWrongRelationshipType() {
        val badRequest = gson.toJson(ObjectRequest(requestRosterPosition())).replace("people", "pringle")
        withBypassToken().body(badRequest)
                .`when`().post("/rosterpositions")
                .then().statusCode(HttpStatus.SC_CONFLICT)
    }

    @Test
    fun postBadTeamId() {
        val badRequest = gson.toJson(ObjectRequest(requestRosterPosition())).replace(team.id.toString(), UUID.randomUUID().toString())
        withBypassToken().body(badRequest)
                .`when`().post("/rosterpositions")
                .then().statusCode(HttpStatus.SC_NOT_FOUND)
    }

    @Test
    fun postBadPersonId() {
        val badRequest = gson.toJson(ObjectRequest(requestRosterPosition())).replace(babe.id.toString(), UUID.randomUUID().toString())
        withBypassToken().body(badRequest)
                .`when`().post("/rosterpositions")
                .then().statusCode(HttpStatus.SC_NOT_FOUND)
    }

    @Test
    fun delete() {
        val position = RosterPositionEntity(team, babe, 3, LocalDate.now())
        rosterPositionRepository.save(position)
        withBypassToken()
                .`when`().delete("/rosterpositions/${position.id}")
                .then().statusCode(HttpStatus.SC_OK)

        withBypassToken()
                .`when`().get("/rosterpositions/${position.id}")
                .then().statusCode(HttpStatus.SC_NOT_FOUND)
    }


    @Test
    fun getOne() {
        val position = RosterPositionEntity(team, babe, 3, LocalDate.now().minusDays(5), LocalDate.now())
        rosterPositionRepository.save(position)
        val response = withBypassToken().`when`().get("/rosterpositions/${position.id}")
                .then().statusCode(HttpStatus.SC_OK).extract().body().jsonPath().getObject("data", ResponseRosterPosition::class.java)
        assertEquals(position.id, response.id)
        assertEquals(position.team.id, response.relationships.team.data.id)
        assertEquals(position.player.id, response.relationships.player.data.id)
        assertEquals(position.jerseyNumber, response.attributes.jerseyNumber)
        assertEquals(position.startDate, response.attributes.startDate)
        assertEquals(position.endDate, response.attributes.endDate)
    }

    @Test
    fun getCollection() {
        val mick = PersonEntity("Mickey", "Mantle")
        personRepository.save(mick)
        listOf(babe, mick)
                .map { RosterPositionEntity(team, it, RandomUtils.nextInt(1, 99), LocalDate.now()) }
                .forEach { rosterPositionRepository.save(it) }

        val getResponse = withBypassToken().`when`().get("/rosterpositions")
                .then().statusCode(HttpStatus.SC_OK)
                .extract().response().jsonPath()

        assertEquals(2, getResponse.getList<ResponseTeam>("data").size)
    }

    private fun requestRosterPosition(): RequestRosterPosition {
        val attributes = RosterPositionAttributes(3, LocalDate.now())
        val relationships = RosterPositionRelationships(team.id, babe.id)
        return RequestRosterPosition("rosterpositions", attributes, relationships)
    }
}