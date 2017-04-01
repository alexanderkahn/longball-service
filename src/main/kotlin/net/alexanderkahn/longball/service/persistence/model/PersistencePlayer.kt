package net.alexanderkahn.longball.service.persistence.model

import javax.persistence.*

@Entity(name = "player")
data class PersistencePlayer(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        override val id: Long? = null,

        @Embedded
        override val owner: EmbeddableUser,

        @Column(nullable = false)
        val first: String,

        @Column(nullable = false)
        val last: String
): OwnedIdentifiable