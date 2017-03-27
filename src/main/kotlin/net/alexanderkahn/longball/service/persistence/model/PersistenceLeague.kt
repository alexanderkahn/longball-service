package net.alexanderkahn.longball.service.persistence.model

import javax.persistence.*

@Entity(name = "league")
data class PersistenceLeague(

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long?,

        @Column(nullable = false)
        val name: String
)