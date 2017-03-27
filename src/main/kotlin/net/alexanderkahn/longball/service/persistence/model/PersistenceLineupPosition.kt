package net.alexanderkahn.longball.service.persistence.model

import net.alexanderkahn.longball.service.model.FieldPosition
import net.alexanderkahn.longball.service.model.InningHalf
import javax.persistence.*

//TODO: I don't love this.
@Entity(name = "lineup_position")
data class PersistenceLineupPosition(

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long?,

        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_game"), nullable = false)
        val game: PersistenceGame,

        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_player"), nullable = false)
        val player: PersistencePlayer,

        @Column(nullable = false)
        val inningHalf: InningHalf,

        @Column(nullable = false)
        val battingOrder: Long,

        @Column(nullable = false)
        val fieldPosition: FieldPosition
)