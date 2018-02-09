package net.alexanderkahn.longball.provider.assembler

import sun.plugin.dom.exception.InvalidStateException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

private val DEFAULT_TIME = LocalTime.NOON

fun LocalDate.toPersistence(): LocalDateTime {
    return LocalDateTime.of(this, DEFAULT_TIME)
}

fun LocalDateTime.fromPersistence(): LocalDate {
    if (this.toLocalTime() != DEFAULT_TIME) {
        throw InvalidStateException("Attempted to convert a datetime to date; this would cause loss of precision!")
    }
    return this.toLocalDate()
}