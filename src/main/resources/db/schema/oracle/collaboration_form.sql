-- liquibase formatted sql

-- changeset eorion:1 dbms:oracle
create table COLLABORATION_FORM
(
    ID             nvarchar2(64)    not null,
    NAME_TXT       nvarchar2(64)    not null ,
    TENANT_TXT     nvarchar2(64)    default null,
    TYPE_TXT       nvarchar2(16)    default '',
    DEFINITION_KEY_TXT NVARCHAR2(255) default '',
    FROM_DATA      nclob,
    CREATED_TS     number(13,0),
    UPDATED_TS     number(13,0),
    CREATE_BY_TXT  nvarchar2(20)    default null,
    UPDATED_BY_TXT nvarchar2(20)    default null,
    DELETE_FG      number(1,0)      default 0,
    primary key (ID)
);