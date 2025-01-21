-- liquibase formatted sql

-- changeset eorion:1 dbms:mysql

CREATE TABLE COLLABORATION_COOPERATION_RESOURCE
(
    ID                   int unsigned auto_increment primary key,
    PROJECT_ID           int unsigned     not null,
    RESOURCE_NAME        varchar(50) not null,
    TYPE                 char(1)      not null comment '1=文件夹，2=BPMN文件，3=DMN文件',
    PARENT_NODE          int unsigned default 0,
    EXTERNAL_RESOURCE_ID varchar(255) comment '外部文件ID',
    STATUS_TXT char(1) default '0',
    TAGS_TXT varchar(255) default '',
    CONFIG_JSON_TXT longtext,
    CREATED_TS           bigint,
    UPDATED_TS           bigint,
    CREATE_BY_TXT        varchar(20) default null,
    UPDATED_BY_TXT       varchar(20) default null,
    DELETE_FG            smallint    default 0
);