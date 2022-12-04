CREATE TABLE `user`
(
    `user_id`          BIGINT auto_increment primary key                              not null,
    `userId`           varchar(30)                                                    NOT NULL,
    `name`             VARCHAR(30)                                                    NOT NULL,
    `password`         VARCHAR(255)                                                   NOT NULL,
    `email`            VARCHAR(50)                                                    NOT NULL,
    `weight`           DOUBLE                                                         NULL,
    `withdrawalReason` VARCHAR(200)                                                   NULL,
    `joinDate`         DATETIME                             default current_timestamp NOT NULL,
    `withdrawalState`  enum ('withdrawal', 'notwithdrawal') default 'notwithdrawal'   NOT NULL,
    `userStatus`       ENUM ('user' , 'provider', 'admin')  default 'user'            NOT NULL,
    `judgeStatus`      ENUM ('pending','passed', 'rejected')                          NULL,
    `photoLink`        VARCHAR(255)                                                   NULL
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
    `post_id`  BIGINT auto_increment primary key NOT NULL,
    `subject`  VARCHAR(50)                       NOT NULL,
    `contents` VARCHAR(1000)                     NOT NULL,
    `user_id`  BIGINT                            NOT NULL
);

CREATE TABLE `comment`
(
    `comment_id`  BIGINT auto_increment primary key NOT NULL,
    `board_id`    BIGINT                            NOT NULL,
    `contents`    VARCHAR(300)                      NOT NULL,
    `depth`       INT                               NOT NULL COMMENT '댓글의 현재 깊이가 몇인지',
    `comment_id3` BIGINT                            NULL,
    `user_id`     BIGINT                            NOT NULL
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