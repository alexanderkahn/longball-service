package net.alexanderkahn.longball.service.persistence.model.entity

import net.alexanderkahn.base.servicebase.service.UserContext
import net.alexanderkahn.longball.service.persistence.model.EmbeddableUser
import net.alexanderkahn.longball.service.persistence.model.OwnedIdentifiable
import net.alexanderkahn.longball.service.persistence.repository.getPersistenceUser
import javax.persistence.*

@Entity(name="inning_half_result")
class PxInningHalfResult(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        override val id: Long? = null,

        @Column(nullable = false)
        val runs: Int,

        @OneToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_inning_half_result"), nullable = false)
        val inningHalf: PxInningHalf,

        @Embedded
        override val owner: EmbeddableUser = UserContext.getPersistenceUser()
): OwnedIdentifiable