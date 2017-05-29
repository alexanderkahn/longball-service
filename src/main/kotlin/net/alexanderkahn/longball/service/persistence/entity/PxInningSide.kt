package net.alexanderkahn.longball.service.persistence.entity

import net.alexanderkahn.servicebase.provider.security.UserContext
import net.alexanderkahn.longball.service.model.Side
import net.alexanderkahn.longball.service.persistence.EmbeddableUser
import net.alexanderkahn.longball.service.persistence.OwnedIdentifiable
import net.alexanderkahn.longball.service.persistence.repository.getPersistenceUser
import javax.persistence.*

@Entity(name = "inning_side")
@Table(uniqueConstraints = arrayOf(UniqueConstraint(columnNames = arrayOf("inning_id", "side"))))
data class PxInningSide(

        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_inning"), nullable = false)
        val inning: PxInning,

        @Column(nullable=false)
        val side: Side,

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        override val id: Long? = null,

        @Embedded
        override val owner: EmbeddableUser = UserContext.getPersistenceUser()

        ): OwnedIdentifiable