package net.alexanderkahn.longball.model.dto

import net.alexanderkahn.service.base.model.request.RequestResourceObject
import net.alexanderkahn.service.base.model.response.body.data.ResourceIdentifier

fun RequestResourceObject.assertType(type: ModelTypes) = assertType(type.display)
fun ResourceIdentifier.assertType(type: ModelTypes) = assertType(type.display)