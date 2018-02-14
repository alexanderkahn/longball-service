package net.alexanderkahn.longball.presentation.rest.controller

import net.alexanderkahn.longball.api.service.ILeagueService
import net.alexanderkahn.longball.model.dto.LeagueAttributes
import net.alexanderkahn.longball.model.dto.ModelTypes
import net.alexanderkahn.longball.model.dto.RequestLeague
import net.alexanderkahn.service.commons.model.exception.BadRequestException
import net.alexanderkahn.service.commons.model.exception.ConflictException
import net.alexanderkahn.service.commons.model.request.body.ObjectRequest
import org.apache.commons.lang3.RandomStringUtils
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock

internal class LeagueControllerTest {

    private val leagueService = mock(ILeagueService::class.java)
    private val subject = LeagueController(leagueService)

    @Nested
    inner class ValidatesIncomingRequests {

        @Test
        fun withWrongType() {
            val wrongTypeLeague = RequestLeague("lugs", LeagueAttributes(RandomStringUtils.randomAlphanumeric(20)))
            val exception = assertThrows(ConflictException::class.java, { subject.addLeague(ObjectRequest( wrongTypeLeague)) })
            assertTrue(exception.message!!.contains("Invalid type: lugs"))
        }

        @Test
        fun withShortName() {
            val shortNameLeague = RequestLeague(ModelTypes.LEAGUES.display, LeagueAttributes(""))
            val exception = assertThrows(BadRequestException::class.java, { subject.addLeague(ObjectRequest( shortNameLeague)) })
            assertTrue(exception.message!!.contains("Invalid value for field: name"))
        }

        @Test
        fun withLongName() {
            val shortNameLeague = RequestLeague(ModelTypes.LEAGUES.display, LeagueAttributes(RandomStringUtils.randomAlphanumeric(300)))
            val exception = assertThrows(BadRequestException::class.java, { subject.addLeague(ObjectRequest( shortNameLeague)) })
            assertTrue(exception.message!!.contains("Invalid value for field: name"))
        }
    }
}

