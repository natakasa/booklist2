０．環境
Micronaut Version: 1.2.7
JVM Version: 11
postgre: 12.1

１．postgreに下記DBを作成
booklist

２．上記で作成したDBに対し下記の初期化
.\docker\initdb.d\init.sql

３．下記コマンドでサーバを起動
gradlew run

４．下記URLにアクセス
http://localhost:8080/book/find/list/all