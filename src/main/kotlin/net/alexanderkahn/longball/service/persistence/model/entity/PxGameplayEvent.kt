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
        @Id
        @GeneratedValue(strategy = IDENTITY)
        override val id: Long?,

        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_plate_appearance"), nullable = false)
        val plateAppearance: PxPlateAppearance,

        @Column(nullable = false)
        val pitch: Pitch,

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
                if (owner != other.owner) return false

                return true
        }

        override fun hashCode(): Int {
                var result = id?.hashCode() ?: 0
                result = 31 * result + plateAppearance.hashCode()
                result = 31 * result + pitch.hashCode()
                result = 31 * result + owner.hashCode()
                return result
        }

        override fun toString(): String {
                return "PxGameplayEvent(id=$id, plateAppearance=$plateAppearance, pitch=$pitch, owner=$owner)"
        }
}