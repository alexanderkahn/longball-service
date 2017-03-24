package net.alexanderkahn.longball.service.persistence

import net.alexanderkahn.longball.service.persistence.model.PersistencePlayer
import org.springframework.data.repository.PagingAndSortingRepository

interface PlayerRepository : PagingAndSortingRepository<PersistencePlayer, Long>
