package net.alexanderkahn.longball.service.persistence.assembler

interface Assembler<M, P> {
    fun toModel(persistenceObject: P): M
    fun toPersistence(modelObject: M): P
}