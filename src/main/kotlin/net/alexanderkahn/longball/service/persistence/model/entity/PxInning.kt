package net.alexanderkahn.longball.service.persistence.model.entity

import net.alexanderkahn.base.servicebase.service.UserContext
import net.alexanderkahn.longball.service.persistence.model.EmbeddableUser
import net.alexanderkahn.longball.service.persistence.model.OwnedIdentifiable
import net.alexanderkahn.longball.service.persistence.repository.getPersistenceUser
import javax.persistence.*

@Entity(name = "inning")
@Table(uniqueConstraints = arrayOf(UniqueConstraint(columnNames = arrayOf("game_id", "inningNumber"))))
class PxInning(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        override val id: Long? = null,

        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_game"), nullable = false)
        val game: PxGame,

        @Column(nullable = false)
        val inningNumber: Int,

        @OneToMany(mappedBy = "inning")
        val inningHalves: MutableList<PxInningHalf> = mutableListOf(),

        @Embedded
        override val owner: EmbeddableUser = UserContext.getPersistenceUser()
        ): OwnedIdentifiable {

        override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (other?.javaClass != javaClass) return false

                other as PxInning

                if (id != other.id) return false
                if (game != other.game) return false
                if (inningNumber != other.inningNumber) return false
                if (owner != other.owner) return false

                return true
        }

        override fun hashCode(): Int {
                var result = id?.hashCode() ?: 0
                result = 31 * result + game.hashCode()
                result = 31 * result + inningNumber
                result = 31 * result + owner.hashCode()
                return result
        }

        override fun toString(): String {
                return "PxInning(id=$id, game=$game, inningNumber=$inningNumber, owner=$owner)"
        }


}