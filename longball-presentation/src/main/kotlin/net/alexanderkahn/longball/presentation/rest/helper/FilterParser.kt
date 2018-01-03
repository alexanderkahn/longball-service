package net.alexanderkahn.longball.presentation.rest.helper

import net.alexanderkahn.service.base.model.exception.BadRequestException
import net.alexanderkahn.service.base.model.request.RequestResourceFilter
import net.alexanderkahn.service.base.model.request.RequestResourceSearch
import org.springframework.util.MultiValueMap

val filterParamStart = "filter["
val filterParamEnd = "]"

val searchParamStart = "search["
val searchParamEnd = "]"

val valueSeparator = ","

fun getFilters(queryParams: MultiValueMap<String, String>?, allowedFields: Set<String>): List<RequestResourceFilter> {
    val filters = queryParams
            ?.filter { it.key.startsWith(filterParamStart) && it.key.endsWith(filterParamEnd) && it.value.isNotEmpty() }
            ?.mapKeys { it.key.removePrefix(filterParamStart).removeSuffix(filterParamEnd) }
            ?.map { RequestResourceFilter(it.key, it.value) } ?: emptyList()
    assertValidFilters(filters, allowedFields)
    return filters
}

fun getSearch(queryParams: MultiValueMap<String, String>?, allowedFields: Set<String>): RequestResourceSearch? {
    val searches = queryParams
            ?.filter { it.key.startsWith(searchParamStart) && it.key.endsWith(searchParamEnd) && it.value.isNotEmpty() }
            ?.mapKeys {it.key.removePrefix(searchParamStart).removeSuffix(searchParamEnd).split(valueSeparator) }
            ?.map { RequestResourceSearch(it.key, it.value.single()) } ?: emptyList()
    assertValidSearches(searches, allowedFields)
    return searches.singleOrNull()
}

//TODO: should this throw an exception or just ignore the param?
private fun assertValidFilters(filter: List<RequestResourceFilter>, validFields: Set<String>) {
    filter.forEach {
        if (!validFields.contains(it.filterField)) {
            throw BadRequestException("Cannot parse filter field: ${it.filterField}")
        } else if (it.filterTerms.isEmpty()) {
            throw BadRequestException("Cannot filter field ${it.filterField} with no value")
        }
    }
}

private fun assertValidSearches(searches: List<RequestResourceSearch>, validFields: Set<String>) {
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