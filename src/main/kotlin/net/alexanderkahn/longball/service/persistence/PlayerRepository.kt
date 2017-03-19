package net.alexanderkahn.longball.service.persistence

import net.alexanderkahn.longball.service.model.Player
import org.springframework.data.repository.PagingAndSortingRepository

interface PlayerRepository : PagingAndSortingRepository<Player, String>
