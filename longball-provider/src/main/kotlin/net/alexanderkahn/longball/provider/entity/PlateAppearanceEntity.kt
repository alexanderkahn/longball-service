package net.alexanderkahn.longball.provider.entity

import net.alexanderkahn.longball.model.type.PlateAppearanceResultType
import java.util.*
import javax.persistence.*

@Entity(name = "plate_appearance")
data class PlateAppearanceEntity(
        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_inning_side"), nullable = false) val side: InningSideEntity,

        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_pitcher"), nullable = false) val pitcher: LineupPositionEntity,

        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_batter"), nullable = false) val batter: LineupPositionEntity,

        @Column(nullable = true)
        var plateAppearanceResult: PlateAppearanceResultType? = null,

        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_owner"), nullable = false)
        override val owner: UserEntity,
        @Id override val id: UUID = UUID.randomUUID()

) : BaseEntity