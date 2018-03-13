package net.alexanderkahn.longball.rest.assembler

import net.alexanderkahn.service.commons.model.response.body.CollectionResponse
import net.alexanderkahn.service.commons.model.response.body.data.ResourceObject
import net.alexanderkahn.service.commons.model.response.body.meta.CollectionResponseMeta
import org.springframework.data.domain.Page

fun <RO: ResourceObject> Page<RO>.toCollectionResponse(included: List<ResourceObject>? = null): CollectionResponse<RO> {
    return CollectionResponse(this.content, this.toMetaPage(), included)
}

private fun Page<out Any>.toMetaPage(): CollectionResponseMeta.Page {
    return CollectionResponseMeta.Page(
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
