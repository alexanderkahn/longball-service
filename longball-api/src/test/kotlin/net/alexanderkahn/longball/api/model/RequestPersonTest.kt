package net.alexanderkahn.longball.api.model

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import javax.validation.Validation
import javax.validation.Validator
import kotlin.test.assertEquals

internal class RequestPersonTest {

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
            val person = RequestPerson("people", PersonAttributes("hello", "there"))
            val violations = validator.validate(person)
            assertEquals(0, violations.size)
        }

        @Test
        internal fun invalidType() {
            val person = RequestPerson("purple", PersonAttributes("hello", "there"))
            val violations = validator.validate(person)
            assertEquals(1, violations.size)
        }
    }
}