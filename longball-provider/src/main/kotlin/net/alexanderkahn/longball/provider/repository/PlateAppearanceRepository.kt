package net.alexanderkahn.longball.provider.repository

import net.alexanderkahn.longball.model.type.Side
import net.alexanderkahn.longball.provider.entity.EmbeddableUser
import net.alexanderkahn.longball.provider.entity.InningSideEntity
import net.alexanderkahn.longball.provider.entity.PlateAppearanceEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import java.util.*

interface PlateAppearanceRepository: LongballRepository<PlateAppearanceEntity> {
    fun findFirstBySideAndOwnerOrderByIdDesc(side: InningSideEntity, owner: EmbeddableUser): PlateAppearanceEntity?
    fun findBySideAndOwner(side: InningSideEntity, owner: EmbeddableUser): List<PlateAppearanceEntity> //TODO should this be deleted?

    @Query("select p from plate_appearance p where p.owner = ?2 and p.side = ?5 and p.side.side = ?4 and p.side.inning.game = ?3")
    fun findByOwnerAndGameAndInningNumberAndSide(pageable: Pageable, owner: EmbeddableUser, gameId: UUID, inningNumber: Int, side: Side): Page<PlateAppearanceEntity>
}