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
    `user_status`       VARCHAR(30) default '01'              NOT NULL COMMENT '01:정상상태,02:탈퇴상태',
    `user_type`         VARCHAR(30) default '01'              NOT NULL COMMENT '01:일반,02:제공자,03:관리자',
    `judge_status`      VARCHAR(30)                           NULL COMMENT '프로바이더 특성, 심사상태',
    `photo_link`        VARCHAR(255)                          NULL COMMENT '프로바이더 특성'
);
drop table if exists food;

CREATE TABLE `food`
(
    `food_id`   BIGINT auto_increment primary key NOT NULL,
    `name`      VARCHAR(50)                       NOT NULL,
    `approve`   INT                               NOT NULL COMMENT '심사승인상태',
    `denyState` INT                               NULL,
    `user_id`   BIGINT                            NOT NULL
);
drop table if exists feed;

CREATE TABLE `feed`
(
    `feed_id`   BIGINT auto_increment primary key  NOT NULL,
    `contents`  VARCHAR(1000)                      NULL,
    `writeDate` DATETIME default current_timestamp NOT NULL,
    `user_id`   BIGINT                             NOT NULL
);

drop table if exists comment;
CREATE TABLE `comment`
(
    `comment_id`     BIGINT auto_increment primary key  NOT NULL,
    `feed_id`        BIGINT                             NOT NULL,
    `contents`       VARCHAR(300)                       NOT NULL,
    `writeDate`      DATETIME default current_timestamp NOT NULL,
    `parent_id`      BIGINT                             NULL,
    `depth`          INT      default 0                 NOT NULL,
    `user_id`        BIGINT                             NOT NULL,
    `group_number`   INT                                NOT NULL,
    `group_reforder` int      default 1                 NOT NULL,
    `child_number`   int      default 0                 NOT NULL
);

drop table if exists nutrition;
CREATE TABLE `nutrition`
(
    `id`          VARCHAR(255) primary key NOT NULL COMMENT '키는 무엇이 되어야 할까요?',
    `name`        VARCHAR(255)             NOT NULL,
    `description` VARCHAR(255)             NOT NULL,
    `metric`      VARCHAR(50)              NOT NULL
);

drop table if exists food_save;
CREATE TABLE `food_save`
(
    `user_id`  BIGINT                                 NOT NULL,
    `food_id`  BIGINT                                 NOT NULL,
    `quantity` INT                                    NOT NULL COMMENT '개수선택',
    `weight`   Enum ('g', 'cup', 'servings', 'ounce') NOT NULL COMMENT 'g, 인분, 온스,1컵 등 카테고리 선택'
);
drop table if exists good;
CREATE TABLE `good`
(
    `Key`        VARCHAR(255) primary key NOT NULL,
    `post_id`    BIGINT                   NOT NULL,
    `user_id3`   BIGINT                   NOT NULL,
    `like_state` BOOLEAN                  NOT NULL
);
drop table if exists admin;
CREATE TABLE `admin`
(
    `Key`   VARCHAR(255) primary key NOT NULL,
    `Field` VARCHAR(255)             NOT NULL
);
drop table if exists user_login;
CREATE TABLE `user_login`
(
    `userlogin_id` VARCHAR(255) primary key NOT NULL,
    `user_id`      BIGINT                   NOT NULL,
    `attempts`     TINYINT                  NOT NULL COMMENT '로그인시도 횟수',
    `time_limit`   DATETIME                 NULL COMMENT '횟수초과시 시간제한',
    `last_login`   DATETIME                 NULL,
    `lock_status`  TINYINT                  NULL COMMENT '로그인 실패로 잠금상태인지',
    `lock_date`    DATETIME                 NULL COMMENT '로그인 실패로 잠긴시간'
);
drop table if exists food_nutrition;
CREATE TABLE `food_nutrition`
(
    `food_id`      BIGINT       NOT NULL,
    `nutrition_id` VARCHAR(255) NOT NULL COMMENT '키는 무엇이 되어야 할까요?',
    `kcal`         BIGINT       NOT NULL
);
drop table if exists user_pw_log;
CREATE TABLE `user_pw_log`
(
    `userlogin_id` BIGINT auto_increment primary key NOT NULL,
    `user_id`      BIGINT                            NOT NULL,
    `last_login`   DATETIME                          NULL,
    `Field`        DATETIME                          NULL
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

drop table if exists `likes`;
CREATE TABLE `likes`
(
    `likes_id`    BIGINT auto_increment primary key NOT NULL,
    `feed_id`     BIGINT                            NOT NULL,
    `user_id`     BIGINT                            NOT NULL,
    `likes_state` VARCHAR(30)                       NOT NULL COMMENT '활성화, 비활성화'
);


insert into user_table(user_id, user_name, user_password, email, weight)
values ('mockUser', '이영진', '$2a$10$uPM5cI9oLxVlppZLDrkxCOwnj/IJBi0kltM2gdrfVibA9m05hK3M2', 'dudwls0505@naver.com',
        50.3);

insert into user_table(user_id, user_name, user_password, email, weight)
values ('wrongUser', '김영진', '$2a$10$uPM5cI9oLxVlppZLDrkxCOwnj/IJBi0kltM2gdrfVibA9m05hK3M2', 'dudwls0505@nate.com',
        50.3);