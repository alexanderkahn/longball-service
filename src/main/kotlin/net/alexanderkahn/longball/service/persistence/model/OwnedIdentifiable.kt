package net.alexanderkahn.longball.service.persistence.model

interface OwnedIdentifiable {
    val id: Long?
    val owner: EmbeddableUser
}