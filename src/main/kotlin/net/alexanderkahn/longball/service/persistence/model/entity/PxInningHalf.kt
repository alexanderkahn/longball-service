package net.alexanderkahn.longball.service.persistence.model.entity

import net.alexanderkahn.base.servicebase.service.UserContext
import net.alexanderkahn.longball.service.model.InningHalf
import net.alexanderkahn.longball.service.persistence.model.EmbeddableUser
import net.alexanderkahn.longball.service.persistence.model.OwnedIdentifiable
import net.alexanderkahn.longball.service.persistence.repository.getPersistenceUser
import javax.persistence.*

@Entity(name = "inning_half")
@Table(uniqueConstraints = arrayOf(UniqueConstraint(columnNames = arrayOf("inning_id", "half"))))
data class PxInningHalf(

        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_inning"), nullable = false) val inning: PxInning,

        @Column(nullable = false) val half: InningHalf,

        @OneToOne(mappedBy = "inningHalf", cascade = arrayOf(CascadeType.ALL))
        var result: PxInningHalfResult? = null,

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        override val id: Long? = null,

        @Embedded
        override val owner: EmbeddableUser = UserContext.getPersistenceUser()

        ): OwnedIdentifiable