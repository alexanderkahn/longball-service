package net.alexanderkahn.longball

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import java.util.*
import javax.annotation.PostConstruct

//TODO: get rid of the service base scanning
@SpringBootApplication
open class LongballServiceApplication {

    @PostConstruct
    fun configureEnvironment() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
        //TODO: set up firebase?
    }

    companion object {
        @JvmStatic fun main(args: Array<String>) {
            SpringApplication.run(LongballServiceApplication::class.java, *args)
        }
    }
}