package net.alexanderkahn.longball.service.persistence.model.entity

import net.alexanderkahn.longball.service.persistence.model.EmbeddableUser
import net.alexanderkahn.longball.service.persistence.model.OwnedIdentifiable
import javax.persistence.*
import javax.persistence.GenerationType.IDENTITY

@Entity(name = "team")
data class PxTeam(
        @Id
        @GeneratedValue(strategy = IDENTITY)
        override val id: Long? = null,

        @Embedded
        override val owner: EmbeddableUser,

        @Column(nullable = false)
        val abbreviation: String,

        @Column(nullable = false)
        val location: String, val nickname: String
): OwnedIdentifiable