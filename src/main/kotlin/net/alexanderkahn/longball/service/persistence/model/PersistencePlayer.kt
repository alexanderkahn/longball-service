package net.alexanderkahn.longball.service.persistence.model

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.PersistenceConstructor

data class PersistencePlayer @PersistenceConstructor constructor(@Id val id: String? = null, val first: String, val last: String)