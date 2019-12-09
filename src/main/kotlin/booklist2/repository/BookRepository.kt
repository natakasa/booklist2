package booklist2.repository

import booklist2.bean.Book
import io.reactiverse.kotlin.pgclient.PgPoolOptions
import io.reactivex.Single
import java.util.*
import javax.inject.Inject
import java.util.ArrayList
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import io.reactiverse.pgclient.PgPoolOptions
import io.reactiverse.reactivex.pgclient.*
import io.vertx.reactivex.impl.AsyncResultSingle
import java.sql.PreparedStatement

class BookRepository {

    var options = PgPoolOptions()
    .setPort(5432)
    .setHost("localhost")
    .setDatabase("booklist")
    .setUser("postgres")
    .setPassword("admin")
    .setMaxSize(5) // todo application.ymlからinjectできなかったため暫定

    var client = PgClient.pool(options) // todo application.ymlからinjectできなかったため暫定

    // 複数件取得
    fun findList(namepart : String): ArrayList<Book> {

        System.out.println("repo findList:" + "start")
        var result = ArrayList<Book>()
        var sqlStr = ""

        if(namepart.isEmpty()){
            // 検索条件なしの場合
            sqlStr = "SELECT * FROM t_book where del_flg = 0 order by updated_at desc"
        } else {
            // 検索条件ありの場合
            sqlStr = "SELECT * FROM t_book where name like \'%" + namepart.trim() + "%\' and del_flg = 0 order by updated_at desc"
        }

        System.out.println("repo findList:" + sqlStr)

        val ret = client.rxQuery(sqlStr).map { pgRowSet ->
            val ite = pgRowSet.iterator()
            while (ite.hasNext()) {
                val row = ite.next()
                var addItem = Book()
                System.out.println("repo findList:" + row.getInteger("id"))
                addItem.id = row.getInteger("id")
                addItem.name = row.getString("name")
                addItem.author = row.getString("author")
                addItem.description = row.getString("description")
                addItem.delFlg = row.getInteger("del_flg")
                result.add(addItem)
            }
            result
        }.blockingGet().stream()

        return  result
    }

    // 1件取得
    fun find(id : Int) : Book {

        System.out.println("repo find:" + "start")

        var result = Book()

        client.rxPreparedQuery("SELECT * FROM t_book where id = $1 and del_flg = 0",
                Tuple.of(id)).map { pgRowSet ->
            if ( pgRowSet.count() >= 1 ) {
                System.out.println("repo find:" + pgRowSet.first().getInteger("id"))
                result.id = pgRowSet.first().getInteger("id")
                result.name = pgRowSet.first().getString("name")
                result.author = pgRowSet.first().getString("author")
                result.description = pgRowSet.first().getString("description")
                result.delFlg = pgRowSet.first().getInteger("del_flg")
            }
            result
        }.blockingGet()

        return result
    }

    // 1件更新or登録
    fun update(book : Book) : Book{

        System.out.println("repo update:" + "start")

        var result = Book()

        if( book.id != null ) {
            // 更新
            System.out.println("repo update:" + "id:" + book.id)

            var ret = client.rxPreparedQuery("" +
                    "update t_book " +
                    "set name = $1, author = $2, description = $3, del_flg = $4, updated_at = CURRENT_TIMESTAMP " +
                    "where id = $5",
                    Tuple.of(book.name, book.author, book.description, if(book.delFlg==null) 0 else 1, book.id)).flatMap { ret ->
                client.rxPreparedQuery("" +
                        "SELECT * FROM t_book " +
                        "where id = $1",
                        Tuple.of(book.id))
            }.map { pgRowSet ->
                System.out.println("repo update:" + pgRowSet.first().getInteger("id"))
                result.id = pgRowSet.first().getInteger("id")
                result.name = pgRowSet.first().getString("name")
                result.author = pgRowSet.first().getString("author")
                result.description = pgRowSet.first().getString("description")
                result.delFlg = pgRowSet.first().getInteger("del_flg")
                result
            }.blockingGet()
        }else{
            // 登録
            var ret = client.rxPreparedQuery(
                    "insert into t_book " +
                    "(name, author, description)" +
                    " values ($1, $2, $3)",
                    Tuple.of(book.name, book.author, book.description)).flatMap { ret ->
                client.rxQuery(
                        "SELECT id, name, author, description, del_flg FROM t_book order by id desc LIMIT 1 OFFSET 0")
            }.map { pgRowSet ->
                System.out.println("repo update:" + pgRowSet.first().getInteger("id"))
                result.id = pgRowSet.first().getInteger("id")
                result.name = pgRowSet.first().getString("name")
                result.author = pgRowSet.first().getString("author")
                result.description = pgRowSet.first().getString("description")
                result.delFlg = pgRowSet.first().getInteger("del_flg")
                result
            }.blockingGet()
        }

        return result
    }

}