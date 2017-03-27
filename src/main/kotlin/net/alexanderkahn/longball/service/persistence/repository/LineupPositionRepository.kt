package net.alexanderkahn.longball.service.persistence.repository

import net.alexanderkahn.longball.service.model.InningHalf
import net.alexanderkahn.longball.service.persistence.model.PersistenceLineupPosition
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.PagingAndSortingRepository

interface LineupPositionRepository: PagingAndSortingRepository<PersistenceLineupPosition, Long> {
    fun findByGameIdAndInningHalf(pageable: Pageable, gameId: Long, inningHalf: InningHalf): Page<PersistenceLineupPosition>
}