package net.alexanderkahn.longball.service.service

import net.alexanderkahn.longball.service.model.Player
import net.alexanderkahn.longball.service.persistence.repository.PlayerRepository
import net.alexanderkahn.longball.service.service.assembler.toModel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class PlayerService(@Autowired private val playerRepository: PlayerRepository) {

    fun getAll(pageable: Pageable): Page<Player> {
        val players = playerRepository.findByOwner(pageable)
        return players.map { it.toModel() }
    }

    fun get(id: Long): Player {
        val player = playerRepository.findByIdAndOwner(id)
        return player.toModel()
    }
}