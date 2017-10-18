package net.alexanderkahn.longball.provider.service

import net.alexanderkahn.longball.model.dto.PersonDTO
import net.alexanderkahn.longball.provider.assembler.embeddableUser
import net.alexanderkahn.longball.provider.assembler.toDTO
import net.alexanderkahn.longball.provider.assembler.toEntity
import net.alexanderkahn.longball.provider.repository.PersonRepository
import net.alexanderkahn.service.base.api.exception.NotFoundException
import net.alexanderkahn.service.base.api.security.UserContext
import net.alexanderkahn.service.longball.api.IPersonService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*

@Service
class PersonService(@Autowired private val personRepository: PersonRepository) : IPersonService {

    override fun getAll(pageable: Pageable): Page<PersonDTO> {
        val players = personRepository.findByOwner(pageable, UserContext.embeddableUser)
        return players.map { it.toDTO() }
    }

    override fun get(id: UUID): PersonDTO {
        val player = personRepository.findByIdAndOwner(id, UserContext.embeddableUser)
        return player?.toDTO() ?: throw NotFoundException("players", id)
    }

    override fun save(person: PersonDTO): PersonDTO {
        val entity = person.toEntity()
        personRepository.save(entity)
        return entity.toDTO()
    }

    override fun delete(id: UUID) {
        if (!personRepository.exists(id)) {
            throw NotFoundException("people", id)
        }
        personRepository.delete(id)
    }
}