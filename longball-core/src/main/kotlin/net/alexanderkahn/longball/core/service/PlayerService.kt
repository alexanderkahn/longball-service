package net.alexanderkahn.longball.core.service

import net.alexanderkahn.longball.core.assembler.pxUser
import net.alexanderkahn.longball.core.assembler.toModel
import net.alexanderkahn.longball.model.Player
import net.alexanderkahn.longball.core.persistence.repository.PlayerRepository
import net.alexanderkahn.servicebase.core.security.UserContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class PlayerService(@Autowired private val playerRepository: PlayerRepository) {

    fun getAll(pageable: Pageable): Page<Player> {
        val players = playerRepository.findByOwner(pageable, UserContext.pxUser)
        return players.map { it.toModel() }
    }

    fun get(id: Long): Player {
        val player = playerRepository.findByIdAndOwner(id, UserContext.pxUser)
        return player.toModel()
    }
}