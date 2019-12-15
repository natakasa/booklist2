package booklist2

import io.micronaut.context.ApplicationContext
import io.micronaut.http.client.HttpClient
import io.micronaut.runtime.server.EmbeddedServer
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import kotlin.test.assertEquals

class BookControllerTest : Spek({

    describe("/book") {

        System.out.println("test:start")

        var embeddedServer: EmbeddedServer = ApplicationContext.run(EmbeddedServer::class.java)
        var client: HttpClient = HttpClient.create(embeddedServer.url)

        it("test /hello responds Hello World") {
            var rsp: String = client.toBlocking().retrieve("/book/find/list/all")
            assertEquals("Hello World", rsp)
        }

        afterGroup {
            client.close()
            embeddedServer.close()
        }
    }
})
