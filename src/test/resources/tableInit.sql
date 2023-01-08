drop table if exists user_table;

CREATE TABLE `user_table`
(
    `id`                BIGINT auto_increment primary key     not null,
    `user_id`           VARCHAR(30)                           NOT NULL,
    `user_name`         VARCHAR(30)                           NOT NULL,
    `user_password`     VARCHAR(255)                          NOT NULL,
    `email`             VARCHAR(50)                           NOT NULL,
    `weight`            DOUBLE                                NULL,
    `withdrawal_reason` VARCHAR(200)                          NULL,
    `join_date`         DATETIME    default current_timestamp NOT NULL,
    `user_status`       VARCHAR(30) default '정상상태'            NOT NULL COMMENT '탈퇴상태, 정상상태',
    `user_type`         VARCHAR(30)                           NOT NULL COMMENT '일반,제공자,관리자',
    `judge_status`      VARCHAR(30)                           NULL COMMENT '프로바이더 특성, 심사상태',
    `photo_link`        VARCHAR(255)                          NULL COMMENT '프로바이더 특성'
);

drop table if exists feed;

CREATE TABLE `feed`
(
    `feed_id`   BIGINT auto_increment primary key  NOT NULL,
    `contents`  VARCHAR(1000)                      NULL,
    `writeDate` DATETIME default current_timestamp NOT NULL,
    `user_id`   BIGINT                             NOT NULL
);

drop table if exists photo;
CREATE TABLE `photo`
(
    `photo_id`    BIGINT auto_increment primary key  NOT NULL,
    `photo_name`  VARCHAR(50)                        not null,
    `upload_date` DATETIME default current_timestamp NOT NULL,
    `photo_path`  VARCHAR(100)                       NOT NULL,
    `feed_id`     BIGINT                             NOT NULL
);