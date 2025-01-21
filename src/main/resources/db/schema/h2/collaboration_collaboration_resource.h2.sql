-- liquibase formatted sql

-- changeset eorion:1 dbms:h2

CREATE TABLE COLLABORATION_COOPERATION_RESOURCE
(
    ID                   SERIAL PRIMARY KEY,
    PROJECT_ID           INTEGER      NOT NULL,
    RESOURCE_NAME        VARCHAR(50) NOT NULL,
    TYPE                 CHAR(1)      NOT NULL,
    PARENT_NODE          INTEGER default 0,
    EXTERNAL_RESOURCE_ID varchar(255),
    STATUS_TXT char(1) default '0',
    TAGS_TXT varchar(255) default '',
    CONFIG_JSON_TXT longtext default '',
    CREATED_TS           bigint,
    UPDATED_TS           bigint,
    CREATE_BY_TXT        varchar(20) default null,
    UPDATED_BY_TXT       varchar(20) default null,
    DELETE_FG            smallint    default 0
);
COMMENT ON COLUMN COLLABORATION_COOPERATION_RESOURCE.TYPE IS '1=文件夹，2=BPMN文件，3=DMN文件';
COMMENT ON COLUMN COLLABORATION_COOPERATION_RESOURCE.EXTERNAL_RESOURCE_ID IS '外部文件ID';