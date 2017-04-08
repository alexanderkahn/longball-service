package net.alexanderkahn.longball.service.persistence.model.entity

import net.alexanderkahn.base.servicebase.service.UserContext
import net.alexanderkahn.longball.service.model.Pitch
import net.alexanderkahn.longball.service.persistence.model.EmbeddableUser
import net.alexanderkahn.longball.service.persistence.model.OwnedIdentifiable
import net.alexanderkahn.longball.service.persistence.repository.getPersistenceUser
import javax.persistence.*
import javax.persistence.GenerationType.IDENTITY

@Entity(name = "gameplay_event")
data class PxGameplayEvent(
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
        override fun toString(): String {
                return "PxGameplayEvent(id=$id, owner=$owner, pitch=$pitch)"
        }
}