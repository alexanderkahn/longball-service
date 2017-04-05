package net.alexanderkahn.longball.service.persistence.model

import net.alexanderkahn.longball.service.model.AtBatResult
import javax.persistence.*

@Entity(name = "gameplay_event_result")
data class PxGameplayEventResult(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        override val id: Long? = null,

        @Embedded
        override val owner: EmbeddableUser,

        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_gameplay_event"), nullable = false)
        val gameplayEvent: PersistenceGameplayEvent,

        @Column(nullable = false)
        val atBatResult: AtBatResult

): OwnedIdentifiable {
    override fun toString(): String {
        return "PxGameplayEventResult(id=$id, owner=$owner, atBatResult=$atBatResult)"
    }
}

