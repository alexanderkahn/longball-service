package net.alexanderkahn.longball.service.model

import java.time.ZonedDateTime

data class RosterPlayer (val id: Long, val team: Long, val player: Long, val jerseyNumber: Short, val startDate: ZonedDateTime, val endDate: ZonedDateTime? = null)