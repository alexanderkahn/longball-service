package net.alexanderkahn.longball.presentation.rest.controller

import net.alexanderkahn.longball.provider.entity.UserEntity
import net.alexanderkahn.longball.provider.repository.UserRepository
import net.alexanderkahn.longball.provider.service.UserService
import net.alexanderkahn.service.commons.firebaseauth.jws.JwsAuthentication
import net.alexanderkahn.service.commons.firebaseauth.jws.JwsCredentials
import net.alexanderkahn.service.commons.firebaseauth.jws.JwsUserDetails
import net.alexanderkahn.service.commons.model.exception.InvalidStateException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.security.core.context.SecurityContextHolder

internal class UserControllerTest {

    private val userRepository = mock(UserRepository::class.java)
    private val userService = UserService(userRepository)
    private val subject = UserController(userService)

    @Nested
    inner class WithCurrentUser {

        private val currentUserCredentials = JwsCredentials("testIssuer", "tokenBypassUser")
        private val currentUserDetails = JwsUserDetails("Test User")

        @BeforeEach
        fun setCurrentUser() {
            val userEntity = UserEntity(currentUserCredentials.issuer, currentUserCredentials.username, currentUserDetails.displayName)
            SecurityContextHolder.getContext().authentication = JwsAuthentication(currentUserCredentials, currentUserDetails,true)
            `when`(userRepository.existsByIssuerAndUsername(anyString(), anyString())).thenReturn(true)
            `when`(userRepository.findOneByIssuerAndUsername(anyString(), anyString())).thenReturn(userEntity)
        }

        @Test
        fun returnsCurrentUser() {
            assertEquals("users", subject.getUser().data.type)
            assertEquals(currentUserCredentials.issuer, subject.getUser().data.attributes.issuer)
            assertEquals(currentUserCredentials.username, subject.getUser().data.attributes.username)
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