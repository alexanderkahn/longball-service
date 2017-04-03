package net.alexanderkahn.longball.service.persistence.model

import net.alexanderkahn.longball.service.model.InningHalf
import javax.persistence.*

@Entity(name = "plate_appearance")
data class PersistencePlateAppearance(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        override val id: Long?,

        @Embedded
        override val owner: EmbeddableUser,

        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_game"), nullable = false)
        val game: PersistenceGame,

        @Column(nullable = false)
        val inning: Short,

        @Column(nullable = false)
        val half: InningHalf,

        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_player"), nullable = false)
        val batter: PersistencePlayer

        ): OwnedIdentifiable