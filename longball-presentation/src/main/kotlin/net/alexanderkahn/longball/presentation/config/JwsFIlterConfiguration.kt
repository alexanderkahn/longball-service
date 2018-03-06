package net.alexanderkahn.longball.presentation.config

import net.alexanderkahn.service.commons.jwsauthenticator.jws.filter.ExceptionResponseWriter
import net.alexanderkahn.service.commons.jwsauthenticator.jws.filter.JwsAuthenticationFilter
import net.alexanderkahn.service.commons.jwsauthenticator.jws.filter.config.JwsConfig
import net.alexanderkahn.service.commons.model.response.body.ErrorsResponse
import net.alexanderkahn.service.commons.model.response.body.error.ResponseError
import net.alexanderkahn.service.commons.model.response.body.meta.ResourceStatus
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletResponse

@EnableConfigurationProperties
@Configuration
open class JwsFIlterConfiguration {

    @Bean
    open fun jwsAuthenticationFilter(responseWriter: RestErrorResponseWriter, config: LongballFirebaseJwsConfig): JwsAuthenticationFilter {
        return JwsAuthenticationFilter(responseWriter, config)
    }

    //Everything's mutable, because Spring Boot can't get configuration properties into constructors yet.
    //Hopefully fixed for SB 2.1. See: https://github.com/spring-projects/spring-boot/issues/8762
    @ConfigurationProperties(prefix = "oauth")
    @Component
    open class LongballFirebaseJwsConfig : JwsConfig {

        override var issuer = object : JwsConfig.JwsIssuerConfig {
            override var algorithm = ""
            override var claims = object: JwsConfig.JwsIssuerConfig.Claims {
                override var audience = ""
                override var issuer = ""
            }
            override var  keystore = object: JwsConfig.JwsIssuerConfig.Keystore {
                override var url = ""
            }
        }
        override var bypassToken = object : JwsConfig.BypassTokenConfig {
            override var token = ""
        }
        override var unauthenticatedPaths = mutableSetOf<String>()
    }

    @Component
    open class RestErrorResponseWriter(private val jsonObjectMapper: JsonObjectMapper) : ExceptionResponseWriter {
        override fun writeExceptionResponse(exception: Exception, response: ServletResponse) {
            val status = ResourceStatus.UNAUTHORIZED
            val payload = ErrorsResponse(ResponseError(status, exception))
            (response as? HttpServletResponse)?.apply {
                setStatus(status.statusCode)
                contentType = "application/json"
                characterEncoding = "UTF-8"
                writer.write(jsonObjectMapper.writeValueAsString(payload))
            }
        }
    }
}