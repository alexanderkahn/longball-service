package net.alexanderkahn.longball.rest.config

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
open class JsonObjectMapper : ObjectMapper() {

    @PostConstruct
    fun init() {
        registerModule(KotlinModule())
        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        configureDateTimeMapping()
        setSerializationInclusion(JsonInclude.Include.NON_NULL)
    }

    private fun configureDateTimeMapping() {
        registerModule(JavaTimeModule())
        configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
    }
}
