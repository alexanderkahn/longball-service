package net.alexanderkahn.longball.model

import net.alexanderkahn.service.commons.model.response.body.data.ModifiableResourceObject
import net.alexanderkahn.service.commons.model.response.body.meta.ModifiableResourceMeta
import java.util.*

data class ResponseUser(
    override val id: UUID,
    override val meta: ModifiableResourceMeta,
    override val attributes: UserAttributes
) : ModifiableResourceObject {
    override val type = "users"
    override val relationships: Nothing? = null
}

data class UserAttributes(val issuer: String, val username: String, val displayName: String)