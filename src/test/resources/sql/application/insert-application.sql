INSERT INTO COLLABORATION_APPLICATION (NAME_TXT, OWNER_TXT, COE_CODE_TXT, TENANT_TXT, TYPE_TXT, CATEGORY_TXT,
                              CONFIG_TXT, KEY_TXT, USER_GROUP_TXT, DESCRIPTION_TXT, VERSION_TAG_TXT, ACCESS_USERS_TXT, STATUS_FG, CREATED_TS,
                              CREATE_BY_TXT)
VALUES ('name1', 'demo', 'coe', 'tenant', 'type', 'category', '{"test": "test"}', 'key1', 'user1', 'description',
        'versionTag', 'demo,test', '0', '1676256003609',
        'demo'),
       ('name2', 'demo', 'coe', 'tenant', 'type', 'category', '{"test": "test"}', 'key2', 'user1,user2', 'description',
        'versionTag', 'demo,test2', '0', '1676256003609',
        'demo'),
       ('name3', 'demo', 'coe', 'tenant', 'type', 'category', '{"test": "test"}', 'key3','user1,user2,user3', 'description',
        'versionTag', 'demo,test', '1', '1676256003609',
        'demo');