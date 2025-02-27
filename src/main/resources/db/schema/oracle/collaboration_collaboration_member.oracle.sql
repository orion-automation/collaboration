-- liquibase formatted sql

-- changeset eorion:1 dbms:oracle
create table COLLABORATION_COOPERATION_MEMBER
(
    ID             number(10,0) not null,
    PROJECT_ID     number(10,0) not null,
    USER_ID        nvarchar2(256) default '',
    NAME_TXT       nvarchar2(50)  default '',
    ROLE           char(1)  default null,
    CREATED_TS     number(13,0),
    UPDATED_TS     number(13,0),
    CREATE_BY_TXT  nvarchar2(20)  default null,
    UPDATED_BY_TXT nvarchar2(20)  default null,
    DELETE_FG      number(1,0)  default 0,
    primary key (ID)
);

-- changeset eorion:2 dbms:oracle
CREATE SEQUENCE COLLABORATION_COOPERATION_MEMBER_SEQ
    START WITH 1
    INCREMENT BY 1
    NOCACHE
NOCYCLE;

-- changeset eorion:3 dbms:oracle
CREATE OR REPLACE TRIGGER COLLABORATION_COOPERATION_MEMBER_ID_INSERT
BEFORE INSERT ON COLLABORATION_COOPERATION_MEMBER
FOR EACH ROW
BEGIN
  :NEW.ID := COLLABORATION_COOPERATION_MEMBER_SEQ.NEXTVAL;
END;
/