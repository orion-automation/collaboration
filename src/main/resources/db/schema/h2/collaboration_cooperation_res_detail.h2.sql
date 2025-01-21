-- liquibase formatted sql

-- changeset eorion:1 dbms:h2
create table if not exists COLLABORATION_COOPERATION_RES_DETAIL
(
    ID           varchar(64) primary key,
    RESOURCE_ID  int not null,
    NAME_TXT     varchar(255) default '',
    XML_         longtext,
    PASSWORD_TXT varchar(255) default null,
    ZERO_CODE_EFFORT_TIME bigint default 0,
    LOW_CODE_EFFORT_TIME bigint default 0,
    ADVANCE_CODE_EFFORT_TIME bigint default 0,
    CONFIG_JSON_TXT longtext default '',
    VERSION      int not null default 1,
    CREATED_TS   bigint       default null,
    UPDATED_TS   bigint       default null,
    CREATED_BY   varchar(64)  default '',
    UPDATED_BY   varchar(64)  default '',
    DELETE_FG    smallint     default 0
);
