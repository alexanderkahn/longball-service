package net.alexanderkahn.longball.service.persistence.repository

import net.alexanderkahn.base.servicebase.service.UserContext
import net.alexanderkahn.longball.service.persistence.model.EmbeddableUser
import net.alexanderkahn.longball.service.persistence.model.entity.PxBasepathResult
import net.alexanderkahn.longball.service.persistence.model.entity.PxGameplayEvent
import net.alexanderkahn.longball.service.persistence.model.entity.PxPlateAppearance
import org.springframework.data.jpa.repository.Query

interface BasepathResultRepository: LongballRepository<PxBasepathResult> {
    fun findByGameplayEventAndOwner(event: PxGameplayEvent, owner: EmbeddableUser = UserContext.getPersistenceUser()): List<PxBasepathResult>

    @Query("select br from basepath_result br where br.owner=?2 and br.gameplayEvent.plateAppearance in (?1)")
    fun findByPlateAppearanceAndOwner(inningAppearances: List<PxPlateAppearance>, owner: EmbeddableUser = UserContext.getPersistenceUser()): List<PxBasepathResult>
}