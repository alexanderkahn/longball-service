package net.alexanderkahn.longball.service.persistence.model.entity

import net.alexanderkahn.base.servicebase.service.UserContext
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
        @OrderBy("id ASC") var events: MutableList<PxGameplayEvent> = mutableListOf(),

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

                if (id != other.id) return false
                if (inningHalf != other.inningHalf) return false
                if (batter != other.batter) return false
                if (owner != other.owner) return false

                return true
        }

        override fun hashCode(): Int {
                var result1 = id?.hashCode() ?: 0
                result1 = 31 * result1 + inningHalf.hashCode()
                result1 = 31 * result1 + batter.hashCode()
                result1 = 31 * result1 + owner.hashCode()
                return result1
        }

        override fun toString(): String {
                return "PxPlateAppearance(id=$id, inningHalf=$inningHalf, batter=$batter, owner=$owner)"
        }


}