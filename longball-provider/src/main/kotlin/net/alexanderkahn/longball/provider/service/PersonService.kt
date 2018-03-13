package net.alexanderkahn.longball.provider.service

import net.alexanderkahn.longball.api.exception.ResourceNotFoundException
import net.alexanderkahn.longball.model.ModelTypes
import net.alexanderkahn.longball.model.RequestPerson
import net.alexanderkahn.longball.model.ResponsePerson
import net.alexanderkahn.longball.api.service.IPersonService
import net.alexanderkahn.longball.provider.assembler.PersonAssembler
import net.alexanderkahn.longball.provider.assembler.toResponse
import net.alexanderkahn.longball.provider.repository.PersonRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.validation.annotation.Validated
import java.util.*
import javax.validation.Valid

@Service
@Validated
open class PersonService @Autowired constructor(
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
        return player?.toResponse() ?: throw ResourceNotFoundException("players", id)
    }

    override fun save(@Valid person: RequestPerson): ResponsePerson {
        val entity = personAssembler.toEntity(person)
        personRepository.save(entity)
        return entity.toResponse()
    }

    override fun delete(id: UUID) {
        if (!personRepository.existsById(id)) {
            throw ResourceNotFoundException(ModelTypes.PEOPLE, id)
        }
        personRepository.deleteById(id)
    }
}