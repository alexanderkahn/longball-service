package net.alexanderkahn.longball.service.persistence.model.entity

import net.alexanderkahn.longball.service.model.InningHalf
import net.alexanderkahn.longball.service.persistence.model.EmbeddableUser
import net.alexanderkahn.longball.service.persistence.model.OwnedIdentifiable
import javax.persistence.*
import javax.persistence.GenerationType.IDENTITY

@Entity(name = "plate_appearance")
data class PxPlateAppearance(
        @Id
        @GeneratedValue(strategy = IDENTITY)
        override val id: Long?,

        @Embedded
        override val owner: EmbeddableUser,

        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_game"), nullable = false)
        val game: PxGame,

        @Column(nullable = false)
        val inning: Short,

        @Column(nullable = false)
        val half: InningHalf,

        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_lineup_player"), nullable = false)
        val batter: PxLineupPlayer,

        @OneToMany(mappedBy = "plateAppearance")
        val events: MutableList<PxGameplayEvent> = mutableListOf(),

        @OneToOne(mappedBy = "plateAppearance")
        val result: PxPlateAppearanceResult? = null

): OwnedIdentifiable