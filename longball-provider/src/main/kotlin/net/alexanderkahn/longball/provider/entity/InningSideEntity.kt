package net.alexanderkahn.longball.provider.entity


import net.alexanderkahn.longball.model.type.Side
import java.util.*
import javax.persistence.*

@Entity(name = "inning_side")
@Table(uniqueConstraints = arrayOf(UniqueConstraint(columnNames = arrayOf("inning_id", "side"))))
data class InningSideEntity(

        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_inning"), nullable = false)
        val inning: InningEntity,

        @Column(nullable=false)
        val side: Side,

        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_owner"), nullable = false)
        override val owner: UserEntity,
        @Id override val id: UUID = UUID.randomUUID()

        ) : BaseEntity