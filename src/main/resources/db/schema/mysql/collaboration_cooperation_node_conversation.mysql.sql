-- liquibase formatted sql

-- changeset eorion:1 dbms:mysql
create table if not exists COLLABORATION_COOPERATION_NODE_CONVERSATION
(
    ID          bigint unsigned auto_increment primary key,
    NODE_ID     int          not null,
    MESSAGE_TXT varchar(255) not null,
    CREATED_TS  bigint unsigned default null,
    UPDATED_TS  bigint unsigned default null,
    CREATED_BY  varchar(64)     default '',
    UPDATED_BY  varchar(64)     default '',
    DELETE_FG   smallint        default 0
);