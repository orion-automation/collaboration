-- liquibase formatted sql

-- changeset eorion:1 dbms:mysql
create table if not exists COLLABORATION_COOPERATION_BPMN_NODE
(
    ID                 bigint unsigned auto_increment primary key,
    RESOURCE_DETAIL_ID varchar(64) not null default '' comment '对应资源详情id',
    ACTIVITY_ID        varchar(64) not null default '' comment '活动节点id',
    DELETE_FG          smallint             default 0
);