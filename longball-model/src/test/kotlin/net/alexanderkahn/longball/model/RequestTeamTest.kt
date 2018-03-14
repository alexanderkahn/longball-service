package net.alexanderkahn.longball.model

import net.alexanderkahn.service.commons.model.request.validation.ExpectedType
import net.alexanderkahn.service.commons.model.response.body.data.RelationshipObject
import org.apache.commons.lang3.RandomStringUtils
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.*
import javax.validation.Validation
import javax.validation.Validator
import javax.validation.constraints.Size
import kotlin.test.assertEquals

internal class RequestTeamTest {

    @Nested
    inner class Validates {

        private val validTeam = RequestTeam("teams", TeamAttributes("TST", "Test", "team"), TeamRelationships(RelationshipObject("leagues", UUID.randomUUID())))

        private lateinit var validator: Validator

        @BeforeEach
        internal fun setUp() {
            val factory = Validation.buildDefaultValidatorFactory()
            this.validator = factory.validator
        }

        @Test
        internal fun validRequest() {
            val violations = validator.validate(validTeam)
            assertEquals(0, violations.size)
        }

        @Test
        internal fun incorrectType() {
            val invalidTeam = validTeam.copy(type = "tams")
            val violations = validator.validate(invalidTeam)
            assertEquals(1, violations.size)
            assertEquals(ExpectedType::class, violations.first().constraintDescriptor.annotation.annotationClass)
        }

        @Test
        internal fun incorrectLeagueRelationshipType() {
            val invalidTeam = validTeam.copy(relationships = validTeam.relationships.copy(league = RelationshipObject("large", UUID.randomUUID())))
            val violations = validator.validate(invalidTeam)
            assertEquals(1, violations.size)
            assertEquals(ExpectedType::class, violations.first().constraintDescriptor.annotation.annotationClass)
        }

        @Test
        internal fun invalidAbbreviationLength() {
            val invalidTeam = validTeam.copy(attributes = validTeam.attributes.copy(abbreviation = RandomStringUtils.randomAlphabetic(2)))
            val violations = validator.validate(invalidTeam)
            assertEquals(1, violations.size)
            assertEquals(Size::class, violations.first().constraintDescriptor.annotation.annotationClass)
        }

        @Test
        internal fun invalidLocationLength() {
            val invalidTeam = validTeam.copy(attributes = validTeam.attributes.copy(location = RandomStringUtils.randomAlphabetic(26)))
            val violations = validator.validate(invalidTeam)
            assertEquals(1, violations.size)
            assertEquals(Size::class, violations.first().constraintDescriptor.annotation.annotationClass)
        }

        @Test
        internal fun invalidNicknameLength() {
            val invalidTeam = validTeam.copy(attributes = validTeam.attributes.copy(nickname = RandomStringUtils.randomAlphabetic(26)))
            val violations = validator.validate(invalidTeam)
            assertEquals(1, violations.size)
            assertEquals(Size::class, violations.first().constraintDescriptor.annotation.annotationClass)
        }
    }
}