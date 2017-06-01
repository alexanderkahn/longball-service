package net.alexanderkahn.longball.core.service


import net.alexanderkahn.longball.core.assembler.InningAssembler
import net.alexanderkahn.longball.core.assembler.pxUser
import net.alexanderkahn.longball.model.Inning
import net.alexanderkahn.longball.model.Side
import net.alexanderkahn.longball.persistence.model.PxInning
import net.alexanderkahn.longball.persistence.model.PxInningSide
import net.alexanderkahn.longball.persistence.repository.GameRepository
import net.alexanderkahn.longball.persistence.repository.InningRepository
import net.alexanderkahn.longball.persistence.repository.InningSideRepository
import net.alexanderkahn.servicebase.core.security.UserContext
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
        val game = gameRepository.findByIdAndOwner(gameId, UserContext.pxUser)
        return inningRepository.findByGameAndOwner(pageable, game, UserContext.pxUser).map { inningAssembler.toModel(it) }
    }

    fun advanceInning(gameId: Long) {
        val game = gameRepository.findByIdAndOwner(gameId, UserContext.pxUser)
        val lastInning = inningRepository.findFirstByGameAndOwnerOrderByIdDesc(game, UserContext.pxUser)
        if (lastInning == null) {
            val firstInning = PxInning(UserContext.pxUser, game, 1)
            inningRepository.save(firstInning)
            inningSideRepository.save(PxInningSide(UserContext.pxUser, firstInning, Side.TOP.ordinal))
        } else {
            val sides = inningSideRepository.findByInningAndOwner(lastInning, UserContext.pxUser)
            if (sides.size >= Side.values().size) {
                val nextInning = PxInning(UserContext.pxUser, game, lastInning.inningNumber.inc())
                inningRepository.save(nextInning)
                inningSideRepository.save(PxInningSide(UserContext.pxUser, nextInning, Side.TOP.ordinal))
            } else {
                inningSideRepository.save(PxInningSide(UserContext.pxUser, lastInning, Side.BOTTOM.ordinal))
            }
        }
    }
}