-- liquibase formatted sql

-- changeset eorion:1 dbms:oracle
create table COLLABORATION_COOPERATION_RES_ASSOCIATION
(
    ID          number(19,0)    not null,
    NAME_TXT    nvarchar2(255)  default '',
    URL_TXT     nvarchar2(255),
    TYPE_FG     char(1)         default '',
    RESOURCE_ID number(10,0)    not null,
    CREATED_TS  number(13,0)    default null,
    UPDATED_TS  number(13,0)    default null,
    CREATED_BY  nvarchar2(64)   default '',
    UPDATED_BY  nvarchar2(64)   default '',
    DELETE_FG   number(1,0)     default 0,
    primary key (ID)
);

-- changeset eorion:2 dbms:oracle
CREATE SEQUENCE COLLABORATION_COOPERATION_RES_ASSOCIATION_SEQ
    START WITH 1
    INCREMENT BY 1
    NOCACHE
NOCYCLE;

-- changeset eorion:3 dbms:oracle
CREATE OR REPLACE TRIGGER COLLABORATION_COOPERATION_RES_ASSOCIATION_ID_INSERT
BEFORE INSERT ON COLLABORATION_COOPERATION_RES_ASSOCIATION
FOR EACH ROW
BEGIN
  :NEW.id := COLLABORATION_COOPERATION_RES_ASSOCIATION_SEQ.NEXTVAL;
END;
/