package net.alexanderkahn.longball.service.persistence.model

import net.alexanderkahn.longball.service.model.FieldPosition
import net.alexanderkahn.longball.service.model.InningHalf
import javax.persistence.*

@Entity(name = "lineup_position")
data class PersistenceLineupPosition(

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        override val id: Long?,

        @Embedded
        override val owner: EmbeddableUser,

        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_game"), nullable = false)
        val game: PersistenceGame,

        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_player"), nullable = false)
        val player: PersistencePlayer,

        @Column(nullable = false)
        val inningHalf: InningHalf,

        @Column(nullable = false)
        val battingOrder: Short,

        @Column(nullable = false)
        val fieldPosition: FieldPosition
): OwnedIdentifiable