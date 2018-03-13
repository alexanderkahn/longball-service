package net.alexanderkahn.longball.provider.assembler

import net.alexanderkahn.longball.api.exception.InvalidRelationshipsException
import net.alexanderkahn.longball.model.RequestRosterPosition
import net.alexanderkahn.longball.model.RosterPositionAttributes
import net.alexanderkahn.longball.model.RosterPositionRelationships
import net.alexanderkahn.longball.provider.repository.PersonRepository
import net.alexanderkahn.longball.provider.repository.TeamRepository
import net.alexanderkahn.longball.provider.service.UserService
import net.alexanderkahn.longball.provider.test.TestSubjects
import net.alexanderkahn.service.commons.model.response.body.data.RelationshipObject
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import java.time.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class RosterPositionAssemblerTest {

    private val userService = mock(UserService::class.java)
    private val teamRepository = mock(TeamRepository::class.java)
    private val personRepository = mock(PersonRepository::class.java)
    private val subject = RosterPositionAssembler(userService, teamRepository, personRepository)

    private val position = TestSubjects.rickPosition
    private val team = TestSubjects.raccoons
    private val player = TestSubjects.rangerRick
    private val owner = TestSubjects.owner

    private val request = RequestRosterPosition(
            "rosterpositions",
            RosterPositionAttributes(4, LocalDate.now()),
            RosterPositionRelationships(team.id, player.id)
    )

    @BeforeEach
    fun setup() {
        `when`(userService.userEntity()).thenReturn(owner)
        `when`(teamRepository.findByIdAndOwner(position.team.id, owner)).thenReturn(team)
        `when`(personRepository.findByIdAndOwner(position.player.id, owner)).thenReturn(player)
    }

    @Test
    fun toEntity() {
        val result = subject.toEntity(request)
        assertEquals(request.relationships.player.data.id, result.player.id)
        assertEquals(request.relationships.team.data.id, result.team.id)
    }

    @Test
    fun notFoundPlayerThrowsMissingRelationshipException() {
        `when`(personRepository.findByIdAndOwner(position.player.id, owner)).thenReturn(null)
        val result = assertThrows(InvalidRelationshipsException::class.java, { subject.toEntity(request) })
        assertEquals(1, result.causes.size)
        assertTrue(result.causes.map { it.missingResource }.contains(RelationshipObject.RelationshipObjectIdentifier("people", position.player.id)))
    }

    @Test
    fun notFoundTeamThrowsMissingRelationshipException() {
        `when`(teamRepository.findByIdAndOwner(position.team.id, owner)).thenReturn(null)
        val result = assertThrows(InvalidRelationshipsException::class.java, { subject.toEntity(request) })
        assertEquals(1, result.causes.size)
        assertTrue(result.causes.map { it.missingResource }.contains(RelationshipObject.RelationshipObjectIdentifier("teams", position.team.id)))
    }

    @Test
    fun notFoundTeamAndPlayerReportsBothRelationships() {
        `when`(teamRepository.findByIdAndOwner(position.team.id, owner)).thenReturn(null)
        `when`(personRepository.findByIdAndOwner(position.player.id, owner)).thenReturn(null)
        val result = assertThrows(InvalidRelationshipsException::class.java, { subject.toEntity(request) })
        assertEquals(2, result.causes.size)
        assertTrue(result.causes.map { it.missingResource }.containsAll(setOf(
                RelationshipObject.RelationshipObjectIdentifier("teams", position.team.id),
                RelationshipObject.RelationshipObjectIdentifier("people", position.player.id)
        )))
    }
}