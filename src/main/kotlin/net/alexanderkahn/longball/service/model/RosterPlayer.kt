package net.alexanderkahn.longball.service.model

import java.time.ZonedDateTime

data class RosterPlayer (val id: String, val teamId: String, val playerId: String, val jerseyNumber: Short, val startDate: ZonedDateTime, val endDate: ZonedDateTime? = null)