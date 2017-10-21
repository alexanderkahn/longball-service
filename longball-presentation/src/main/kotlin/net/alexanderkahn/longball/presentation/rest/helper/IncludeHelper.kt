package net.alexanderkahn.longball.presentation.rest.helper

import net.alexanderkahn.longball.presentation.rest.model.ModelTypes
import net.alexanderkahn.longball.presentation.rest.model.ModelTypes.PEOPLE
import net.alexanderkahn.longball.presentation.rest.model.toResponse
import net.alexanderkahn.service.base.presentation.response.body.data.ResponseResourceObject
import net.alexanderkahn.service.longball.api.IPersonService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*

@Component
class IncludeHelper(@Autowired private val personService: IPersonService) {

    fun include(typeToIdsMap: Map<ModelTypes, List<UUID>>): List<ResponseResourceObject> {
        //TODO: with UUID this sort doesn't really mean much. Would be nice to sort by created or something like that, but that's not a guaranteed part of the model
        return typeToIdsMap.flatMap { (type, ids) -> getResourceObjectsForType(type, ids.distinct()) }.sortedWith(compareBy({it.type}, {it.id}))
    }

    private fun getResourceObjectsForType(type: ModelTypes, ids: List<UUID>): List<ResponseResourceObject> {
        return when (type) {
            PEOPLE -> ids.map { personService.get(it).toResponse() }
            else -> listOf()
        }
    }
}