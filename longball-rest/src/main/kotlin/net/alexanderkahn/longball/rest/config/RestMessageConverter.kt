package net.alexanderkahn.longball.rest.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter

@Configuration
open class RestMessageConverter(@Autowired objectMapper: JsonObjectMapper) : MappingJackson2HttpMessageConverter() {
    init {
        this.objectMapper = objectMapper
    }
}