package net.alexanderkahn.longball.service.persistence.model

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.PersistenceConstructor

data class PersistenceTeam @PersistenceConstructor constructor(@Id val id: String? = null, val abbreviation: String, val location: String, val nickname: String)