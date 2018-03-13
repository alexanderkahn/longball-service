
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.Test
import org.reflections.Reflections
import java.time.LocalDate
import javax.persistence.Entity


/*
There appears to be a bug when persisting LocalDate with Spring Boot/Liquibase/H2db configurations that causes
off-by-one errors. Until that's fixed or the cause discovered, we're working around it by persisting LocalDateTimes.
See https://stackoverflow.com/questions/48651828/spring-boot-repository-returns-wrong-value-when-persisting-localdate-with-liquib
*/
class EntityDateReflectionTest {

    private val disallowedTypes = setOf(LocalDate::class.java)

    @Test
    fun entitiesContainNoLocalDateFields() {
        val classesToScan = getEntityClasses()
        if (classesToScan === null || classesToScan.isEmpty()) {
            throw RuntimeException("Found no persistence entities to scan. Something has gone wrong!")
        }

        val failedFields = mutableListOf<String>()
        classesToScan.forEach { clazz ->
            clazz.declaredFields.forEach { field ->
                if (field.type in disallowedTypes) {
                    failedFields.add("${clazz.simpleName}.${field.name} [${field.type}]")
                }
            }
        }

        if (failedFields.isNotEmpty()) {
            fail<String>("Found entity fields of restricted types: ${failedFields.joinToString()}" )
        }
    }

    private fun getEntityClasses(): MutableSet<Class<*>>? {
        return Reflections("net.alexanderkahn.longball")
                .getTypesAnnotatedWith(Entity::class.java)
    }
}