package net.alexanderkahn.longball.presentation.rest.model

import net.alexanderkahn.service.base.presentation.request.RequestResourceObject
import net.alexanderkahn.service.base.presentation.response.body.data.ResourceIdentifier

enum class ModelTypes(val display: String) {
    LEAGUES("leagues"),
    TEAMS("teams"),
    PEOPLE("people"),
    ROSTER_POSITIONS("rosterpositions")
}

fun RequestResourceObject.assertType(type: ModelTypes) = assertType(type.display)
fun ResourceIdentifier.assertType(type: ModelTypes) = assertType(type.display)