package net.alexanderkahn.longball.service.persistence.model.entity

import net.alexanderkahn.longball.service.model.PlateAppearanceResult
import net.alexanderkahn.longball.service.persistence.model.EmbeddableUser
import net.alexanderkahn.longball.service.persistence.model.OwnedIdentifiable
import javax.persistence.*
import javax.persistence.GenerationType.IDENTITY

@Entity(name = "plate_appearance_result")
data class PxPlateAppearanceResult(
        @Id
        @GeneratedValue(strategy = IDENTITY)
        override val id: Long?,

        @Embedded
        override val owner: EmbeddableUser,

        @OneToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_plate_appearance"), nullable = false)
        val plateAppearance: PxPlateAppearance,

        @Column(nullable = false)
        val plateAppearanceResult: PlateAppearanceResult

): OwnedIdentifiable