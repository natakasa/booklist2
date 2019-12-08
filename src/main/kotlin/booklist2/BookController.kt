package booklist2

import booklist2.bean.Book
import booklist2.repository.BookRepository
import io.micronaut.core.util.CollectionUtils
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpResponse.ok
import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.*
import io.micronaut.views.ModelAndView
import io.micronaut.views.View;
import java.util.ArrayList
import javax.validation.Valid

@Controller("/book")
class BookController {

    @Get("/")
    fun index(): HttpStatus {
        return HttpStatus.OK
    }
    @Get("/find")
    @View("update")
    fun find(@QueryValue id : Int) = HttpResponse.ok(BookRepository().find(id))

    // todo フロントからformで/find/idと渡すことができればこっちを使いたい
    @Get("/find/{id}")
    @View("update")
    fun find2(id : Int) = HttpResponse.ok(BookRepository().find(id))

    @Get("/find/list/all")
    @View("find")
    fun findList() = HttpResponse.ok(CollectionUtils.mapOf("book",BookRepository().findList("")))

    @Get("/find/list")
    @View("find")
    fun findListSearch(@QueryValue namepart : String) = HttpResponse.ok(CollectionUtils.mapOf("book",BookRepository().findList(namepart)))

    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Post("/update")
    @View("update")
    fun update(@Body book : Book)=  HttpResponse.ok(BookRepository().update(book))

}