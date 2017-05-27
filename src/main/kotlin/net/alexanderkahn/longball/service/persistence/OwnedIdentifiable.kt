package net.alexanderkahn.longball.service.persistence

interface OwnedIdentifiable {
    val id: Long?
    val owner: EmbeddableUser
}