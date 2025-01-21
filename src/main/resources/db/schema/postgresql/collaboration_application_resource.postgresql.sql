-- liquibase formatted sql

-- changeset eorion:1 dbms:postgresql
create table if not exists COLLABORATION_APPLICATION_RESOURCE
(
    ID             SERIAL PRIMARY KEY,
    APPLICATION_ID integer not null,
    NAME_TXT       varchar(255) not null ,
    TYPE_FG        char(1) not null ,
    ICON_URL       varchar(2000) default '',
    PARENT_NODE    integer default null,
    EXTERNAL_RESOURCE_ID varchar(2000) default '',
    LINKED_RESOURCE_ID varchar(2000) default '',
    EDIT_GROUP_TXT varchar(128) default '',
    STATUS_TXT char(1) default '0',
    DEF_KEY varchar(64),
    DEF_ID varchar(64),
    CONFIG_JSON_TXT text,
    CREATED_TS     bigint ,
    UPDATED_TS     bigint ,
    CREATE_BY_TXT  varchar(20) default null ,
    UPDATED_BY_TXT varchar(20) default null,
    DELETE_FG      smallint      default 0
);