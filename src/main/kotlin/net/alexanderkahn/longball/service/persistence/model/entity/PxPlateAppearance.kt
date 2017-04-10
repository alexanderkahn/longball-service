package net.alexanderkahn.longball.service.persistence.model.entity

import net.alexanderkahn.base.servicebase.service.UserContext
import net.alexanderkahn.longball.service.model.PlateAppearanceResult
import net.alexanderkahn.longball.service.persistence.model.EmbeddableUser
import net.alexanderkahn.longball.service.persistence.model.OwnedIdentifiable
import net.alexanderkahn.longball.service.persistence.repository.getPersistenceUser
import javax.persistence.*
import javax.persistence.GenerationType.IDENTITY

@Entity(name = "plate_appearance")
class PxPlateAppearance(
        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_inning_half"), nullable = false) val inningHalf: PxInningHalf,

        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_lineup_player"), nullable = false) val batter: PxLineupPlayer,

        @OneToMany(mappedBy = "plateAppearance", cascade = arrayOf(CascadeType.ALL))
        @OrderBy("id ASC") var pitchEvents: MutableList<PxGameplayEvent> = mutableListOf(),

        @Column(nullable = true)
        var plateAppearanceResult: PlateAppearanceResult? = null,

        @Id
        @GeneratedValue(strategy = IDENTITY)
        override val id: Long? = null,

        @Embedded
        override val owner: EmbeddableUser = UserContext.getPersistenceUser()

): OwnedIdentifiable {
        override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (other?.javaClass != javaClass) return false

                other as PxPlateAppearance

                if (inningHalf.id != other.inningHalf.id) return false
                if (batter.id != other.batter.id) return false
                if (pitchEvents != other.pitchEvents) return false
                if (plateAppearanceResult != other.plateAppearanceResult) return false
                if (id != other.id) return false
                if (owner != other.owner) return false

                return true
        }

        override fun hashCode(): Int {
                var result = inningHalf.id?.hashCode() ?: 31
                result = 31 * result + batter.hashCode()
                result = 31 * result + pitchEvents.hashCode()
                result = 31 * result + (plateAppearanceResult?.hashCode() ?: 0)
                result = 31 * result + (id?.hashCode() ?: 0)
                result = 31 * result + owner.hashCode()
                return result
        }
}