package net.alexanderkahn.longball.provider.service

import net.alexanderkahn.longball.model.Person
import net.alexanderkahn.longball.provider.assembler.pxUser
import net.alexanderkahn.longball.provider.assembler.toModel
import net.alexanderkahn.longball.provider.assembler.toPersistence
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

    override fun getAll(pageable: Pageable): Page<Person> {
        val players = personRepository.findByOwner(pageable, UserContext.pxUser)
        return players.map { it.toModel() }
    }

    override fun get(id: UUID): Person {
        val player = personRepository.findByIdAndOwner(id, UserContext.pxUser)
        return player?.toModel() ?: throw NotFoundException("players", id)
    }

    override fun save(person: Person): Person {
        val entity = person.toPersistence()
        personRepository.save(entity)
        return entity.toModel()
    }

    override fun delete(id: UUID) {
        if (!personRepository.exists(id)) {
            throw NotFoundException("people", id)
        }
        personRepository.delete(id)
    }
}