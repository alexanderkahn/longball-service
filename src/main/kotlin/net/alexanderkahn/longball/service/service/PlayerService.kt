package net.alexanderkahn.longball.service.service

import net.alexanderkahn.longball.service.model.Player
import net.alexanderkahn.longball.service.persistence.PlayerRepository
import net.alexanderkahn.longball.service.persistence.assembler.PlayerAssembler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class PlayerService(@Autowired private val playerRepository: PlayerRepository) {

    private val playerAssembler: PlayerAssembler = PlayerAssembler()

    fun getAll(pageable: Pageable): Page<Player> {
        val players = playerRepository.findAll(pageable)
        return players.map { playerAssembler.toModel(it) }
    }

    fun get(id: String): Player {
        val player = playerRepository.findOne(id)
        return playerAssembler.toModel(player)
    }
}