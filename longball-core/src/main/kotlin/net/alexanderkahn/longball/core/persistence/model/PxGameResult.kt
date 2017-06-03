package net.alexanderkahn.longball.core.persistence.model

import net.alexanderkahn.longball.core.persistence.EmbeddableUser
import net.alexanderkahn.longball.core.persistence.OwnedIdentifiable
import java.time.OffsetDateTime
import javax.persistence.*

@Entity(name = "game_result")
data class PxGameResult(

        @Embedded
        override val owner: EmbeddableUser,

        @OneToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_game"), nullable = false)
        val game: PxGame,

        @Column(nullable = false)
        val endTime: OffsetDateTime,

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        override val id: Long? = null

): OwnedIdentifiable