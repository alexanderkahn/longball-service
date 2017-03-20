package net.alexanderkahn.longball.service.persistence

import net.alexanderkahn.longball.service.model.Team
import org.springframework.data.repository.PagingAndSortingRepository

interface TeamRepository: PagingAndSortingRepository<Team, String>