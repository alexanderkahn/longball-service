package net.alexanderkahn.longball.service.persistence.model

import org.springframework.data.annotation.PersistenceConstructor
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity(name = "team")
data class PersistenceTeam @PersistenceConstructor constructor(
        @Id
        @GeneratedValue
        val id: Long? = null,

        @Column(nullable = false)
        val abbreviation: String,

        @Column(nullable = false)
        val location: String, val nickname: String)