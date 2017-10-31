package net.alexanderkahn.longball.presentation.rest.controller

import net.alexanderkahn.longball.provider.service.UserService
import net.alexanderkahn.service.base.api.auth.JwtAuthentication
import net.alexanderkahn.service.base.api.auth.JwtCredentials
import net.alexanderkahn.service.base.api.exception.InvalidStateException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.security.core.context.SecurityContextHolder

internal class UserControllerTest {

    private val userService = UserService()
    private val subject = UserController(userService)

    @Nested
    inner class WithCurrentUser {

        private val currentUserCredentials = JwtCredentials("testIssuer", "tokenBypassUser")

        @BeforeEach
        fun setCurrentUser() {
            SecurityContextHolder.getContext().authentication = JwtAuthentication(currentUserCredentials, true)
        }

        @Test
        fun returnsCurrentUser() {
            assertEquals("users", subject.getUser().body.data.type)
            assertEquals(currentUserCredentials.issuer, subject.getUser().body.data.attributes.issuer)
            assertEquals(currentUserCredentials.username, subject.getUser().body.data.attributes.username)
        }
    }

    @Nested
    inner class WithoutUser {

        @BeforeEach
        fun clearCurrentUser() {
            SecurityContextHolder.getContext().authentication = null
        }

        @Test()
        fun throwsException() {
            val exception = assertThrows(InvalidStateException::class.java, { subject.getUser() })
            assertEquals("No current user set", exception.message)
        }
    }
}