package booklist2

import io.micronaut.runtime.Micronaut

object Application {

    @JvmStatic
    fun main(args: Array<String>) {
        Micronaut.build()
                .packages("booklist2")
                .mainClass(Application.javaClass)
                .start()
    }
}