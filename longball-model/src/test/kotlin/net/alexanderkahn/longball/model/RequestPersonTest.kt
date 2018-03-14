package net.alexanderkahn.longball.model

import net.alexanderkahn.service.commons.model.request.validation.ExpectedType
import org.apache.commons.lang3.RandomStringUtils
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import javax.validation.Validation
import javax.validation.Validator
import javax.validation.constraints.Size
import kotlin.test.assertEquals

internal class RequestPersonTest {

    @Nested
    inner class Validates {

        private lateinit var validator: Validator
        private val validPerson = RequestPerson("people", PersonAttributes("hello", "there"))

        @BeforeEach
        internal fun setUp() {
            val factory = Validation.buildDefaultValidatorFactory()
            this.validator = factory.validator
        }

        @Test
        internal fun validRequest() {
            val violations = validator.validate(validPerson)
            assertEquals(0, violations.size)
        }

        @Test
        internal fun invalidType() {
            val invalidPerson = validPerson.copy(type = "purple")
            val violations = validator.validate(invalidPerson)
            assertEquals(1, violations.size)
            assertEquals(ExpectedType::class, violations.first().constraintDescriptor.annotation.annotationClass)
        }

        @Test
        internal fun invalidFirstNameLength() {
            val invalidPerson = validPerson.copy(attributes = validPerson.attributes.copy(first = RandomStringUtils.randomAlphabetic(260)))
            val violations = validator.validate(invalidPerson)
            assertEquals(1, violations.size)
            assertEquals(Size::class, violations.first().constraintDescriptor.annotation.annotationClass)
        }

        @Test
        internal fun invalidLastNameLength() {
            val invalidPerson = validPerson.copy(attributes = validPerson.attributes.copy(last = RandomStringUtils.randomAlphabetic(1)))
            val violations = validator.validate(invalidPerson)
            assertEquals(1, violations.size)
            assertEquals(Size::class, violations.first().constraintDescriptor.annotation.annotationClass)
        }
    }
}