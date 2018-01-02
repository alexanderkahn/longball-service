package net.alexanderkahn.longball.presentation.rest.controller

import net.alexanderkahn.longball.api.service.ILeagueService
import net.alexanderkahn.longball.model.dto.RequestLeague
import net.alexanderkahn.longball.model.dto.ResponseLeague
import net.alexanderkahn.longball.model.dto.toCollectionResponse
import net.alexanderkahn.service.base.model.request.ObjectRequest
import net.alexanderkahn.service.base.model.response.CollectionResponse
import net.alexanderkahn.service.base.model.response.CreatedResponse
import net.alexanderkahn.service.base.model.response.DeletedResponse
import net.alexanderkahn.service.base.model.response.ObjectResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/leagues")
class LeagueController(@Autowired private val leagueService: ILeagueService) {

    @GetMapping
    fun getLeagues(pageable: Pageable, @RequestParam("filter[name]", required = false) nameFilter: String? = null): CollectionResponse<ResponseLeague> {
        val page = leagueService.getAll(pageable, nameFilter)
        return page.toCollectionResponse()
    }

    @PostMapping
    fun addLeague(@RequestBody leagueRequest: ObjectRequest<RequestLeague>): CreatedResponse<ResponseLeague> {
        leagueRequest.data.validate()
        val created = leagueService.save(leagueRequest.data)
        return CreatedResponse(created)
    }

    @GetMapping("/{id}")
    fun getLeague(@PathVariable id: UUID): ObjectResponse<ResponseLeague> {
        val league = leagueService.get(id)
        return ObjectResponse(league)
    }

    @DeleteMapping("/{id}")
    fun deleteLeague(@PathVariable id: UUID): DeletedResponse {
        leagueService.delete(id)
        return DeletedResponse()
    }
}