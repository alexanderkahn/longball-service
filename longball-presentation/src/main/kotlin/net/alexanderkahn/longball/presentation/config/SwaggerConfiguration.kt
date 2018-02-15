package net.alexanderkahn.longball.presentation.config

import net.alexanderkahn.service.commons.firebaseauth.jws.filter.config.UnauthenticatedPath
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2


@Configuration
@EnableSwagger2
open class SwaggerConfiguration {
    @Bean
    open fun productApi(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
                .select().apis(RequestHandlerSelectors.basePackage("net.alexanderkahn.longball.presentation.rest"))
                .build()
    }

    @Bean
    open fun swaggerUiPathAuthenticationExclusion(): UnauthenticatedPath {
        return UnauthenticatedPath("/swagger-ui.*")
    }

    @Bean
    open fun swaggerUiResourcesAuthenticationExclusion(): UnauthenticatedPath {
        return UnauthenticatedPath("/webjars/springfox-swagger-ui/.*")
    }

    @Bean
    open fun swaggerUiConfigurationAuthenticationExclusion(): UnauthenticatedPath {
        return UnauthenticatedPath("/swagger-resources.*")
    }

    @Bean
    open fun swaggerDocsAuthenticationException(): UnauthenticatedPath {
        return UnauthenticatedPath("/v2/api-docs")
    }
}
