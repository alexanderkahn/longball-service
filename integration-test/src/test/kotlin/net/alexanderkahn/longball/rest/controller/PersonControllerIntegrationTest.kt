package net.alexanderkahn.longball.rest.controller

import junit.framework.TestCase.assertEquals
import net.alexanderkahn.longball.itest.AbstractBypassTokenIntegrationTest
import net.alexanderkahn.longball.model.PersonAttributes
import net.alexanderkahn.longball.model.RequestPerson
import net.alexanderkahn.longball.model.ResponsePerson
import net.alexanderkahn.longball.model.ResponseTeam
import net.alexanderkahn.longball.core.entity.PersonEntity
import net.alexanderkahn.longball.core.repository.PersonRepository
import net.alexanderkahn.service.commons.model.request.body.ObjectRequest
import org.apache.http.HttpStatus
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired


class PersonControllerIntegrationTest : AbstractBypassTokenIntegrationTest() {

    @Autowired private lateinit var personRepository: PersonRepository

    @BeforeEach
    fun setUp() {
        personRepository.deleteAll()
    }

    @Test
    fun post() {
        val requestPerson = RequestPerson("people", PersonAttributes("Vladimir", "Guerrero"))
        val response = withBypassToken().body(ObjectRequest(requestPerson))
                .`when`().post("/people")
                .then().statusCode(HttpStatus.SC_CREATED)
                .extract().jsonPath().getObject("data", ResponsePerson::class.java)

        assertEquals(requestPerson.type, response.type)
        assertEquals(requestPerson.attributes, response.attributes)
    }

    @Test
    fun postWrongType() {
        val requestPerson = RequestPerson("purple", PersonAttributes("Valdomar", "Gardrango"))
        withBypassToken().body(ObjectRequest(requestPerson))
                .`when`().post("/people")
                .then().statusCode(HttpStatus.SC_CONFLICT)
    }


    @Test
    fun delete() {
        val entityToDelete = PersonEntity("Alex", "Rodriguez", userEntity)
        personRepository.save(entityToDelete)
        withBypassToken().`when`().delete("/people/${entityToDelete.id}")
                .then().statusCode(HttpStatus.SC_OK)

        withBypassToken().`when`().get("/people/${entityToDelete.id}")
                .then().statusCode(HttpStatus.SC_NOT_FOUND)
    }

    @Test
    fun getOne() {
        val requestPerson = PersonEntity("Ichiro", "Suzuki", userEntity)
        personRepository.save(requestPerson)
        val returnedPerson = withBypassToken().`when`().get("/people/${requestPerson.id}")
                .then().statusCode(HttpStatus.SC_OK)
                .extract().jsonPath().getObject("data", ResponsePerson::class.java)

        assertEquals(requestPerson.first, returnedPerson.attributes.first)
        assertEquals(requestPerson.last, returnedPerson.attributes.last)
    }

    @Test
    fun getCollection() {
        listOf("Yadier", "Bengie", "Jose").map { first -> PersonEntity(first, "Molina", userEntity) }.forEach { personRepository.save(it) }
        val getResponse = withBypassToken().`when`().get("/people")
                .then().statusCode(HttpStatus.SC_OK)
                .extract().response().jsonPath()

        assertEquals(3, getResponse.getList<ResponseTeam>("data").size)
    }
}