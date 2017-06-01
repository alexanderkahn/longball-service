package net.alexanderkahn.longball.persistence.model

import net.alexanderkahn.longball.persistence.EmbeddableUser
import net.alexanderkahn.longball.persistence.OwnedIdentifiable
import javax.persistence.*
import javax.persistence.GenerationType.IDENTITY

@Entity(name = "team")
data class PxTeam(

        @Embedded
        override val owner: EmbeddableUser,

        @Column(nullable = false) val abbreviation: String,

        @Column(nullable = false) val location: String,

        @Column(nullable = false) val nickname: String,

        @Id
        @GeneratedValue(strategy = IDENTITY)
        override val id: Long? = null

): OwnedIdentifiable