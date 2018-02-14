package net.alexanderkahn.longball.model.dto

import net.alexanderkahn.service.commons.model.response.body.CollectionResponse
import net.alexanderkahn.service.commons.model.response.body.data.ResourceObject
import net.alexanderkahn.service.commons.model.response.body.meta.ResponseMetaPage
import org.springframework.data.domain.Page

fun <RO: ResourceObject> Page<RO>.toCollectionResponse(included: List<ResourceObject>? = null): CollectionResponse<RO> {
    return CollectionResponse(this.content, this.toMetaPage(), included)
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
