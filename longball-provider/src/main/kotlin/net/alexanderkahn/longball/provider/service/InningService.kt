package net.alexanderkahn.longball.provider.service


import net.alexanderkahn.longball.model.Inning
import net.alexanderkahn.longball.model.InningSide
import net.alexanderkahn.longball.model.Side
import net.alexanderkahn.longball.provider.assembler.InningAssembler
import net.alexanderkahn.longball.provider.assembler.pxUser
import net.alexanderkahn.longball.provider.persistence.model.PxInning
import net.alexanderkahn.longball.provider.persistence.model.PxInningSide
import net.alexanderkahn.longball.provider.persistence.repository.InningRepository
import net.alexanderkahn.longball.provider.persistence.repository.InningSideRepository
import net.alexanderkahn.service.base.api.security.UserContext
import net.alexanderkahn.service.longball.api.IInningService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class InningService(
        @Autowired private val gameService: GameService,
        @Autowired private val inningRepository: InningRepository,
        @Autowired private val inningAssembler: InningAssembler,
        @Autowired private val inningSideRepository: InningSideRepository) : IInningService {

    override fun getInningsForGame(pageable: Pageable, gameId: Long): Page<Inning> {
        val game = gameService.getPxGame(gameId)
        return inningRepository.findByOwnerAndGame(pageable, UserContext.pxUser, game).map { inningAssembler.toModel(it) }
    }

    override fun advanceInning(gameId: Long) {
        val game = gameService.getPxGame(gameId)
        val lastInning = inningRepository.findFirstByOwnerAndGameOrderByIdDesc(UserContext.pxUser, game)
        if (lastInning == null) {
            val firstInning = PxInning(UserContext.pxUser, game, 1)
            inningRepository.save(firstInning)
            inningSideRepository.save(PxInningSide(UserContext.pxUser, firstInning, Side.TOP))
        } else {
            val sides = inningSideRepository.findByInningAndOwner(lastInning, UserContext.pxUser)
            if (sides.size >= Side.values().size) {
                val nextInning = PxInning(UserContext.pxUser, game, lastInning.inningNumber.inc())
                inningRepository.save(nextInning)
                inningSideRepository.save(PxInningSide(UserContext.pxUser, nextInning, Side.TOP))
            } else {
                inningSideRepository.save(PxInningSide(UserContext.pxUser, lastInning, Side.BOTTOM))
            }
        }
    }

    override fun getInning(gameId: Long, inningNumber: Int): Inning {
        val game = gameService.getPxGame(gameId)
        return inningRepository.findByOwnerAndGameAndInningNumber(UserContext.pxUser, game, inningNumber)?.let { inningAssembler.toModel(it) }
                ?: throw Exception("Inning $inningNumber not found for game $gameId")
    }

    override fun getInningSide(gameId: Long, inningNumber: Int, inningSide: Side): InningSide {
        val returnedSide: InningSide? = getInning(gameId, inningNumber).let { if (inningSide == Side.TOP) it.top else it.bottom }
        return returnedSide ?: throw Exception("Could not find $inningSide $inningNumber for game $gameId")
    }
}