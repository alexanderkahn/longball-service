package net.alexanderkahn.longball.api.model.type

enum class FieldPosition(val positionNotation: Int) {
    PITCHER(1),
    CATCHER(2),
    FIRST_BASE(3),
    SECOND_BASE(4),
    THIRD_BASE(5),
    SHORTSTOP(6),
    LEFT_FIELD(7),
    CENTER_FIELD(8),
    RIGHT_FIELD(9);

    companion object {
        fun fromNotation(notation: Int): FieldPosition {
            return FieldPosition.values().single { it.positionNotation == notation }
        }
    }
}