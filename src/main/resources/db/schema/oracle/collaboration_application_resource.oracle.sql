-- liquibase formatted sql

-- changeset eorion:1 dbms:oracle
create table COLLABORATION_APPLICATION_RESOURCE
(
    ID number(10,0) not null,
    APPLICATION_ID number(10,0) not null,
    NAME_TXT nvarchar2(255) not null,
    TYPE_FG char(1) not null,
    ICON_URL nvarchar2(2000) default '',
    PARENT_NODE number(10,0) default null,
    EXTERNAL_RESOURCE_ID nvarchar2(2000) default '',
    LINKED_RESOURCE_ID nvarchar2(2000) default '',
    EDIT_GROUP_TXT nvarchar2(128) default '',
    STATUS_TXT char(1) default '0',
    DEF_KEY nvarchar2(64),
    DEF_ID nvarchar2(64),
    CONFIG_JSON_TXT nclob,
    CREATED_TS number(13,0),
    UPDATED_TS number(13,0),
    CREATE_BY_TXT nvarchar2(20) default null,
    UPDATED_BY_TXT nvarchar2(20) default null,
    DELETE_FG number(1,0) default 0,
    primary key (ID)
);

-- changeset eorion:2 dbms:oracle
CREATE SEQUENCE COLLABORATION_APPLICATION_RESOURCE_SEQ
    START WITH 1
    INCREMENT BY 1
    NOCACHE
NOCYCLE;

-- changeset eorion:3 dbms:oracle
CREATE OR REPLACE TRIGGER COLLABORATION_APPLICATION_RESOURCE_ID_INSERT
BEFORE INSERT ON COLLABORATION_APPLICATION_RESOURCE
FOR EACH ROW
BEGIN
  :NEW.ID := COLLABORATION_APPLICATION_RESOURCE_SEQ.NEXTVAL;
END;
/