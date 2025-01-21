-- liquibase formatted sql

-- changeset eorion:1 dbms:mysql
create table if not exists COLLABORATION_APPLICATION_RESOURCE
(
    ID             int unsigned  auto_increment primary key,
    APPLICATION_ID integer not null comment '应用ID',
    NAME_TXT       varchar(255) not null ,
    TYPE_FG        char(1) not null comment '1=page页面，2=form表单，3=ux流，4=业务流，5=数据对象',
    ICON_URL       varchar(2000) default '' comment '关联的资源ID',
    PARENT_NODE    integer default null,
    EXTERNAL_RESOURCE_ID varchar(2000) default '' comment '外部资源ID',
    LINKED_RESOURCE_ID varchar(2000) default '',
    EDIT_GROUP_TXT varchar(128) default '',
    STATUS_TXT char(1) default '0',
    DEF_KEY varchar(64),
    DEF_ID varchar(64),
    CONFIG_JSON_TXT longtext,
    CREATED_TS     bigint ,
    UPDATED_TS     bigint ,
    CREATE_BY_TXT  varchar(20) default null ,
    UPDATED_BY_TXT varchar(20) default null,
    DELETE_FG      smallint      default 0
);