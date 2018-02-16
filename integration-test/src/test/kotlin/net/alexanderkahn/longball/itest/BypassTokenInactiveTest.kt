package net.alexanderkahn.longball.itest

import junit.framework.TestCase.assertTrue
import net.alexanderkahn.service.commons.firebaseauth.jws.filter.config.FirebaseJwsConfig
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension::class)
@ActiveProfiles("test")
class BypassTokenInactiveTest(@Autowired private val config: FirebaseJwsConfig) {

    @Test
    fun bypassTokenConfigurationEmpty() {
        assertTrue("!!!UNSAFE CONFIGURATION!!! When the bypassToken profile is not active, " +
                "we should not be able to use the bypassToken", config.bypassToken?.token?.isBlank() ?: true)
    }
}