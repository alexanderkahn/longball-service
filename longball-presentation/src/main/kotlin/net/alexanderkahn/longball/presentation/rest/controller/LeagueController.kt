package net.alexanderkahn.longball.presentation.rest.controller

import net.alexanderkahn.longball.api.service.ILeagueService
import net.alexanderkahn.longball.model.dto.LeagueAttributes
import net.alexanderkahn.longball.model.dto.RequestLeague
import net.alexanderkahn.longball.model.dto.ResponseLeague
import net.alexanderkahn.longball.model.dto.toCollectionResponse
import net.alexanderkahn.longball.presentation.rest.helper.getSearch
import net.alexanderkahn.longball.presentation.rest.helper.getSearchableFieldsFor
import net.alexanderkahn.service.commons.model.request.ObjectRequest
import net.alexanderkahn.service.commons.model.response.CollectionResponse
import net.alexanderkahn.service.commons.model.response.CreatedResponse
import net.alexanderkahn.service.commons.model.response.DeletedResponse
import net.alexanderkahn.service.commons.model.response.ObjectResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/leagues")
class LeagueController(@Autowired private val leagueService: ILeagueService) {

    private val validLeagueSearchFields = getSearchableFieldsFor(LeagueAttributes::class)

    @GetMapping
    fun getLeagues(pageable: Pageable, @RequestParam(required = false) queryParams: MultiValueMap<String, String>?): CollectionResponse<ResponseLeague> {
        val search = getSearch(queryParams, validLeagueSearchFields)
        val page = leagueService.getAll(pageable, search)
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