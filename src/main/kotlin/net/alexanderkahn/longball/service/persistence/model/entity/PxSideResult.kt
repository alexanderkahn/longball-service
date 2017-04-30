package net.alexanderkahn.longball.service.persistence.model.entity

import net.alexanderkahn.base.servicebase.service.UserContext
import net.alexanderkahn.longball.service.persistence.model.EmbeddableUser
import net.alexanderkahn.longball.service.persistence.model.OwnedIdentifiable
import net.alexanderkahn.longball.service.persistence.repository.getPersistenceUser
import javax.persistence.*

@Entity(name="side_result")
data class PxSideResult(
        @OneToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_inning_side"), nullable = false)
        val side: PxInningSide,

        @Column(nullable = false)
        val hits: Int,

        @Column(nullable = false)
        val walks: Int,

        @Column(nullable = false)
        val errors: Int,

        @Column(nullable = false)
        val runs: Int,

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        override val id: Long? = null,

        @Embedded
        override val owner: EmbeddableUser = UserContext.getPersistenceUser()
): OwnedIdentifiable