package net.alexanderkahn.longball.core

import org.reflections.Reflections
import javax.persistence.Entity

fun getEntityClasses(): MutableSet<Class<*>> {
    val entityTypes = Reflections("net.alexanderkahn.longball").getTypesAnnotatedWith(Entity::class.java)
    if (entityTypes === null || entityTypes.isEmpty()) {
        throw RuntimeException("Found no persistence entities to scan. Something has gone wrong!")
    }
    return entityTypes
}