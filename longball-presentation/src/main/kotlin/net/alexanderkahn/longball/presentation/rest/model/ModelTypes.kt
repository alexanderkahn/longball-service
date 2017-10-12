package net.alexanderkahn.longball.presentation.rest.model

import net.alexanderkahn.service.base.presentation.request.RequestResourceObject

enum class ModelTypes(val display: String) {
    LEAGUES("leagues"),
    TEAMS("teams")
}

fun RequestResourceObject.assertType(type: ModelTypes) = assertType(type.display)