-- liquibase formatted sql

-- changeset eorion:1 dbms:oracle
create table COLLABORATION_COOPERATION_RES_DETAIL
(
    ID           nvarchar2(64)      not null,
    RESOURCE_ID  number(10,0)       not null,
    NAME_TXT     nvarchar2(255)     default '',
    XML_         nclob,
    PASSWORD_TXT nvarchar2(255),
    ZERO_CODE_EFFORT_TIME number(13,0)  default 0,
    LOW_CODE_EFFORT_TIME number(13,0)   default 0,
    ADVANCE_CODE_EFFORT_TIME number(13,0) default 0,
    CONFIG_JSON_TXT nclob,
    VERSION      number(10,0)       default 1,
    CREATED_TS   number(13,0),
    UPDATED_TS   number(13,0),
    CREATED_BY   nvarchar2(64)      default '',
    UPDATED_BY   nvarchar2(64)      default '',
    DELETE_FG    number(1,0)        default 0,
    primary key (ID)
);
