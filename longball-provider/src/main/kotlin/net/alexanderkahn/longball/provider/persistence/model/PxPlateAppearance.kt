package net.alexanderkahn.longball.provider.persistence.model

import net.alexanderkahn.longball.model.PlateAppearanceResult
import net.alexanderkahn.longball.provider.persistence.EmbeddableUser
import net.alexanderkahn.longball.provider.persistence.OwnedIdentifiable
import javax.persistence.*
import javax.persistence.GenerationType.IDENTITY

@Entity(name = "plate_appearance")
data class PxPlateAppearance(
        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_inning_side"), nullable = false) val side: PxInningSide,

        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_pitcher"), nullable = false) val pitcher: PxLineupPlayer,

        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_batter"), nullable = false) val batter: PxLineupPlayer,

        @Column(nullable = true)
        var plateAppearanceResult: PlateAppearanceResult? = null,

        @Id
        @GeneratedValue(strategy = IDENTITY)
        override val id: Long? = null,

        @Embedded
        override val owner: EmbeddableUser

): OwnedIdentifiable