package net.alexanderkahn.longball.service.persistence.repository

import net.alexanderkahn.longball.service.persistence.model.PersistenceGame
import org.springframework.data.repository.PagingAndSortingRepository

interface GameRepository: PagingAndSortingRepository<PersistenceGame, Long>