-- liquibase formatted sql

-- changeset eorion:1 dbms:mysql
create table if not exists COLLABORATION_COOPERATION_MEMBER
(
    ID             int unsigned auto_increment primary key,
    PROJECT_ID     int not null,
    USER_ID        varchar(256) default '',
    NAME_TXT       varchar(50)  DEFAULT '',
    `ROLE`           char(1)      default null,
    CREATED_TS     bigint,
    UPDATED_TS     bigint,
    CREATE_BY_TXT  varchar(20)  default null,
    UPDATED_BY_TXT varchar(20)  default null,
    DELETE_FG      smallint     default 0
);