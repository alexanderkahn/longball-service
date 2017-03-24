package net.alexanderkahn.longball.service.persistence.repository

import net.alexanderkahn.longball.service.persistence.model.PersistenceTeam
import org.springframework.data.repository.PagingAndSortingRepository

interface TeamRepository: PagingAndSortingRepository<PersistenceTeam, Long>