package net.alexanderkahn.longball.provider.service

import net.alexanderkahn.longball.api.service.IPersonService
import net.alexanderkahn.longball.model.dto.RequestPerson
import net.alexanderkahn.longball.model.dto.ResponsePerson
import net.alexanderkahn.longball.provider.assembler.PersonAssembler
import net.alexanderkahn.longball.provider.assembler.toResponse
import net.alexanderkahn.longball.provider.repository.PersonRepository
import net.alexanderkahn.service.base.model.exception.NotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*

@Service
class PersonService @Autowired constructor(
        private val userService: UserService,
        private val personRepository: PersonRepository,
        private val personAssembler: PersonAssembler
) : IPersonService {

    override fun getAll(pageable: Pageable): Page<ResponsePerson> {
        val players = personRepository.findByOwnerOrderByCreated(userService.userEntity(), pageable)
        return players.map { it.toResponse() }
    }

    override fun get(id: UUID): ResponsePerson {
        val player = personRepository.findByIdAndOwner(id, userService.userEntity())
        return player?.toResponse() ?: throw NotFoundException("players", id)
    }

    override fun save(person: RequestPerson): ResponsePerson {
        val entity = personAssembler.toEntity(person)
        personRepository.save(entity)
        return entity.toResponse()
    }

    override fun delete(id: UUID) {
        if (!personRepository.exists(id)) {
            throw NotFoundException("people", id)
        }
        personRepository.delete(id)
    }
}