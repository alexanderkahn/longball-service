package net.alexanderkahn.longball.service.persistence.model.entity

import net.alexanderkahn.base.servicebase.service.UserContext
import net.alexanderkahn.longball.service.model.FieldPosition
import net.alexanderkahn.longball.service.model.InningHalf
import net.alexanderkahn.longball.service.persistence.model.EmbeddableUser
import net.alexanderkahn.longball.service.persistence.model.OwnedIdentifiable
import net.alexanderkahn.longball.service.persistence.repository.getPersistenceUser
import javax.persistence.*
import javax.persistence.GenerationType.IDENTITY

@Entity(name = "lineup_player")
class PxLineupPlayer(

        @Id
        @GeneratedValue(strategy = IDENTITY)
        override val id: Long?,

        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_game"), nullable = false)
        val game: PxGame,

        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_player"), nullable = false)
        val player: PxPlayer,

        @Column(nullable = false)
        val inningHalf: InningHalf,

        @Column(nullable = false)
        val battingOrder: Short,

        @Column(nullable = false)
        val fieldPosition: FieldPosition,

        @Embedded
        override val owner: EmbeddableUser = UserContext.getPersistenceUser()
): OwnedIdentifiable {
        override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (other?.javaClass != javaClass) return false

                other as PxLineupPlayer

                if (id != other.id) return false
                if (game != other.game) return false
                if (player != other.player) return false
                if (inningHalf != other.inningHalf) return false
                if (battingOrder != other.battingOrder) return false
                if (fieldPosition != other.fieldPosition) return false
                if (owner != other.owner) return false

                return true
        }

        override fun hashCode(): Int {
                var result = id?.hashCode() ?: 0
                result = 31 * result + game.hashCode()
                result = 31 * result + player.hashCode()
                result = 31 * result + inningHalf.hashCode()
                result = 31 * result + battingOrder
                result = 31 * result + fieldPosition.hashCode()
                result = 31 * result + owner.hashCode()
                return result
        }

        override fun toString(): String {
                return "PxLineupPlayer(id=$id, game=$game, player=$player, inningHalf=$inningHalf, battingOrder=$battingOrder, fieldPosition=$fieldPosition, owner=$owner)"
        }
}