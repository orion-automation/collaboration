-- liquibase formatted sql

-- changeset eorion:1 dbms:postgresql
create table if not exists COLLABORATION_COOPERATION_BPMN_NODE
(
    ID                 serial primary key,
    RESOURCE_DETAIL_ID varchar(64) not null default '',
    ACTIVITY_ID        varchar(64) not null default '',
    DELETE_FG          smallint             default 0
);
comment on column COLLABORATION_COOPERATION_BPMN_NODE.RESOURCE_DETAIL_ID is '对应资源详情id';
comment on column COLLABORATION_COOPERATION_BPMN_NODE.ACTIVITY_ID is '活动节点id';