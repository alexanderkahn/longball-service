package net.alexanderkahn.longball.api.model

import net.alexanderkahn.service.commons.model.request.validation.ExpectedType
import net.alexanderkahn.service.commons.model.response.body.data.RelationshipObject
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.*
import javax.validation.Validation
import javax.validation.Validator
import kotlin.test.assertEquals

internal class ResponseTeamTest {
    @Nested
    inner class Validates {

        private lateinit var validator: Validator

        @BeforeEach
        internal fun setUp() {
            val factory = Validation.buildDefaultValidatorFactory()
            this.validator = factory.validator
        }

        @Test
        internal fun validRequest() {
            val team = RequestTeam("teams", TeamAttributes("TST", "Test", "team"), TeamRelationships(RelationshipObject("leagues", UUID.randomUUID())))
            val violations = validator.validate(team)
            assertEquals(0, violations.size)
        }

        @Test
        internal fun incorrectType() {
            val invalidType = "tams"
            val team = RequestTeam(invalidType, TeamAttributes("TST", "Test", "team"), TeamRelationships(RelationshipObject("leagues", UUID.randomUUID())))
            val violations = validator.validate(team)
            assertEquals(1, violations.size)
            assertEquals(ExpectedType::class, violations.first().constraintDescriptor.annotation.annotationClass)
        }

        @Test
        internal fun incorrectLeagueRelationshipType() {
            val invalidType = "large"
            val team = RequestTeam("teams", TeamAttributes("TST", "Test", "team"), TeamRelationships(RelationshipObject(invalidType, UUID.randomUUID())))
            val violations = validator.validate(team)
            assertEquals(1, violations.size)
            assertEquals(ExpectedType::class, violations.first().constraintDescriptor.annotation.annotationClass)
        }
    }
}