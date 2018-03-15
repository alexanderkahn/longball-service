package net.alexanderkahn.longball.core.repository

import net.alexanderkahn.longball.core.entity.LeagueEntity
import net.alexanderkahn.longball.core.entity.UserEntity
import net.alexanderkahn.service.commons.jwsauthenticator.jws.JwsAuthentication
import net.alexanderkahn.service.commons.jwsauthenticator.jws.JwsCredentials
import net.alexanderkahn.service.commons.jwsauthenticator.jws.JwsUserDetails
import org.apache.commons.lang3.RandomStringUtils
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.context.junit.jupiter.SpringExtension
import kotlin.test.assertTrue

@SpringBootTest
@ExtendWith(SpringExtension::class)
class LongballRepositoryIntegrationTest {

    @Autowired private lateinit var userRepository: UserRepository
    @Autowired private lateinit var leagueRepository: LeagueRepository

    private val user = UserEntity(RandomStringUtils.randomAlphanumeric(10), RandomStringUtils.randomAlphanumeric(10), "test user")
    private val league = LeagueEntity("name", user)

    @BeforeEach
    internal fun setUp() {
        userRepository.save(user)
        SecurityContextHolder.getContext().authentication = JwsAuthentication(JwsCredentials(user.issuer, user.username), JwsUserDetails(user.displayName), true)
    }

    @Test
    internal fun entitySaveUpdatesLastModifiedDate() {
        val savedLeague = leagueRepository.save(league)
        Thread.sleep(10) //sorry - time stuff, y'know?
        val updatedLeague = leagueRepository.save(savedLeague)
        assertTrue(updatedLeague.lastModified.isAfter(savedLeague.lastModified))
    }
}