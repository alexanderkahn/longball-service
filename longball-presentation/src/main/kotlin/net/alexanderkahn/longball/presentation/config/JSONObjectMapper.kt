package net.alexanderkahn.longball.presentation.config

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*

@Component
open class JsonObjectMapper(
        @Autowired(required = false) mixins: Optional<Collection<JsonMixin>>) : ObjectMapper() {

    private val logger = LoggerFactory.getLogger(JsonObjectMapper::class.java)

    init {
        configureMapper()
        mixins.ifPresent({ this.configureMixins(it) })
    }

    private fun configureMapper() {
        registerModule(KotlinModule())
        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        configureDateTimeMapping()
        setSerializationInclusion(JsonInclude.Include.NON_NULL)
    }

    private fun configureDateTimeMapping() {
        registerModule(JavaTimeModule())
        configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
    }

    private fun configureMixins(mixins: Collection<JsonMixin>) {
        for ((target, source) in mixins) {
            addMixIn(target, source)
        }

        logger.info("Configured REST mapping with mixins: " +
                "[${mixins.joinToString { "${it::class.simpleName}[${it.source} -> ${it.target}]" }}]")
    }
}


//TODO: create a mixin for ResponseResourceObject
//@JsonPropertyOrder("type", "id", "meta", "attributes", "relationships")
data class JsonMixin(val target: Class<*>, val source: Class<*>)