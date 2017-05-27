package net.alexanderkahn.longball.service.service

import net.alexanderkahn.longball.service.model.Inning
import net.alexanderkahn.longball.service.model.Side
import net.alexanderkahn.longball.service.persistence.entity.PxInning
import net.alexanderkahn.longball.service.persistence.entity.PxInningSide
import net.alexanderkahn.longball.service.persistence.repository.GameRepository
import net.alexanderkahn.longball.service.persistence.repository.InningRepository
import net.alexanderkahn.longball.service.persistence.repository.InningSideRepository
import net.alexanderkahn.longball.service.service.assembler.InningAssembler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class InningService(
        @Autowired private val gameRepository: GameRepository,
        @Autowired private val inningRepository: InningRepository,
        @Autowired private val inningAssembler: InningAssembler,
        @Autowired private val inningSideRepository: InningSideRepository) {

    fun getInningsForGame(pageable: Pageable, gameId: Long): Page<Inning> {
        val game = gameRepository.findByIdAndOwner(gameId)
        return inningRepository.findByGameAndOwner(pageable, game).map { inningAssembler.toModel(it) }
    }

    fun advanceInning(gameId: Long) {
        val game = gameRepository.findByIdAndOwner(gameId)
        val lastInning = inningRepository.findFirstByGameAndOwnerOrderByIdDesc(game)
        if (lastInning == null) {
            val firstInning = PxInning(game, 1)
            inningRepository.save(firstInning)
            inningSideRepository.save(PxInningSide(firstInning, Side.TOP))
        } else {
            val sides = inningSideRepository.findByInningAndOwner(lastInning)
            if (sides.size >= Side.values().size) {
                val nextInning = PxInning(game, lastInning.inningNumber.inc())
                inningRepository.save(nextInning)
                inningSideRepository.save(PxInningSide(nextInning, Side.TOP))
            } else {
                inningSideRepository.save(PxInningSide(lastInning, Side.BOTTOM))
            }
        }
    }
}