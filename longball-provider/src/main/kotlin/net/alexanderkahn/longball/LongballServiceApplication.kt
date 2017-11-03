package net.alexanderkahn.longball

import net.alexanderkahn.service.base.ServiceApplicationBase
import org.springframework.boot.autoconfigure.SpringBootApplication

//TODO: break up the base into discrete dependencies, instantiate them here and get rid of the service base scanning
@SpringBootApplication(scanBasePackages = arrayOf(
        "net.alexanderkahn.longball",
        "net.alexanderkahn.service.base"))
open class LongballServiceApplication: ServiceApplicationBase() {

    companion object {
        @JvmStatic fun main(args: Array<String>) {
            LongballServiceApplication().startService(args)
        }
    }
}