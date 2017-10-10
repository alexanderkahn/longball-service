package net.alexanderkahn.longball.presentation.rest.model

import net.alexanderkahn.service.base.presentation.response.body.meta.ResponseMetaPage
import org.springframework.data.domain.Page

fun Page<out Any>.toMetaPage(): ResponseMetaPage {
    return ResponseMetaPage(this.isFirst, isLast, 0, 0) //TODO: this doesn't work
}