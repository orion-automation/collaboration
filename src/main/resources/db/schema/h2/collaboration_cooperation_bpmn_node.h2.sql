-- liquibase formatted sql

-- changeset eorion:1 dbms:h2
create table if not exists COLLABORATION_COOPERATION_BPMN_NODE
(
    ID                 serial primary key,
    RESOURCE_DETAIL_ID varchar(64) not null default '',
    ACTIVITY_ID        varchar(64) not null default '',
    DELETE_FG          smallint             default 0
);