package net.alexanderkahn.longball.presentation.config

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import net.alexanderkahn.service.commons.model.response.body.CollectionResponse
import net.alexanderkahn.service.commons.model.response.body.ObjectResponse
import net.alexanderkahn.service.commons.model.response.body.data.ResourceObject
import net.alexanderkahn.service.commons.model.response.body.meta.CollectionResponseMeta
import net.alexanderkahn.service.commons.model.response.body.meta.ObjectResponseMeta
import org.springframework.stereotype.Component

@Component
open class JsonObjectMapper : ObjectMapper() {

    init {
        configureMapper()
        configureMixins()
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

    private fun configureMixins() {
        configureResponseObjectPropertyOrder()
    }

    private fun configureResponseObjectPropertyOrder() {
        // Set the order on response objects so they're consistent across the application
        // This doesn't matter in terms of application contract, but it's a nice convenience for human readers
        addMixIn(ResourceObject::class.java, PropertyOrderMixins.ResourceObjectMixin::class.java)
        addMixIn(ObjectResponse::class.java, PropertyOrderMixins.OkResponseMixin::class.java)
        addMixIn(CollectionResponse::class.java, PropertyOrderMixins.OkResponseMixin::class.java)
        addMixIn(ObjectResponseMeta::class.java, PropertyOrderMixins.ObjectResponseMetaMixin::class.java)
        addMixIn(CollectionResponseMeta::class.java, PropertyOrderMixins.CollectionResponseMetaMixin::class.java)
    }

    private class PropertyOrderMixins {

        @JsonPropertyOrder("type", "id", "meta", "attributes", "relationships")
        abstract class ResourceObjectMixin

        @JsonPropertyOrder("meta", "data", "included")
        abstract class OkResponseMixin

        @JsonPropertyOrder("status", "time")
        abstract class ObjectResponseMetaMixin

        @JsonPropertyOrder("status", "time", "page")
        abstract class CollectionResponseMetaMixin
    }
}
