package net.alexanderkahn.longball.service.persistence.model

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.PersistenceConstructor
import java.time.Instant

data class PersistenceRosterPlayer @PersistenceConstructor constructor(@Id val id: String? = null, val teamId: String, val playerId: String, val jerseyNumber: Short, val startDate: Instant, val endDate: Instant? = null)