-- liquibase formatted sql

-- changeset eorion:1 dbms:oracle
create table COLLABORATION_COOPERATION_BPMN_NODE
(
    ID                 number(19,0)     not null,
    RESOURCE_DETAIL_ID nvarchar2(64)    default '',
    ACTIVITY_ID        nvarchar2(64)    default '',
    DELETE_FG          number(1,0)      default 0,
    primary key (ID)
);

-- changeset eorion:2 dbms:oracle
CREATE SEQUENCE COLLABORATION_COOPERATION_BPMN_NODE_SEQ
    START WITH 1
    INCREMENT BY 1
    NOCACHE
NOCYCLE;

-- changeset eorion:3 dbms:oracle
CREATE OR REPLACE TRIGGER COLLABORATION_COOPERATION_BPMN_NODE_ID_INSERT
BEFORE INSERT ON COLLABORATION_COOPERATION_BPMN_NODE
FOR EACH ROW
BEGIN
  :NEW.id := COLLABORATION_COOPERATION_BPMN_NODE_SEQ.NEXTVAL;
END;
/