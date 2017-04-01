package net.alexanderkahn.longball.service.persistence.model

import javax.persistence.*

@Entity(name = "league")
data class PersistenceLeague(

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        override val id: Long?,

        @Embedded
        override val owner: EmbeddableUser,

        @Column(nullable = false)
        val name: String
): OwnedIdentifiable