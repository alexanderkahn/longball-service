package net.alexanderkahn.longball.rest.controller


import net.alexanderkahn.longball.model.LeagueAttributes
import net.alexanderkahn.longball.model.RequestLeague
import net.alexanderkahn.longball.model.ResponseLeague
import net.alexanderkahn.longball.rest.AbstractBypassTokenIntegrationTest
import net.alexanderkahn.longball.core.entity.LeagueEntity
import net.alexanderkahn.longball.core.repository.LeagueRepository
import net.alexanderkahn.longball.core.service.SpecificationBuilder
import net.alexanderkahn.service.commons.model.request.body.ObjectRequest
import net.alexanderkahn.service.commons.model.response.body.error.ResponseError
import org.apache.commons.lang3.RandomStringUtils
import org.apache.http.HttpStatus
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class LeagueControllerIntegrationTest : AbstractBypassTokenIntegrationTest() {

    @Autowired lateinit var leagueRepository: LeagueRepository

    @AfterEach
    fun tearDown() = clearRepositories(leagueRepository)

    @Test
    fun getLeagues() {
        IntRange(0, 5 - 1).forEach { leagueRepository.save(LeagueEntity(RandomStringUtils.randomAlphabetic(5), userEntity)) }
        val response = withBypassToken()
                .`when`().get("/leagues")
                .then().statusCode(HttpStatus.SC_OK)
                .extract().response()
        val leagues: List<ResponseLeague> = response.jsonPath().getList<ResponseLeague>("data")
        assertEquals(5, leagues.size)
        assertNotNull(response.jsonPath().getString("data[0].attributes.name"))
    }

    @Test
    fun getLeague() {
        val league = LeagueEntity(RandomStringUtils.randomAlphabetic(5), userEntity)
        leagueRepository.save(league)

        val responseBody = withBypassToken()
                .`when`().get("/leagues/${league.id}")
                .then().statusCode(HttpStatus.SC_OK)
                .extract().jsonPath()

        assertEquals(league.id, responseBody.getUUID("data.id"))
        assertEquals(league.name, responseBody.getString("data.attributes.name"))
    }

    @Test
    fun deleteLeague() {
        val league = LeagueEntity(RandomStringUtils.randomAlphabetic(5), userEntity)
        leagueRepository.save(league)

        withBypassToken()
                .`when`().delete("/leagues/${league.id}")
                .then().statusCode(HttpStatus.SC_OK)

        withBypassToken()
                .`when`().get("/leagues/${league.id}")
                .then().statusCode(HttpStatus.SC_NOT_FOUND)
    }

    @Nested inner class PostLeague {

        @Test
        fun validData() {
            val league = RequestLeague("leagues", LeagueAttributes(RandomStringUtils.randomAlphabetic(10)))
            val response = withBypassToken().body(ObjectRequest(league))
                    .`when`().post("/leagues")
                    .then().statusCode(HttpStatus.SC_CREATED)
                    .extract().response()
            val leagueId = response.jsonPath().getUUID("data.id")
            assertTrue(leagueRepository.existsById(leagueId))
        }

        @Test
        fun invalidType() {
            val response = withBypassToken().body(ObjectRequest(RequestLeague("otherType", LeagueAttributes("name"))))
                    .`when`().post("/leagues")
                    .then().statusCode(HttpStatus.SC_CONFLICT)
                    .extract().jsonPath()
            val errors = response.getList("errors", ResponseError::class.java)
            assertEquals(1, errors.size)
            assertEquals(HttpStatus.SC_CONFLICT.toString(), errors[0].status.statusCode)
        }

        @Test
        fun invalidAttributeLength() {
            val response = withBypassToken().body(ObjectRequest(RequestLeague("leagues", LeagueAttributes("n"))))
                    .`when`().post("/leagues")
                    .then().statusCode(HttpStatus.SC_BAD_REQUEST)
                    .extract().jsonPath()
            val errors = response.getList("errors", ResponseError::class.java)
            assertEquals(1, errors.size)
            assertEquals(HttpStatus.SC_BAD_REQUEST.toString(), errors[0].status.statusCode)
        }

        @Test
        fun invalidTypeAndAttributeLength() {
            val response = withBypassToken().body(ObjectRequest(RequestLeague("otherType", LeagueAttributes("n"))))
                    .`when`().post("/leagues")
                    .then().statusCode(HttpStatus.SC_BAD_REQUEST)
                    .extract().jsonPath()
            val errors = response.getList("errors", ResponseError::class.java)
            assertEquals(2, errors.size)
            assertEquals(HttpStatus.SC_BAD_REQUEST.toString(), errors[0].status.statusCode)
            assertEquals(HttpStatus.SC_CONFLICT.toString(), errors[1].status.statusCode)
        }
    }
}