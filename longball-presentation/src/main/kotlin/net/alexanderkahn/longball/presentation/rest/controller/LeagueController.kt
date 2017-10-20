package net.alexanderkahn.longball.presentation.rest.controller

import net.alexanderkahn.longball.presentation.rest.model.*
import net.alexanderkahn.service.base.presentation.request.ObjectRequest
import net.alexanderkahn.service.base.presentation.response.CollectionResponse
import net.alexanderkahn.service.base.presentation.response.CreatedResponse
import net.alexanderkahn.service.base.presentation.response.DeletedResponse
import net.alexanderkahn.service.base.presentation.response.ObjectResponse
import net.alexanderkahn.service.longball.api.ILeagueService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/leagues")
class LeagueController(@Autowired private val leagueService: ILeagueService) {

    @GetMapping
    fun getLeagues(pageable: Pageable): CollectionResponse<ResponseLeague> {
        val page = leagueService.getAll(pageable)
        return page.map { it.toResponse() }.toCollectionResponse()
    }

    @PostMapping
    fun addLeague(@RequestBody leagueRequest: ObjectRequest<RequestLeague>): CreatedResponse<ResponseLeague> {
        leagueRequest.data.validate()
        val created = leagueService.save(leagueRequest.data.toDto())
        return CreatedResponse(created.toResponse())
    }

    @GetMapping("/{id}")
    fun getLeague(@PathVariable id: UUID): ObjectResponse<ResponseLeague> {
        val league = leagueService.get(id)
        return ObjectResponse(league.toResponse())
    }

    @DeleteMapping("/{id}")
    fun deleteLeague(@PathVariable id: UUID): DeletedResponse {
        leagueService.delete(id)
        return DeletedResponse()
    }
}