package net.alexanderkahn.longball.provider.service

import net.alexanderkahn.longball.model.Player
import net.alexanderkahn.longball.provider.assembler.pxUser
import net.alexanderkahn.longball.provider.assembler.toModel
import net.alexanderkahn.longball.provider.repository.PlayerRepository
import net.alexanderkahn.service.base.api.exception.NotFoundException
import net.alexanderkahn.service.base.api.security.UserContext
import net.alexanderkahn.service.longball.api.IPlayerService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*

@Service
class PlayerService(@Autowired private val playerRepository: PlayerRepository) : IPlayerService {

    override fun getAll(pageable: Pageable): Page<Player> {
        val players = playerRepository.findByOwner(pageable, UserContext.pxUser)
        return players.map { it.toModel() }
    }

    override fun get(id: UUID): Player {
        val player = playerRepository.findByIdAndOwner(id, UserContext.pxUser)
        return player?.toModel() ?: throw NotFoundException("players", id)
    }
}