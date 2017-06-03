package net.alexanderkahn.longball.core.persistence.model

import net.alexanderkahn.longball.core.persistence.EmbeddableUser
import net.alexanderkahn.longball.core.persistence.OwnedIdentifiable
import javax.persistence.*

@Entity(name = "inning_side")
@Table(uniqueConstraints = arrayOf(UniqueConstraint(columnNames = arrayOf("inning_id", "side"))))
data class PxInningSide(

        @Embedded
        override val owner: EmbeddableUser,

        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_inning"), nullable = false)
        val inning: PxInning,

        @Column(nullable=false)
        val side: Int,

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        override val id: Long? = null

        ): OwnedIdentifiable