package net.alexanderkahn.longball.rest.helper

import net.alexanderkahn.longball.api.exception.BadRequestException
import net.alexanderkahn.service.commons.model.request.parameter.RequestResourceFilter
import net.alexanderkahn.service.commons.model.request.parameter.RequestResourceSearch
import net.alexanderkahn.service.commons.model.request.parameter.SEARCH_WILDCARD_SPACE
import net.alexanderkahn.service.commons.model.response.body.data.RelationshipObject
import org.springframework.util.MultiValueMap
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.javaType

val filterParamStart = "filter["
val filterParamEnd = "]"

val searchParamStart = "search["
val searchParamEnd = "]"

val valueSeparator = ","

fun getSearchableFieldsFor(clazz: KClass<*>): Collection<String> {
    return clazz.memberProperties.filter { it.returnType.javaType == String::class.java }
            .map { it.name }
            .plus(SEARCH_WILDCARD_SPACE)
}

fun getFilterableFieldsFor(clazz: KClass<*>): Collection<String> {
    return clazz.memberProperties.filter { it.returnType.javaType == RelationshipObject::class.java }
            .map { it.name }
}

fun getFilters(queryParams: MultiValueMap<String, String>?, allowedFields: Collection<String>): Collection<RequestResourceFilter> {
    val filters = queryParams
            ?.filter { it.key.startsWith(filterParamStart) && it.key.endsWith(filterParamEnd) && it.value.isNotEmpty() }
            ?.mapKeys { it.key.removePrefix(filterParamStart).removeSuffix(filterParamEnd) }
            ?.mapValues { it.value.flatMap { it.split(valueSeparator) } }
            ?.map { RequestResourceFilter(it.key, it.value.map { it.toUUID() }) } ?: emptyList()
    assertValidFilters(filters, allowedFields)
    return filters
}

fun String.toUUID(): UUID {
    try {
        return UUID.fromString(this)
    } catch (e: Exception) {
        throw BadRequestException("Invalid filter UUID: $this")
    }
}

fun getSearch(queryParams: MultiValueMap<String, String>?, allowedFields: Collection<String>): RequestResourceSearch? {
    val searches = queryParams
            ?.filter { it.key.startsWith(searchParamStart) && it.key.endsWith(searchParamEnd) && it.value.isNotEmpty() }
            ?.mapKeys {it.key.removePrefix(searchParamStart).removeSuffix(searchParamEnd).split(valueSeparator) }
            ?.map { RequestResourceSearch(it.key, it.value.single()) } ?: emptyList()
    assertValidSearches(searches, allowedFields)
    return searches.singleOrNull()
}

private fun assertValidFilters(filter: List<RequestResourceFilter>, validFields: Collection<String>) {
    filter.forEach {
        if (!validFields.contains(it.filterField)) {
            throw BadRequestException("Cannot parse filter field: ${it.filterField}")
        } else if (it.filterTerms.isEmpty()) {
            throw BadRequestException("Cannot filter field ${it.filterField} with no value")
        }
    }
}

private fun assertValidSearches(searches: List<RequestResourceSearch>, validFields: Collection<String>) {
    if (searches.size > 1) {
        throw BadRequestException("Only one search term may be specified per request")
    }
    searches.forEach {
        it.searchFields.forEach {
            if (!validFields.contains(it)) {
                throw BadRequestException("Cannot parse search field: $it")
            }
        }

        if (it.searchTerm.isEmpty()) {
            throw BadRequestException("Cannot parse empty search term for fields ${it.searchFields}")
        }
    }
}