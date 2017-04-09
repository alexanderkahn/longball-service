package net.alexanderkahn.longball.service.persistence.model.entity

import net.alexanderkahn.base.servicebase.service.UserContext
import net.alexanderkahn.longball.service.model.Pitch
import net.alexanderkahn.longball.service.persistence.model.EmbeddableUser
import net.alexanderkahn.longball.service.persistence.model.OwnedIdentifiable
import net.alexanderkahn.longball.service.persistence.repository.getPersistenceUser
import javax.persistence.*
import javax.persistence.GenerationType.IDENTITY

@Entity(name = "gameplay_event")
class PxGameplayEvent(
        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_plate_appearance"), nullable = false) val plateAppearance: PxPlateAppearance,

        @Column(nullable = false) val pitch: Pitch,

        @OneToOne(mappedBy = "gameplayEvent", cascade = arrayOf(CascadeType.ALL)) var result: PxPlateAppearanceResult? = null,

        @Id
        @GeneratedValue(strategy = IDENTITY)
        override val id: Long? = null,

        @Embedded
        override val owner: EmbeddableUser = UserContext.getPersistenceUser()
): OwnedIdentifiable {

        override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (other?.javaClass != javaClass) return false

                other as PxGameplayEvent

                if (id != other.id) return false
                if (plateAppearance != other.plateAppearance) return false
                if (pitch != other.pitch) return false
                if (result != other.result) return false
                if (owner != other.owner) return false

                return true
        }

        override fun hashCode(): Int {
                var result1 = id?.hashCode() ?: 0
                result1 = 31 * result1 + plateAppearance.hashCode()
                result1 = 31 * result1 + pitch.hashCode()
                result1 = 31 * result1 + (result?.hashCode() ?: 0)
                result1 = 31 * result1 + owner.hashCode()
                return result1
        }

        override fun toString(): String {
                return "PxGameplayEvent(id=$id, plateAppearance=$plateAppearance, pitch=$pitch, result=$result, owner=$owner)"
        }


}