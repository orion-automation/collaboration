-- liquibase formatted sql

-- changeset eorion:1 dbms:h2
create table if not exists COLLABORATION_COOPERATION_RES_ASSOCIATION
(
    ID          serial primary key,
    NAME_TXT    varchar(255) default '',
    URL_TXT     varchar(255),
    TYPE_FG     char(1)      default '',
    RESOURCE_ID int not null,
    CREATED_TS  bigint       default null,
    UPDATED_TS  bigint       default null,
    CREATED_BY  varchar(64)  default '',
    UPDATED_BY  varchar(64)  default '',
    DELETE_FG   smallint     default 0
);