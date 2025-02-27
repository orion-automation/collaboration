-- liquibase formatted sql

-- changeset eorion:1 dbms:oracle
create table COLLABORATION_COOPERATION_PROJECT
(
    ID             number(10,0)   not null,
    NAME_TXT       nvarchar2(256) not null,
    COE_CODE       nvarchar2(128) default null,
    TAGS_TXT       nvarchar2(128) default null,
    OWNER          nvarchar2(64)  not null,
    TYPE_TXT       char(1) default '1',
    CONFIG_JSON_TXT nclob,
    TENANT         nvarchar2(64)  default null,
    CREATED_TS     number(13,0)   default 0,
    UPDATED_TS     number(13,0)   default 0,
    CREATE_BY_TXT  nvarchar2(256) default null,
    UPDATED_BY_TXT nvarchar2(256) default null,
    DELETE_FG      number(1,0)    default 0,
    primary key (ID)
);

-- changeset eorion:2 dbms:oracle
CREATE SEQUENCE COLLABORATION_COOPERATION_PROJECT_SEQ
    START WITH 1
    INCREMENT BY 1
    NOCACHE
NOCYCLE;

-- changeset eorion:3 dbms:oracle
CREATE OR REPLACE TRIGGER COLLABORATION_COOPERATION_PROJECT_ID_INSERT
BEFORE INSERT ON COLLABORATION_COOPERATION_PROJECT
FOR EACH ROW
BEGIN
  :NEW.ID := COLLABORATION_COOPERATION_PROJECT_SEQ.NEXTVAL;
END;
/