package net.alexanderkahn.longball.provider.service


import net.alexanderkahn.longball.model.Inning
import net.alexanderkahn.longball.model.InningSide
import net.alexanderkahn.longball.model.Side
import net.alexanderkahn.longball.provider.assembler.InningAssembler
import net.alexanderkahn.longball.provider.assembler.pxUser
import net.alexanderkahn.longball.provider.entity.InningEntity
import net.alexanderkahn.longball.provider.entity.InningSideEntity
import net.alexanderkahn.longball.provider.repository.InningRepository
import net.alexanderkahn.longball.provider.repository.InningSideRepository
import net.alexanderkahn.service.base.api.security.UserContext
import net.alexanderkahn.service.longball.api.IInningService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*

@Service
class InningService(
        @Autowired private val gameService: GameService,
        @Autowired private val inningRepository: InningRepository,
        @Autowired private val inningAssembler: InningAssembler,
        @Autowired private val inningSideRepository: InningSideRepository) : IInningService {

    override fun getInningsForGame(pageable: Pageable, gameId: UUID): Page<Inning> {
        val game = gameService.getPxGame(gameId)
        return inningRepository.findByOwnerAndGame(pageable, UserContext.pxUser, game).map { inningAssembler.toModel(it) }
    }

    override fun advanceInning(gameId: UUID) {
        val game = gameService.getPxGame(gameId)
        val lastInning = inningRepository.findFirstByOwnerAndGameOrderByIdDesc(UserContext.pxUser, game)
        if (lastInning == null) {
            val firstInning = InningEntity(game, 1)
            inningRepository.save(firstInning)
            inningSideRepository.save(InningSideEntity(firstInning, Side.TOP))
        } else {
            val sides = inningSideRepository.findByInningAndOwner(lastInning, UserContext.pxUser)
            if (sides.size >= Side.values().size) {
                val nextInning = InningEntity(game, lastInning.inningNumber.inc())
                inningRepository.save(nextInning)
                inningSideRepository.save(InningSideEntity(nextInning, Side.TOP))
            } else {
                inningSideRepository.save(InningSideEntity(lastInning, Side.BOTTOM))
            }
        }
    }

    override fun getInning(gameId: UUID, inningNumber: Int): Inning {
        val game = gameService.getPxGame(gameId)
        return inningRepository.findByOwnerAndGameAndInningNumber(UserContext.pxUser, game, inningNumber)?.let { inningAssembler.toModel(it) }
                ?: throw Exception("Inning $inningNumber not found for game $gameId")
    }

    override fun getInningSide(gameId: UUID, inningNumber: Int, inningSide: Side): InningSide {
        val returnedSide: InningSide? = getInning(gameId, inningNumber).let { if (inningSide == Side.TOP) it.top else it.bottom }
        return returnedSide ?: throw Exception("Could not find $inningSide $inningNumber for game $gameId")
    }
}