package net.alexanderkahn.longball.presentation.rest.controller

import junit.framework.TestCase
import junit.framework.TestCase.assertEquals
import net.alexanderkahn.longball.api.model.*
import net.alexanderkahn.longball.itest.AbstractBypassTokenIntegrationTest
import net.alexanderkahn.longball.provider.assembler.toPersistence
import net.alexanderkahn.longball.provider.assembler.toResponse
import net.alexanderkahn.longball.provider.entity.LeagueEntity
import net.alexanderkahn.longball.provider.entity.PersonEntity
import net.alexanderkahn.longball.provider.entity.RosterPositionEntity
import net.alexanderkahn.longball.provider.entity.TeamEntity
import net.alexanderkahn.longball.provider.repository.LeagueRepository
import net.alexanderkahn.longball.provider.repository.PersonRepository
import net.alexanderkahn.longball.provider.repository.RosterPositionRepository
import net.alexanderkahn.longball.provider.repository.TeamRepository
import net.alexanderkahn.service.commons.model.request.body.ObjectRequest
import org.apache.commons.lang3.RandomUtils
import org.apache.http.HttpStatus
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDate
import java.util.*

class RosterPositionControllerIntegrationTest : AbstractBypassTokenIntegrationTest() {

    @Autowired private lateinit var personRepository: PersonRepository
    @Autowired private lateinit var leagueRepository: LeagueRepository
    @Autowired private lateinit var teamRepository: TeamRepository
    @Autowired private lateinit var rosterPositionRepository: RosterPositionRepository

    private lateinit var babe: RosterPositionEntity

    @BeforeEach
    fun setUp() {
        val league = LeagueEntity("MLB", userEntity)
        val team = TeamEntity(league, "HOU", "Houston", "Astros", userEntity) //that's right. Babe Ruth, the famous Astro.
        leagueRepository.save(league)
        teamRepository.save(team)

        babe = getTestRosterPosition("Babe", "Ruth")
    }

    @AfterEach
    fun tearDown() {
        rosterPositionRepository.deleteAll()
        teamRepository.deleteAll()
        personRepository.deleteAll()
        leagueRepository.deleteAll()
    }

    @Test
    fun post() {
        val requestRosterPos = babeRequest()
        val responseRosterPos = withBypassToken().body(ObjectRequest(requestRosterPos))
                .`when`().post("/rosterpositions")
                .then().statusCode(HttpStatus.SC_CREATED)
                .extract().response().jsonPath().getObject("data", ResponseRosterPosition::class.java)
        TestCase.assertEquals(requestRosterPos.attributes, responseRosterPos.attributes)
        TestCase.assertEquals(requestRosterPos.relationships, responseRosterPos.relationships)
    }

    @Test
    fun postWrongType() {
        val badRequest = gson.toJson(ObjectRequest(babeRequest())).replace("rosterpositions", "romperpartitions")
        withBypassToken().body(badRequest)
                .`when`().post("/rosterpositions")
                .then().statusCode(HttpStatus.SC_CONFLICT)
    }

    @Test
    fun postWrongRelationshipType() {
        val badRequest = gson.toJson(ObjectRequest(babeRequest())).replace("people", "pringle")
        withBypassToken().body(badRequest)
                .`when`().post("/rosterpositions")
                .then().statusCode(HttpStatus.SC_CONFLICT)
    }

    @Test
    fun postBadTeamId() {
        val badRequest = gson.toJson(ObjectRequest(babeRequest())).replace(babe.team.id.toString(), UUID.randomUUID().toString())
        withBypassToken().body(badRequest)
                .`when`().post("/rosterpositions")
                .then().statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
    }

    @Test
    fun postBadPersonId() {
        val badRequest = gson.toJson(ObjectRequest(babeRequest())).replace(babe.player.id.toString(), UUID.randomUUID().toString())
        withBypassToken().body(badRequest)
                .`when`().post("/rosterpositions")
                .then().statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY)
    }

    @Test
    fun delete() {
        rosterPositionRepository.save(babe)
        withBypassToken()
                .`when`().delete("/rosterpositions/${babe.id}")
                .then().statusCode(HttpStatus.SC_OK)

        withBypassToken()
                .`when`().get("/rosterpositions/${babe.id}")
                .then().statusCode(HttpStatus.SC_NOT_FOUND)
    }


    @Test
    fun getOne() {
        rosterPositionRepository.save(babe)
        val response = withBypassToken().`when`().get("/rosterpositions/${babe.id}")
                .then().statusCode(HttpStatus.SC_OK).extract().body().jsonPath().getObject("data", ResponseRosterPosition::class.java)
        assertEquals(babe.id, response.id)
        assertEquals(babe.team.id, response.relationships.team.data.id)
        assertEquals(babe.player.id, response.relationships.player.data.id)
        assertEquals(babe.jerseyNumber, response.attributes.jerseyNumber)
        assertEquals(babe.startDate.toLocalDate(), response.attributes.startDate)
        assertEquals(babe.endDate?.toLocalDate(), response.attributes.endDate)
    }

    @Test
    fun getOneWithIncluded() {
        rosterPositionRepository.save(babe)
        val response = withBypassToken().`when`().get("/rosterpositions/${babe.id}?include=player")
                .then().statusCode(HttpStatus.SC_OK).extract().body().jsonPath().getObject("included[0]", ResponsePerson::class.java)
        assertEquals(babe.player.toResponse(), response)
    }

    @Test
    fun getCollection() {
        rosterPositionRepository.saveAll(listOf(babe, getTestRosterPosition("Mickey", "Mantle")))
        val getResponse = withBypassToken().`when`().get("/rosterpositions")
                .then().statusCode(HttpStatus.SC_OK)
                .extract().response().jsonPath()

        assertEquals(2, getResponse.getList<ResponseTeam>("data").size)
    }

    @Test
    fun getCollectionWithIncluded() {
        rosterPositionRepository.saveAll(listOf(babe, getTestRosterPosition("Hank", "Aaron")))
        val getResponse = withBypassToken().`when`().get("/rosterpositions?include=player")
                .then().statusCode(HttpStatus.SC_OK)
                .extract().response().jsonPath()

        assertEquals(2, getResponse.getList<ResponseTeam>("included").size)
    }

    private fun getTestRosterPosition(first: String, last: String): RosterPositionEntity {
        val person = PersonEntity(first, last, userEntity)
        personRepository.save(person)
        return RosterPositionEntity(teamRepository.findAll().first(), person, RandomUtils.nextInt(1, 99), LocalDate.now().minusYears(5).toPersistence(), LocalDate.now().toPersistence(), userEntity)


    }

    private fun babeRequest(): RequestRosterPosition {
        val attributes = RosterPositionAttributes(3, LocalDate.now())
        val relationships = RosterPositionRelationships(babe.team.id, babe.player.id)
        return RequestRosterPosition("rosterpositions", attributes, relationships)
    }
}