package net.alexanderkahn.longball.service.persistence.model

import javax.persistence.*

@Entity(name = "team")
data class PersistenceTeam(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        override val id: Long? = null,

        @Embedded
        override val owner: EmbeddableUser,

        @Column(nullable = false)
        val abbreviation: String,

        @Column(nullable = false)
        val location: String, val nickname: String
): OwnedIdentifiable