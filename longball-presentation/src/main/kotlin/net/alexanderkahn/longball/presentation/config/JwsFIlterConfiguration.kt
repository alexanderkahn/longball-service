package net.alexanderkahn.longball.presentation.config

import net.alexanderkahn.service.commons.firebaseauth.jws.filter.JwsAuthenticationFilter
import net.alexanderkahn.service.commons.firebaseauth.jws.filter.config.FirebaseJwsConfig
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component

@EnableConfigurationProperties
@Configuration
open class JwsFIlterConfiguration {

    @Bean
    open fun jwsAuthenticationFilter(objectMapper: JsonObjectMapper, config: LongballFirebaseJwsConfig): JwsAuthenticationFilter {
        return JwsAuthenticationFilter(objectMapper, config)
    }

    //Everything's mutable, because Spring Boot can't get configuration properties into constructors yet.
    //Hopefully fixed for SB 2.1. See: https://github.com/spring-projects/spring-boot/issues/8762
    @ConfigurationProperties(prefix = "oauth")
    @Component
    open class LongballFirebaseJwsConfig : FirebaseJwsConfig {

        override var issuer = object : FirebaseJwsConfig.JwsIssuerConfig {
            override var algorithm = ""
            override var claims = object: FirebaseJwsConfig.JwsIssuerConfig.Claims {
                override var audience = ""
                override var issuer = ""
            }
            override var  keystore = object: FirebaseJwsConfig.JwsIssuerConfig.Keystore {
                override var url = ""
            }
        }
        override var bypassToken = object : FirebaseJwsConfig.BypassTokenConfig {
            override var token = ""
        }
        override var unauthenticatedPaths = mutableSetOf<String>()
    }
}