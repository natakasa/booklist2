package booklist2

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.HttpStatus

@Controller("/test")
class TestController {

    @Get("/")
    fun index(): HttpStatus {
        return HttpStatus.OK
    }
}