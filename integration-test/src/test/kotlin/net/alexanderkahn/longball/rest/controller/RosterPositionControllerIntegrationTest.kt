package net.alexanderkahn.longball.rest.controller

import junit.framework.TestCase
import junit.framework.TestCase.assertEquals
import net.alexanderkahn.longball.core.assembler.toPersistence
import net.alexanderkahn.longball.core.assembler.toResponse
import net.alexanderkahn.longball.core.entity.LeagueEntity
import net.alexanderkahn.longball.core.entity.PersonEntity
import net.alexanderkahn.longball.core.entity.RosterPositionEntity
import net.alexanderkahn.longball.core.entity.TeamEntity
import net.alexanderkahn.longball.core.repository.LeagueRepository
import net.alexanderkahn.longball.core.repository.PersonRepository
import net.alexanderkahn.longball.core.repository.RosterPositionRepository
import net.alexanderkahn.longball.core.repository.TeamRepository
import net.alexanderkahn.longball.model.*
import net.alexanderkahn.longball.rest.AbstractBypassTokenIntegrationTest
import net.alexanderkahn.service.commons.model.request.body.ObjectRequest
import net.alexanderkahn.service.commons.model.response.body.data.RelationshipObject
import org.apache.commons.lang3.RandomUtils
import org.apache.http.HttpStatus
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDate
import java.util.*

class RosterPositionControllerIntegrationTest : AbstractBypassTokenIntegrationTest() {

    @Autowired
    private lateinit var personRepository: PersonRepository
    @Autowired
    private lateinit var leagueRepository: LeagueRepository
    @Autowired
    private lateinit var teamRepository: TeamRepository
    @Autowired
    private lateinit var rosterPositionRepository: RosterPositionRepository

    private lateinit var team: TeamEntity
    private lateinit var league: LeagueEntity
    private lateinit var babe: RosterPositionEntity

    @BeforeEach
    fun setUp() {
        league = LeagueEntity("MLB", userEntity)
        team = TeamEntity(league, "HOU", "Houston", "Astros", userEntity) //that's right. Babe Ruth, the famous Astro.
        leagueRepository.save(league)
        teamRepository.save(team)

        babe = getTestRosterPosition("Babe", "Ruth")
    }

    @AfterEach
    fun tearDown() = clearRepositories(rosterPositionRepository, teamRepository, personRepository, leagueRepository)

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
        val badRequest = babeRequest().copy(type = "romperpartitions")
        withBypassToken().body(ObjectRequest(badRequest))
                .`when`().post("/rosterpositions")
                .then().statusCode(HttpStatus.SC_CONFLICT)
    }

    @Test
    fun postWrongRelationshipType() {
        val babeRequest = babeRequest()
        val badRequest = babeRequest.copy(
                relationships = babeRequest.relationships.copy(
                        player = RelationshipObject("pringle", babeRequest.relationships.player.data.id)
                )
        )
        withBypassToken().body(ObjectRequest(badRequest))
                .`when`().post("/rosterpositions")
                .then().statusCode(HttpStatus.SC_CONFLICT)
    }

    @Test
    fun postBadTeamId() {
        val babeRequest = babeRequest()
        val badRequest = babeRequest.copy(
                relationships = babeRequest.relationships.copy(
                        team = RelationshipObject(babeRequest.relationships.team.data.type, UUID.randomUUID())
                )
        )
        withBypassToken().body(ObjectRequest(badRequest))
                .`when`().post("/rosterpositions")
                .then().statusCode(HttpStatus.SC_NOT_FOUND)
    }

    @Test
    fun postBadPersonId() {
        val babeRequest = babeRequest()
        val badRequest = babeRequest.copy(
                relationships = babeRequest.relationships.copy(
                        player = RelationshipObject(babeRequest.relationships.player.data.type, UUID.randomUUID())
                )
        )
        withBypassToken().body(ObjectRequest(badRequest))
                .`when`().post("/rosterpositions")
                .then().statusCode(HttpStatus.SC_NOT_FOUND)
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
        val savedBabe = rosterPositionRepository.save(babe)
        val response = withBypassToken().`when`().get("/rosterpositions/${babe.id}?include=player")
                .then().statusCode(HttpStatus.SC_OK).extract().body().jsonPath().getObject("included[0]", ResponsePerson::class.java)
        assertEquals(savedBabe.player.toResponse(), response)
    }

    @Test
    fun getCollection() {
        listOf(babe, getTestRosterPosition("Mickey", "Mantle")).forEach { rosterPositionRepository.save(it) }
        val getResponse = withBypassToken().`when`().get("/rosterpositions")
                .then().statusCode(HttpStatus.SC_OK)
                .extract().response().jsonPath()

        assertEquals(2, getResponse.getList<ResponseTeam>("data").size)
    }

    @Test
    fun getCollectionWithIncluded() {
        listOf(babe, getTestRosterPosition("Hank", "Aaron")).forEach { rosterPositionRepository.save(it) }
        val getResponse = withBypassToken().`when`().get("/rosterpositions?include=player")
                .then().statusCode(HttpStatus.SC_OK)
                .extract().response().jsonPath()

        assertEquals(2, getResponse.getList<ResponseTeam>("included").size)
    }

    private fun getTestRosterPosition(first: String, last: String): RosterPositionEntity {
        val person = PersonEntity(first, last, userEntity)
        personRepository.save(person)
        return RosterPositionEntity(team, person, RandomUtils.nextInt(1, 99), LocalDate.now().minusYears(5).toPersistence(), LocalDate.now().toPersistence(), userEntity)


    }

    private fun babeRequest(): RequestRosterPosition {
        val attributes = RosterPositionAttributes(3, LocalDate.now())
        val relationships = RosterPositionRelationships(babe.team.id, babe.player.id)
        return RequestRosterPosition("rosterpositions", attributes, relationships)
    }
}