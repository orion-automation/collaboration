BEGIN
    EXECUTE IMMEDIATE 'TRUNCATE TABLE COLLABORATION_COOPERATION_RESOURCE REUSE STORAGE';
    EXECUTE IMMEDIATE 'ALTER SEQUENCE COLLABORATION_COOPERATION_RESOURCE_SEQ RESTART START WITH 1';
END;