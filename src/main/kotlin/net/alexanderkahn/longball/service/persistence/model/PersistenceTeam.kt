package net.alexanderkahn.longball.service.persistence.model

import javax.persistence.*

@Entity(name = "team")
data class PersistenceTeam(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long? = null,

        @Column(nullable = false)
        val abbreviation: String,

        @Column(nullable = false)
        val location: String, val nickname: String)