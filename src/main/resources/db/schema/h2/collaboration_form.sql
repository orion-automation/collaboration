-- liquibase formatted sql

-- changeset eorion:1 dbms:h2
create table if not exists COLLABORATION_FORM
(
    ID             varchar(64) not null primary key,
    NAME_TXT       varchar(64) not null ,
    TENANT_TXT     varchar(64) default null,
    TYPE_TXT       varchar(16) default '',
    DEFINITION_KEY_TXT VARCHAR(255) default '',
    FROM_DATA      text default '',
    CREATED_TS     bigint,
    UPDATED_TS     bigint,
    CREATE_BY_TXT  varchar(20) default null,
    UPDATED_BY_TXT varchar(20) default null,
    DELETE_FG      smallint    default 0
);