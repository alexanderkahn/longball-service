package net.alexanderkahn.longball.provider.service


import net.alexanderkahn.longball.model.dto.InningDTO
import net.alexanderkahn.longball.model.dto.InningSideDTO
import net.alexanderkahn.longball.model.type.Side
import net.alexanderkahn.longball.provider.assembler.InningAssembler
import net.alexanderkahn.longball.provider.entity.InningEntity
import net.alexanderkahn.longball.provider.entity.InningSideEntity
import net.alexanderkahn.longball.provider.repository.InningRepository
import net.alexanderkahn.longball.provider.repository.InningSideRepository
import net.alexanderkahn.service.longball.api.IInningService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*

@Service
class InningService @Autowired constructor(
        private val userService: UserService,
        private val gameService: GameService,
        private val inningRepository: InningRepository,
        private val inningAssembler: InningAssembler,
        private val inningSideRepository: InningSideRepository) : IInningService {

    override fun getInningsForGame(pageable: Pageable, gameId: UUID): Page<InningDTO> {
        val game = gameService.getPxGame(gameId)
        return inningRepository.findByOwnerAndGame(pageable, userService.embeddableUser(), game).map { inningAssembler.toDTO(it) }
    }

    override fun advanceInning(gameId: UUID) {
        val game = gameService.getPxGame(gameId)
        val lastInning = inningRepository.findFirstByOwnerAndGameOrderByIdDesc(userService.embeddableUser(), game)
        if (lastInning == null) {
            val firstInning = InningEntity(game, 1, userService.embeddableUser())
            inningRepository.save(firstInning)
            inningSideRepository.save(InningSideEntity(firstInning, Side.TOP, userService.embeddableUser()))
        } else {
            val sides = inningSideRepository.findByInningAndOwner(lastInning, userService.embeddableUser())
            if (sides.size >= Side.values().size) {
                val nextInning = InningEntity(game, lastInning.inningNumber.inc(), userService.embeddableUser())
                inningRepository.save(nextInning)
                inningSideRepository.save(InningSideEntity(nextInning, Side.TOP, userService.embeddableUser()))
            } else {
                inningSideRepository.save(InningSideEntity(lastInning, Side.BOTTOM, userService.embeddableUser()))
            }
        }
    }

    override fun getInning(gameId: UUID, inningNumber: Int): InningDTO {
        val game = gameService.getPxGame(gameId)
        return inningRepository.findByOwnerAndGameAndInningNumber(userService.embeddableUser(), game, inningNumber)?.let { inningAssembler.toDTO(it) }
                ?: throw Exception("Inning $inningNumber not found for game $gameId")
    }

    override fun getInningSide(gameId: UUID, inningNumber: Int, inningSide: Side): InningSideDTO {
        val returnedSide: InningSideDTO? = getInning(gameId, inningNumber).let { if (inningSide == Side.TOP) it.top else it.bottom }
        return returnedSide ?: throw Exception("Could not find $inningSide $inningNumber for game $gameId")
    }
}