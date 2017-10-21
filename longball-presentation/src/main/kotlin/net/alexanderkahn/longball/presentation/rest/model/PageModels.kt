package net.alexanderkahn.longball.presentation.rest.model

import net.alexanderkahn.service.base.presentation.response.CollectionResponse
import net.alexanderkahn.service.base.presentation.response.body.data.ResponseResourceCollection
import net.alexanderkahn.service.base.presentation.response.body.data.ResponseResourceObject
import net.alexanderkahn.service.base.presentation.response.body.meta.ResponseMetaPage
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
            previousPageable()?.pageNumber,
            nextPageable()?.pageNumber
    )
}
