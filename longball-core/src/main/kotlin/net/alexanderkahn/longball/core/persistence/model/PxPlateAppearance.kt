package net.alexanderkahn.longball.core.persistence.model

import net.alexanderkahn.longball.core.persistence.EmbeddableUser
import net.alexanderkahn.longball.core.persistence.OwnedIdentifiable
import javax.persistence.*
import javax.persistence.GenerationType.IDENTITY

@Entity(name = "plate_appearance")
data class PxPlateAppearance(
        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_inning_side"), nullable = false) val side: PxInningSide,

        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_lineup_player"), nullable = false) val batter: PxLineupPlayer,

        @Column(nullable = true)
        var plateAppearanceResult: Int? = null,

        @Id
        @GeneratedValue(strategy = IDENTITY)
        override val id: Long? = null,

        @Embedded
        override val owner: EmbeddableUser

): OwnedIdentifiable