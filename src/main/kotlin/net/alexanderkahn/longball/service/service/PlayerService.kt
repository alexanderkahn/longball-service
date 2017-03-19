package net.alexanderkahn.longball.service.service

import net.alexanderkahn.longball.service.model.Player
import net.alexanderkahn.longball.service.persistence.PlayerRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class PlayerService(@Autowired private val playerRepository: PlayerRepository) {
    fun getAll(pageable: Pageable): Page<Player> {
        return playerRepository.findAll(pageable)
    }

    fun get(id: String): Player {
        return playerRepository.findOne(id)
    }
}