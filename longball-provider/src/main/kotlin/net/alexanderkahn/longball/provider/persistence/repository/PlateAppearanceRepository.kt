package net.alexanderkahn.longball.provider.persistence.repository

import net.alexanderkahn.longball.model.Side
import net.alexanderkahn.longball.provider.persistence.EmbeddableUser
import net.alexanderkahn.longball.provider.persistence.model.PxInningSide
import net.alexanderkahn.longball.provider.persistence.model.PxPlateAppearance
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import java.util.*

interface PlateAppearanceRepository: LongballRepository<PxPlateAppearance> {
    fun findFirstBySideAndOwnerOrderByIdDesc(side: PxInningSide, owner: EmbeddableUser): PxPlateAppearance?
    fun findBySideAndOwner(side: PxInningSide, owner: EmbeddableUser): List<PxPlateAppearance> //TODO should this be deleted?

    @Query("select p from plate_appearance p where p.owner = ?2 and p.side = ?5 and p.side.side = ?4 and p.side.inning.game = ?3")
    fun findByOwnerAndGameAndInningNumberAndSide(pageable: Pageable, owner: EmbeddableUser, gameId: UUID, inningNumber: Int, side: Side): Page<PxPlateAppearance>
}