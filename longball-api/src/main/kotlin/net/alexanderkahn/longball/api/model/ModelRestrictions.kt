package net.alexanderkahn.longball.api.model

//FIXME re-implement this stuff
//fun RequestResourceObject.assertType(type: ModelTypes) = assertType(type.display)
//fun ResourceIdentifier.assertType(type: ModelTypes) = assertType(type.display)

const val MIN_NAME_FIELD_SIZE = 3
const val MAX_NAME_FIELD_SIZE = 255

fun invalidFieldLengthMessage(
        fieldName: String,
        minLength: Int = MIN_NAME_FIELD_SIZE,
        maxLength: Int = MAX_NAME_FIELD_SIZE
): String = "Invalid value for field: $fieldName. Value must be between $minLength and $maxLength characters long"