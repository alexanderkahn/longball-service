package net.alexanderkahn.longball.model.dto

import net.alexanderkahn.service.base.model.response.body.data.ResponseResourceObject
import net.alexanderkahn.service.base.model.response.body.meta.ModifiableResourceMeta
import java.util.*

class ResponseUser(
    override val id: UUID,
    override val meta: ModifiableResourceMeta,
    override val attributes: UserAttributes
) : ResponseResourceObject {
    override val type = "users"
    override val relationships = null
}

class UserAttributes(val issuer: String, val username: String)