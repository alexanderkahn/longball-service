package net.alexanderkahn.longball.model.dto

import net.alexanderkahn.service.commons.model.response.CollectionResponse
import net.alexanderkahn.service.commons.model.response.body.data.ResponseResourceCollection
import net.alexanderkahn.service.commons.model.response.body.data.ResponseResourceObject
import net.alexanderkahn.service.commons.model.response.body.meta.ResponseMetaPage
import org.springframework.data.domain.Page

fun <RO: ResponseResourceObject> Page<RO>.toCollectionResponse(included: List<ResponseResourceObject>? = null): CollectionResponse<RO> {
    return CollectionResponse(ResponseResourceCollection(this.content), this.toMetaPage(), included)
}

private fun Page<out Any>.toMetaPage(): ResponseMetaPage {
    return ResponseMetaPage(
            isFirst,
            isLast,
            totalElements.toInt(),
            totalPages,
            number,
            size,
            if (hasPrevious()) previousPageable().pageNumber else null,
            if (hasNext()) nextPageable().pageNumber else null
    )
}
