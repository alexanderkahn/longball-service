package net.alexanderkahn.longball.service.persistence.model.entity

import net.alexanderkahn.base.servicebase.service.UserContext
import net.alexanderkahn.longball.service.model.PlateAppearanceResult
import net.alexanderkahn.longball.service.persistence.model.EmbeddableUser
import net.alexanderkahn.longball.service.persistence.model.OwnedIdentifiable
import net.alexanderkahn.longball.service.persistence.repository.getPersistenceUser
import javax.persistence.*
import javax.persistence.GenerationType.IDENTITY

@Entity(name = "plate_appearance_result")
class PxPlateAppearanceResult(
        @Id
        @GeneratedValue(strategy = IDENTITY)
        override val id: Long?,

        @OneToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_plate_appearance"), nullable = false)
        val plateAppearance: PxPlateAppearance,

        @Column(nullable = false)
        val plateAppearanceResult: PlateAppearanceResult,

        @Embedded
        override val owner: EmbeddableUser = UserContext.getPersistenceUser()

): OwnedIdentifiable {

        override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (other?.javaClass != javaClass) return false

                other as PxPlateAppearanceResult

                if (id != other.id) return false
                if (plateAppearance != other.plateAppearance) return false
                if (plateAppearanceResult != other.plateAppearanceResult) return false
                if (owner != other.owner) return false

                return true
        }

        override fun hashCode(): Int {
                var result = id?.hashCode() ?: 0
                result = 31 * result + plateAppearance.hashCode()
                result = 31 * result + plateAppearanceResult.hashCode()
                result = 31 * result + owner.hashCode()
                return result
        }

        override fun toString(): String {
                return "PxPlateAppearanceResult(id=$id, plateAppearance=$plateAppearance, plateAppearanceResult=$plateAppearanceResult, owner=$owner)"
        }


}