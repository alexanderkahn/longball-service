package net.alexanderkahn.longball.service.persistence.model


import javax.persistence.Embeddable

@Embeddable
data class EmbeddableUser(val issuer: String, val userId: String)