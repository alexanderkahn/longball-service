package net.alexanderkahn.longball.service.persistence.model

import net.alexanderkahn.longball.service.model.Pitch
import javax.persistence.*

@Entity(name = "gameplay_event")
data class PersistenceGameplayEvent(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        override val id: Long?,

        @Embedded
        override val owner: EmbeddableUser,

        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_plate_appearance"), nullable = false)
        val plateAppearance: PersistencePlateAppearance,

        @Column(nullable = false)
        val pitch: Pitch,

        @OneToOne(mappedBy = "gameplayEvent")
        var result: PxGameplayEventResult? = null
): OwnedIdentifiable {
        override fun toString(): String {
                return "PersistenceGameplayEvent(id=$id, owner=$owner, pitch=$pitch)"
        }
}