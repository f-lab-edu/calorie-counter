CREATE TABLE `user_table`
(
    `id`                BIGINT auto_increment primary key     not null,
    `user_id`           VARCHAR(30)                           NOT NULL,
    `user_name`         VARCHAR(30)                           NOT NULL,
    `user_password`     VARCHAR(255)                          NOT NULL,
    `email`             VARCHAR(50)                           NOT NULL,
    `weight`            DOUBLE                                NULL,
    `withdrawal_reason` VARCHAR(200)                          NULL,
    `join_date`          DATETIME    default current_timestamp NOT NULL,
    `user_status`       VARCHAR(30) default '정상상태'            NOT NULL COMMENT '탈퇴상태, 정상상태',
    `user_type`         VARCHAR(30)                           NOT NULL COMMENT '일반,제공자,관리자',
    `judge_status`      VARCHAR(30)                           NULL COMMENT '프로바이더 특성, 심사상태',
    `photo_link`        VARCHAR(255)                          NULL COMMENT '프로바이더 특성'
);

CREATE TABLE `food`
(
    `food_id`   BIGINT auto_increment primary key NOT NULL,
    `name`      VARCHAR(50)                       NOT NULL,
    `approve`   INT                               NOT NULL COMMENT '심사승인상태',
    `denyState` INT                               NULL,
    `user_id`   BIGINT                            NOT NULL
);

CREATE TABLE `post`
(
    `post_id`   BIGINT auto_increment primary key  NOT NULL,
    `contents`  VARCHAR(1000)                      NOT NULL,
    `writeDate` DATETIME default current_timestamp NOT NULL,
    `user_id`   BIGINT                             NOT NULL
);

CREATE TABLE `comment`
(
    `comment_id`  BIGINT auto_increment primary key  NOT NULL,
    `board_id`    BIGINT                             NOT NULL,
    `contents`    VARCHAR(300)                       NOT NULL,
    `depth`       INT                                NOT NULL COMMENT '댓글의 현재 깊이가 몇인지',
    `writeDate`   DATETIME default current_timestamp NOT NULL,
    `comment_id3` BIGINT                             NULL,
    `user_id`     BIGINT                             NOT NULL
);

CREATE TABLE `nutrition`
(
    `id`          VARCHAR(255) primary key NOT NULL COMMENT '키는 무엇이 되어야 할까요?',
    `name`        VARCHAR(255)             NOT NULL,
    `description` VARCHAR(255)             NOT NULL,
    `metric`      VARCHAR(50)              NOT NULL
);

CREATE TABLE `food_save`
(
    `user_id`  BIGINT                                 NOT NULL,
    `food_id`  BIGINT                                 NOT NULL,
    `quantity` INT                                    NOT NULL COMMENT '개수선택',
    `weight`   Enum ('g', 'cup', 'servings', 'ounce') NOT NULL COMMENT 'g, 인분, 온스,1컵 등 카테고리 선택'
);

CREATE TABLE `like`
(
    `Key`        VARCHAR(255) primary key NOT NULL,
    `post_id`    BIGINT                   NOT NULL,
    `user_id3`   BIGINT                   NOT NULL,
    `like_state` BOOLEAN                  NOT NULL
);

CREATE TABLE `admin`
(
    `Key`   VARCHAR(255) primary key NOT NULL,
    `Field` VARCHAR(255)             NOT NULL
);

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

CREATE TABLE `food_nutrition`
(
    `food_id`      BIGINT       NOT NULL,
    `nutrition_id` VARCHAR(255) NOT NULL COMMENT '키는 무엇이 되어야 할까요?',
    `kcal`         BIGINT       NOT NULL
);

CREATE TABLE `user_pw_log`
(
    `userlogin_id` BIGINT auto_increment primary key NOT NULL,
    `user_id`      BIGINT                            NOT NULL,
    `last_login`   DATETIME                          NULL,
    `Field`        DATETIME                          NULL
);

CREATE TABLE `photo`
(
    `Key`         BIGINT auto_increment primary key NOT NULL,
    `post_id`     BIGINT                            NOT NULL,
    `upload_date` DATETIME                          NOT NULL,
    `path`        VARCHAR(100)                      NOT NULL
);

CREATE TABLE `feed`
(
    `feed_id`   BIGINT auto_increment primary key  NOT NULL,
    `contents`  VARCHAR(40000)                      NOT NULL,
    `created_date` DATETIME default current_timestamp NOT NULL,
    `user_id`   BIGINT                             NOT NULL
);
