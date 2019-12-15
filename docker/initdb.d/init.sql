CREATE TABLE IF NOT EXISTS t_book(id    SERIAL    NOT NULL,
name   TEXT       NOT NULL,
author    TEXT    NOT NULL,
description    TEXT,
del_flg INTEGER NOT NULL DEFAULT 0,
created_at timestamp not null default current_timestamp,
updated_at timestamp not null default current_timestamp,
PRIMARY KEY (id));

insert into t_book (name, author, description) values ('文明退化の音がする', 'デーブ・スペクター', '');
insert into t_book (name, author, description) values ('ANAの気づかい', 'ANAビジネスソリューション', 'ANAで実際に活用されている気づかい術について');
insert into t_book (name, author, description) values ('文明の衝突', 'サミュエル・ハンチントン', '西欧への挑戦を続ける「儒教―イスラム・コネクション」は核拡散の深刻な危機を招くのか?');
