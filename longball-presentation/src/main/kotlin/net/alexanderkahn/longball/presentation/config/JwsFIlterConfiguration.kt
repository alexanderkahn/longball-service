package net.alexanderkahn.longball.presentation.config

import net.alexanderkahn.service.commons.firebaseauth.jws.filter.ExceptionResponseWriter
import net.alexanderkahn.service.commons.firebaseauth.jws.filter.JwsAuthenticationFilter
import net.alexanderkahn.service.commons.firebaseauth.jws.filter.config.FirebaseJwsConfig
import net.alexanderkahn.service.commons.model.exception.UnauthenticatedException
import net.alexanderkahn.service.commons.model.response.body.ErrorResponse
import net.alexanderkahn.service.commons.model.response.body.error.ResponseError
import net.alexanderkahn.service.commons.model.response.body.meta.ObjectResponseMeta
import net.alexanderkahn.service.commons.model.response.body.meta.ResponseStatus
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

    @Component
    open class RestErrorResponseWriter(private val jsonObjectMapper: JsonObjectMapper) : ExceptionResponseWriter {
        override fun writeExceptionResponse(exception: Exception, response: ServletResponse) {
            val status = ResponseStatus.UNAUTHORIZED
            val payload = ErrorResponse(ObjectResponseMeta(status), ResponseError(UnauthenticatedException(exception.message.orEmpty())))
            (response as? HttpServletResponse)?.apply {
                setStatus(status.statusCode)
                contentType = "application/json"
                characterEncoding = "UTF-8"
                writer.write(jsonObjectMapper.writeValueAsString(payload))
            }
        }
    }
}