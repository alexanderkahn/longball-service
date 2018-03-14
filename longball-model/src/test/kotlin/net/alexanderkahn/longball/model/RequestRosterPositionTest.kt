package net.alexanderkahn.longball.model

import net.alexanderkahn.service.commons.model.response.body.data.RelationshipObject
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.util.*
import javax.validation.Validation
import javax.validation.Validator
import javax.validation.constraints.Min
import kotlin.test.assertEquals

internal class RequestRosterPositionTest {
    @Nested
    inner class Validates {

        private lateinit var validator: Validator

        private val validPosition = RequestRosterPosition(
                "rosterpositions",
                RosterPositionAttributes(1, LocalDate.now()),
                RosterPositionRelationships(RelationshipObject("teams", UUID.randomUUID()), RelationshipObject("people", UUID.randomUUID()))
        )

        @BeforeEach
        internal fun setUp() {
            val factory = Validation.buildDefaultValidatorFactory()
            this.validator = factory.validator
        }

        @Test
        internal fun validRequest() {

            val violations = validator.validate(validPosition)
            assertEquals(0, violations.size)
        }

        @Test
        internal fun invalidType() {
            val invalidType = "romperportions"
            val position = RequestRosterPosition(
                    invalidType,
                    RosterPositionAttributes(1, LocalDate.now()),
                    RosterPositionRelationships(RelationshipObject("teams", UUID.randomUUID()), RelationshipObject("people", UUID.randomUUID()))
            )
            val violations = validator.validate(position)
            assertEquals(1, violations.size)
        }

        @Test
        internal fun invalidTeamType() {
            val invalidType = "tram"
            val position = RequestRosterPosition(
                    "rosterpositions",
                    RosterPositionAttributes(1, LocalDate.now()),
                    RosterPositionRelationships(RelationshipObject(invalidType, UUID.randomUUID()), RelationshipObject("people", UUID.randomUUID()))
            )
            val violations = validator.validate(position)
            assertEquals(1, violations.size)
        }

        @Test
        internal fun invalidPlayerType() {
            val invalidType = "plural"
            val position = RequestRosterPosition(
                    "rosterpositions",
                    RosterPositionAttributes(1, LocalDate.now()),
                    RosterPositionRelationships(RelationshipObject("teams", UUID.randomUUID()), RelationshipObject(invalidType, UUID.randomUUID()))
            )
            val violations = validator.validate(position)
            assertEquals(1, violations.size)
        }

        @Test
        internal fun invalidJerseyNumber() {
            val invalidPosition = validPosition.copy(attributes = validPosition.attributes.copy(jerseyNumber = -1))
            val violations = validator.validate(invalidPosition)
            assertEquals(1, violations.size)
            assertEquals(Min::class, violations.first().constraintDescriptor.annotation.annotationClass)
        }
    }
}