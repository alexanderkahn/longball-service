package net.alexanderkahn.longball

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import java.util.*
import javax.annotation.PostConstruct

@SpringBootApplication
open class LongballServiceApplication {

    @PostConstruct
    fun configureEnvironment() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
    }

    companion object {
        @JvmStatic fun main(args: Array<String>) {
            SpringApplication.run(LongballServiceApplication::class.java, *args)
        }
    }
}