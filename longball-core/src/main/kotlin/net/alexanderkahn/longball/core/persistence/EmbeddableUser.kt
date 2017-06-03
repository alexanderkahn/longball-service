package net.alexanderkahn.longball.core.persistence


@javax.persistence.Embeddable
data class EmbeddableUser(val issuer: String, val userId: String)