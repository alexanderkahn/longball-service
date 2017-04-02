package net.alexanderkahn.longball.service.service

import net.alexanderkahn.base.servicebase.service.UserContext
import net.alexanderkahn.longball.service.model.Player
import net.alexanderkahn.longball.service.persistence.assembler.PlayerAssembler
import net.alexanderkahn.longball.service.persistence.repository.PlayerRepository
import net.alexanderkahn.longball.service.persistence.repository.getPersistenceUser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class PlayerService(@Autowired private val playerRepository: PlayerRepository) {

    private val playerAssembler: PlayerAssembler = PlayerAssembler()

    fun getAll(pageable: Pageable): Page<Player> {
        val players = playerRepository.findByOwner(pageable, UserContext.getPersistenceUser())
        return players.map { playerAssembler.toModel(it) }
    }

    fun get(id: Long): Player {
        val player = playerRepository.findByIdAndOwner(id, UserContext.getPersistenceUser())
        return playerAssembler.toModel(player)
    }
}