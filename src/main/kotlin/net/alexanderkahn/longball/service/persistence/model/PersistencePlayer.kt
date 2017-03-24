package net.alexanderkahn.longball.service.persistence.model

import javax.persistence.*

@Entity(name = "player")
data class PersistencePlayer(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long? = null,

        @Column(nullable = false)
        val first: String,

        @Column(nullable = false)
        val last: String)