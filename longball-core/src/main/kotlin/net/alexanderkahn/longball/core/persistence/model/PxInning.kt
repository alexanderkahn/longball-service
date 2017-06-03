package net.alexanderkahn.longball.core.persistence.model

import net.alexanderkahn.longball.core.persistence.EmbeddableUser
import net.alexanderkahn.longball.core.persistence.OwnedIdentifiable
import javax.persistence.*

@Entity(name = "inning")
@Table(uniqueConstraints = arrayOf(UniqueConstraint(columnNames = arrayOf("game_id", "inningNumber"))))
data class PxInning(

        @Embedded
        override val owner: EmbeddableUser,

        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_game"), nullable = false)
        val game: PxGame,

        @Column(nullable = false)
        val inningNumber: Int,

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        override val id: Long? = null

        ): OwnedIdentifiable