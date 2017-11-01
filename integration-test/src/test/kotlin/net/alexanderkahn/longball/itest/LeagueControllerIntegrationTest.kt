package net.alexanderkahn.longball.itest


import junit.framework.TestCase.*
import net.alexanderkahn.longball.model.dto.LeagueAttributes
import net.alexanderkahn.longball.model.dto.RequestLeague
import net.alexanderkahn.longball.model.dto.ResponseLeague
import net.alexanderkahn.longball.provider.entity.LeagueEntity
import net.alexanderkahn.longball.provider.repository.LeagueRepository
import net.alexanderkahn.service.base.model.request.ObjectRequest
import org.apache.commons.lang3.RandomStringUtils
import org.apache.http.HttpStatus
import org.junit.After
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class LeagueControllerIntegrationTest : AbstractBypassTokenIntegrationTest() {

    @Autowired lateinit var leagueRepository: LeagueRepository

    @After
    fun tearDown() {
        leagueRepository.deleteAll()
    }

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
    fun postLeague() {
        val league = RequestLeague("leagues", LeagueAttributes(RandomStringUtils.randomAlphabetic(10)))
        val response = withBypassToken().body(ObjectRequest(league))
                .`when`().post("/leagues")
                .then().statusCode(HttpStatus.SC_CREATED)
                .extract().response()
        val leagueId = response.jsonPath().getUUID("data.id")
        assertTrue(leagueRepository.exists(leagueId))
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

    @Test
    fun addLeagueWrongType() {
        withBypassToken().body(ObjectRequest(RequestLeague("otherType", LeagueAttributes("name"))))
                .`when`().post("/leagues")
                .then().statusCode(HttpStatus.SC_CONFLICT)
    }
}