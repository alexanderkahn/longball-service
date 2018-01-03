package net.alexanderkahn.longball.presentation.rest.helper

import net.alexanderkahn.service.base.model.exception.BadRequestException
import net.alexanderkahn.service.base.model.request.filter.RequestResourceFilter
import net.alexanderkahn.service.base.model.request.filter.RequestResourceSearch
import net.alexanderkahn.service.base.model.request.filter.SEARCH_WILDCARD_SPACE
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

//TODO: probably need a try for the UUID conversion
fun getFilters(queryParams: MultiValueMap<String, String>?, allowedFields: Set<String>): Collection<RequestResourceFilter> {
    val filters = queryParams
            ?.filter { it.key.startsWith(filterParamStart) && it.key.endsWith(filterParamEnd) && it.value.isNotEmpty() }
            ?.mapKeys { it.key.removePrefix(filterParamStart).removeSuffix(filterParamEnd) }
            ?.map { RequestResourceFilter(it.key, it.value.map { UUID.fromString(it) }) } ?: emptyList()
    assertValidFilters(filters, allowedFields)
    return filters
}

fun getSearch(queryParams: MultiValueMap<String, String>?, allowedFields: Collection<String>): RequestResourceSearch? {
    val searches = queryParams
            ?.filter { it.key.startsWith(searchParamStart) && it.key.endsWith(searchParamEnd) && it.value.isNotEmpty() }
            ?.mapKeys {it.key.removePrefix(searchParamStart).removeSuffix(searchParamEnd).split(valueSeparator) }
            ?.map { RequestResourceSearch(it.key, it.value.single()) } ?: emptyList()
    assertValidSearches(searches, allowedFields)
    return searches.singleOrNull()
}

//TODO: should this throw an exception or just ignore the param?
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