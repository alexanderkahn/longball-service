package net.alexanderkahn.longball.service.persistence.model.entity

import net.alexanderkahn.base.servicebase.service.UserContext
import net.alexanderkahn.longball.service.model.InningHalf
import net.alexanderkahn.longball.service.persistence.model.EmbeddableUser
import net.alexanderkahn.longball.service.persistence.model.OwnedIdentifiable
import net.alexanderkahn.longball.service.persistence.repository.getPersistenceUser
import javax.persistence.*
import javax.persistence.GenerationType.IDENTITY

@Entity(name = "plate_appearance")
class PxPlateAppearance(
        @Id
        @GeneratedValue(strategy = IDENTITY)
        override val id: Long?,

        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_game"), nullable = false)
        val game: PxGame,

        @Column(nullable = false)
        val inning: Short,

        @Column(nullable = false)
        val half: InningHalf,

        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_lineup_player"), nullable = false)
        val batter: PxLineupPlayer,

        @OneToMany(mappedBy = "plateAppearance")
        var events: MutableList<PxGameplayEvent> = mutableListOf(),

        @OneToOne(mappedBy = "plateAppearance")
        var result: PxPlateAppearanceResult? = null,

        @Embedded
        override val owner: EmbeddableUser = UserContext.getPersistenceUser()

): OwnedIdentifiable {
        override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (other?.javaClass != javaClass) return false

                other as PxPlateAppearance

                if (id != other.id) return false
                if (game != other.game) return false
                if (inning != other.inning) return false
                if (half != other.half) return false
                if (batter != other.batter) return false
                if (owner != other.owner) return false

                return true
        }

        override fun hashCode(): Int {
                var result1 = id?.hashCode() ?: 0
                result1 = 31 * result1 + game.hashCode()
                result1 = 31 * result1 + inning
                result1 = 31 * result1 + half.hashCode()
                result1 = 31 * result1 + batter.hashCode()
                result1 = 31 * result1 + owner.hashCode()
                return result1
        }

        override fun toString(): String {
                return "PxPlateAppearance(id=$id, game=$game, inning=$inning, half=$half, batter=$batter, owner=$owner)"
        }
}