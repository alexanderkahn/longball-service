package net.alexanderkahn.longball.service

import net.alexanderkahn.base.servicebase.ServiceApplicationBase
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication(scanBasePackages = arrayOf("net.alexanderkahn"))
open class LongballServiceApplication: ServiceApplicationBase() {

    companion object {
        @JvmStatic fun main(args: Array<String>) {
            LongballServiceApplication().run(args)
        }
    }
}