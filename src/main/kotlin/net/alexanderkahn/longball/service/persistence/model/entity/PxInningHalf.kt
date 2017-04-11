package net.alexanderkahn.longball.service.persistence.model.entity

import net.alexanderkahn.base.servicebase.service.UserContext
import net.alexanderkahn.longball.service.model.InningHalf
import net.alexanderkahn.longball.service.persistence.model.EmbeddableUser
import net.alexanderkahn.longball.service.persistence.model.OwnedIdentifiable
import net.alexanderkahn.longball.service.persistence.repository.getPersistenceUser
import javax.persistence.*

@Entity(name = "inning_half")
@Table(uniqueConstraints = arrayOf(UniqueConstraint(columnNames = arrayOf("inning_id", "half"))))
class PxInningHalf(

        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_inning"), nullable = false) val inning: PxInning,

        @Column(nullable = false) val half: InningHalf,

        @OneToMany(mappedBy = "inningHalf", cascade = arrayOf(CascadeType.ALL))
        @OrderBy("id ASC") var plateAppearances: MutableList<PxPlateAppearance> = mutableListOf(),

        @OneToOne(mappedBy = "inningHalf", cascade = arrayOf(CascadeType.ALL))
        var result: PxInningHalfResult? = null,

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        override val id: Long? = null,

        @Embedded
        override val owner: EmbeddableUser = UserContext.getPersistenceUser()

        ): OwnedIdentifiable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as PxInningHalf

        if (id != other.id) return false
        if (inning != other.inning) return false
        if (half != other.half) return false
        if (owner != other.owner) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + inning.hashCode()
        result = 31 * result + half.hashCode()
        result = 31 * result + owner.hashCode()
        return result
    }

    override fun toString(): String {
        return "PxInningHalf(id=$id, inning=$inning, half=$half, owner=$owner)"
    }


}