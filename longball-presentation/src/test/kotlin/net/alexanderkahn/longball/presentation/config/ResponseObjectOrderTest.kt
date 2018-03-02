package net.alexanderkahn.longball.presentation.config

import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.databind.BeanDescription
import net.alexanderkahn.service.commons.model.response.body.CollectionResponse
import net.alexanderkahn.service.commons.model.response.body.ObjectResponse
import net.alexanderkahn.service.commons.model.response.body.data.ResourceObject
import net.alexanderkahn.service.commons.model.response.body.meta.CollectionResponseMeta
import net.alexanderkahn.service.commons.model.response.body.meta.ObjectResponseMeta
import org.junit.jupiter.api.Test
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties
import kotlin.test.assertEquals
import kotlin.test.fail


class ResponseObjectOrderTest {

    @Test
    internal fun objectResponseDataInCorrectOrder() {
        val expectedOrder = listOf("meta", "data", "included")
        assertAllPropertiesOrdered(ObjectResponse::class, expectedOrder)
    }

    @Test
    internal fun collectionResponseDataInCorrectOrder() {
        val expectedOrder = listOf("meta", "data", "included")
        assertAllPropertiesOrdered(CollectionResponse::class, expectedOrder)
    }

    @Test
    internal fun resourceObjectDataInCorrectOrder() {
        val expectedOrder = listOf("type", "id", "meta", "attributes", "relationships")
        assertAllPropertiesOrdered(ResourceObject::class, expectedOrder)

    }

    @Test
    internal fun objectMetaInCorrectOrder() {
        val expectedOrder = listOf("status", "time")
        assertAllPropertiesOrdered(ObjectResponseMeta::class, expectedOrder)
    }

    @Test
    internal fun collectionMetaInCorrectOrder() {
        val expectedOrder = listOf("status", "time", "page")
        assertAllPropertiesOrdered(CollectionResponseMeta::class, expectedOrder)

    }

    private fun assertAllPropertiesOrdered(annotatedClazz: KClass<*>, expectedOrder: List<String>) {
        val propertyConfiguration = getPropertyOrderAnnotation(annotatedClazz)
        assertEquals(annotatedClazz.memberProperties.map { it.name }.toSet(), propertyConfiguration.value.toSet())
        assertEquals(expectedOrder, propertyConfiguration.value.toList())
    }

    private fun getPropertyOrderAnnotation(annotatedClazz: KClass<*>): JsonPropertyOrder {
        val mapper = JsonObjectMapper()
        val type = mapper.typeFactory.constructType(annotatedClazz.java)
        val desc = mapper.serializationConfig.introspect<BeanDescription>(type)
        val jpo = desc.classAnnotations.get(JsonPropertyOrder::class.java)
        return jpo ?: fail("Expected a JsonPropertyOrder configuration for class: ${annotatedClazz.simpleName}")
    }

}