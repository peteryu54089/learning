ALTER TABLE LEARNING.CADRE_RECORD ADD COLUMN CHECK INT WITH DEFAULT 0;
CALL SYSPROC.ADMIN_CMD('REORG TABLE LEARNING.CADRE_RECORD');
ALTER TABLE LEARNING.CADRE_RECORD ADD COLUMN STATUS VARCHAR(2) WITH DEFAULT '1';
CALL SYSPROC.ADMIN_CMD('REORG TABLE LEARNING.CADRE_RECORD');
ALTER TABLE LEARNING.COMPETITION_RECORD ADD COLUMN CHECK INT WITH DEFAULT 0;
CALL SYSPROC.ADMIN_CMD('REORG TABLE LEARNING.COMPETITION_RECORD');
ALTER TABLE LEARNING.COMPETITION_RECORD ADD COLUMN STATUS VARCHAR(2) WITH DEFAULT DEFAULT '1';
CALL SYSPROC.ADMIN_CMD('REORG TABLE LEARNING.COMPETITION_RECORD');
ALTER TABLE LEARNING.LICENSE_RECORD ADD COLUMN CHECK INT WITH DEFAULT 0;
CALL SYSPROC.ADMIN_CMD('REORG TABLE LEARNING.LICENSE_RECORD');
ALTER TABLE LEARNING.LICENSE_RECORD ADD COLUMN STATUS VARCHAR(2) WITH DEFAULT '1';
CALL SYSPROC.ADMIN_CMD('REORG TABLE LEARNING.LICENSE_RECORD');
ALTER TABLE LEARNING.VOLUNTEER_RECORD ADD COLUMN CHECK INT WITH DEFAULT 0;
CALL SYSPROC.ADMIN_CMD('REORG TABLE LEARNING.VOLUNTEER_RECORD');
ALTER TABLE LEARNING.VOLUNTEER_RECORD ADD COLUMN STATUS VARCHAR(2) WITH DEFAULT '1';
CALL SYSPROC.ADMIN_CMD('REORG TABLE LEARNING.VOLUNTEER_RECORD');