ALTER TABLE LEARNING.CADRE_RECORD ADD COLUMN SELECTEDYEAR VARCHAR(5) WITH DEFAULT NULL ;
CALL SYSPROC.ADMIN_CMD('REORG TABLE LEARNING.CADRE_RECORD');
ALTER TABLE LEARNING.COMPETITION_RECORD ADD COLUMN SELECTEDYEAR VARCHAR(5) WITH DEFAULT NULL ;
CALL SYSPROC.ADMIN_CMD('REORG TABLE LEARNING.COMPETITION_RECORD');
ALTER TABLE LEARNING.LICENSE_RECORD ADD COLUMN SELECTEDYEAR VARCHAR(5) WITH DEFAULT NULL ;
CALL SYSPROC.ADMIN_CMD('REORG TABLE LEARNING.LICENSE_RECORD');
ALTER TABLE LEARNING.OTHER_RECORD ADD COLUMN SELECTEDYEAR VARCHAR(5) WITH DEFAULT null;
CALL SYSPROC.ADMIN_CMD('REORG TABLE LEARNING.OTHER_RECORD');
ALTER TABLE LEARNING.VOLUNTEER_RECORD ADD COLUMN SELECTEDYEAR VARCHAR(5) WITH DEFAULT null;
CALL SYSPROC.ADMIN_CMD('REORG TABLE LEARNING.VOLUNTEER_RECORD');