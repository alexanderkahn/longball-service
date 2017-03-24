package net.alexanderkahn.longball.service.persistence.model

import org.springframework.data.annotation.PersistenceConstructor
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity(name = "player")
data class PersistencePlayer @PersistenceConstructor constructor(
        @Id
        @GeneratedValue
        val id: Long? = null,

        @Column(nullable = false)
        val first: String,

        @Column(nullable = false)
        val last: String)