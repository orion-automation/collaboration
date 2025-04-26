-- liquibase formatted sql

-- changeset eorion:1 dbms:mysql
create table if not exists COLLABORATION_COOPERATION_PROJECT
(
    ID             int unsigned auto_increment primary key,
    NAME_TXT       varchar(256) not null,
    COE_CODE       varchar(128) default null,
    TAGS_TXT       varchar(128) default null,
    OWNER          varchar(64)  not null,
    TENANT         varchar(64)  default null,
    TYPE_TXT char(2) default '1',
    CONFIG_JSON_TXT longtext,
    CREATED_TS     bigint       default 0,
    UPDATED_TS     bigint       default 0,
    CREATE_BY_TXT  varchar(256) default null,
    UPDATED_BY_TXT varchar(256) default null,
    DELETE_FG      smallint     default 0
);
