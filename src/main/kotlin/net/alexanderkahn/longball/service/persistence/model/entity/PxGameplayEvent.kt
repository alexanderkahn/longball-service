package net.alexanderkahn.longball.service.persistence.model.entity

import net.alexanderkahn.longball.service.model.Pitch
import net.alexanderkahn.longball.service.persistence.model.EmbeddableUser
import net.alexanderkahn.longball.service.persistence.model.OwnedIdentifiable
import javax.persistence.*
import javax.persistence.GenerationType.IDENTITY

@Entity(name = "gameplay_event")
data class PxGameplayEvent(
        @Id
        @GeneratedValue(strategy = IDENTITY)
        override val id: Long?,

        @Embedded
        override val owner: EmbeddableUser,

        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_plate_appearance"), nullable = false)
        val plateAppearance: PxPlateAppearance,

        @javax.persistence.Column(nullable = false)
        val pitch: Pitch,

        @javax.persistence.OneToOne(mappedBy = "gameplayEvent")
        var result: PxGameplayEventResult? = null
): OwnedIdentifiable {
        override fun toString(): String {
                return "PxGameplayEvent(id=$id, owner=$owner, pitch=$pitch)"
        }
}