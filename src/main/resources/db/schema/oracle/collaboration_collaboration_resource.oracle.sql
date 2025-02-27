-- liquibase formatted sql

-- changeset eorion:1 dbms:oracle
CREATE TABLE COLLABORATION_COOPERATION_RESOURCE
(
    ID                   number(10,0)   not null,
    PROJECT_ID           number(10,0)   not null,
    RESOURCE_NAME        nvarchar2(50)  not null,
    TYPE                 char(1)        not null,
    PARENT_NODE          number(10,0)   default 0,
    EXTERNAL_RESOURCE_ID nvarchar2(255),
    STATUS_TXT           char(1)        default '0',
    TAGS_TXT             nvarchar2(255) default '',
    CONFIG_JSON_TXT      nclob,
    CREATED_TS           number(13,0),
    UPDATED_TS           number(13,0),
    CREATE_BY_TXT        nvarchar2(20)  default null,
    UPDATED_BY_TXT       nvarchar2(20)  default null,
    DELETE_FG            number(1,0)    default 0,
    primary key (ID)
);

-- changeset eorion:2 dbms:oracle
CREATE SEQUENCE COLLABORATION_COOPERATION_RESOURCE_SEQ
    START WITH 1
    INCREMENT BY 1
    NOCACHE
NOCYCLE;

-- changeset eorion:3 dbms:oracle
CREATE OR REPLACE TRIGGER COLLABORATION_COOPERATION_RESOURCE_ID_INSERT
BEFORE INSERT ON COLLABORATION_COOPERATION_RESOURCE
FOR EACH ROW
BEGIN
  :NEW.id := COLLABORATION_COOPERATION_RESOURCE_SEQ.NEXTVAL;
END;
/