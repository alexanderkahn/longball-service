package net.alexanderkahn.longball.rest.config

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

//This allows us to use the JwsAuthenticationFilter to handle verification. Kind of messy, huh?
@Configuration
@EnableWebSecurity
open class WebSecurityConfig: WebSecurityConfigurerAdapter() {
    override fun configure(http: HttpSecurity?) {
        http?.headers()?.cacheControl() //this disables the basic-auth login BS that you get with spring security
        http?.csrf()?.disable() //TODO: this leaves me open to an attack I guess!
    }
}