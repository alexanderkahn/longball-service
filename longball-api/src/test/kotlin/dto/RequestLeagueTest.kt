package dto

import net.alexanderkahn.longball.api.model.LeagueAttributes
import net.alexanderkahn.longball.api.model.RequestLeague
import net.alexanderkahn.service.commons.model.request.validation.ExpectedType
import org.apache.commons.lang3.RandomStringUtils
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import javax.validation.Validation
import javax.validation.Validator
import javax.validation.constraints.Size
import kotlin.test.assertEquals


internal class RequestLeagueTest {

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
            val league = RequestLeague("leagues", LeagueAttributes("hello there"))
            val violations = validator.validate(league)
            assertEquals(0, violations.size)
        }

        @Test
        internal fun incorrectType() {
            val invalidType = "large"
            val league = RequestLeague(invalidType, LeagueAttributes("hello there"))
            val violations = validator.validate(league)
            assertEquals(1, violations.size)
            assertEquals(ExpectedType::class, violations.first().constraintDescriptor.annotation.annotationClass)
        }

        @Test
        internal fun belowMinimumLength() {
            val invalidName = RandomStringUtils.randomAlphabetic(2)
            val league = RequestLeague("leagues", LeagueAttributes(invalidName))
            val violations = validator.validate(league)
            assertEquals(1, violations.size)
            assertEquals(Size::class, violations.first().constraintDescriptor.annotation.annotationClass)
        }

        @Test
        internal fun aboveMaximumLength() {
            val invalidName = RandomStringUtils.randomAlphabetic(300)
            val league = RequestLeague("leagues", LeagueAttributes(invalidName))
            val violations = validator.validate(league)
            assertEquals(1, violations.size)
            assertEquals(Size::class, violations.first().constraintDescriptor.annotation.annotationClass)
        }
    }
}