package net.alexanderkahn.longball.provider.entity


import net.alexanderkahn.longball.model.Side
import javax.persistence.*

@Entity(name = "inning_side")
@Table(uniqueConstraints = arrayOf(UniqueConstraint(columnNames = arrayOf("inning_id", "side"))))
data class InningSideEntity(

        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_inning"), nullable = false)
        val inning: InningEntity,

        @Column(nullable=false)
        val side: Side

        ) : BaseEntity()