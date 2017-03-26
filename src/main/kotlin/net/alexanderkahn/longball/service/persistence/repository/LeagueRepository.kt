package net.alexanderkahn.longball.service.persistence.repository

import net.alexanderkahn.longball.service.persistence.model.PersistenceLeague
import org.springframework.data.repository.PagingAndSortingRepository

interface LeagueRepository: PagingAndSortingRepository<PersistenceLeague, Long>