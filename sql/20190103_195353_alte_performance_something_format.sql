ALTER TABLE LEARNING.CADRE_RECORD ALTER UNIT SET DATA TYPE VARCHAR(60);
CALL SYSPROC.ADMIN_CMD('REORG TABLE LEARNING.CADRE_RECORD');
ALTER TABLE LEARNING.CADRE_RECORD ALTER JOB SET DATA TYPE VARCHAR(60);
CALL SYSPROC.ADMIN_CMD('REORG TABLE LEARNING.CADRE_RECORD');
ALTER TABLE LEARNING.CADRE_RECORD ALTER EXTERNAL_LINK SET DATA TYPE VARCHAR(1500);
CALL SYSPROC.ADMIN_CMD('REORG TABLE LEARNING.CADRE_RECORD');

ALTER TABLE LEARNING.COMPETITION_RECORD ALTER NAME SET DATA TYPE VARCHAR(120);
CALL SYSPROC.ADMIN_CMD('REORG TABLE LEARNING.COMPETITION_RECORD');
ALTER TABLE LEARNING.COMPETITION_RECORD ALTER ITEM SET DATA TYPE VARCHAR(60);
CALL SYSPROC.ADMIN_CMD('REORG TABLE LEARNING.COMPETITION_RECORD');
ALTER TABLE LEARNING.COMPETITION_RECORD ALTER AWARD SET DATA TYPE VARCHAR(60);
CALL SYSPROC.ADMIN_CMD('REORG TABLE LEARNING.COMPETITION_RECORD');
ALTER TABLE LEARNING.COMPETITION_RECORD ALTER EXTERNAL_LINK SET DATA TYPE VARCHAR(1500);
CALL SYSPROC.ADMIN_CMD('REORG TABLE LEARNING.COMPETITION_RECORD');

ALTER TABLE LEARNING.LICENSE_RECORD ALTER RESULT SET DATA TYPE VARCHAR(150);
CALL SYSPROC.ADMIN_CMD('REORG TABLE LEARNING.LICENSE_RECORD');
ALTER TABLE LEARNING.LICENSE_RECORD ALTER LICENSENUMBER SET DATA TYPE VARCHAR(60);
CALL SYSPROC.ADMIN_CMD('REORG TABLE LEARNING.LICENSE_RECORD');
ALTER TABLE LEARNING.LICENSE_RECORD ALTER GROUP SET DATA TYPE VARCHAR(60);
CALL SYSPROC.ADMIN_CMD('REORG TABLE LEARNING.LICENSE_RECORD');
ALTER TABLE LEARNING.LICENSE_RECORD ALTER EXTERNAL_LINK SET DATA TYPE VARCHAR(1500);
CALL SYSPROC.ADMIN_CMD('REORG TABLE LEARNING.LICENSE_RECORD');
ALTER TABLE LEARNING.LICENSE_RECORD ALTER POINT SET DATA TYPE VARCHAR(6);
CALL SYSPROC.ADMIN_CMD('REORG TABLE LEARNING.LICENSE_RECORD');

ALTER TABLE LEARNING.VOLUNTEER_RECORD ALTER NAME SET DATA TYPE VARCHAR(60);
CALL SYSPROC.ADMIN_CMD('REORG TABLE LEARNING.VOLUNTEER_RECORD');
ALTER TABLE LEARNING.VOLUNTEER_RECORD ALTER PLACE SET DATA TYPE VARCHAR(60);
CALL SYSPROC.ADMIN_CMD('REORG TABLE LEARNING.VOLUNTEER_RECORD');
ALTER TABLE LEARNING.VOLUNTEER_RECORD ALTER EXTERNAL_LINK SET DATA TYPE VARCHAR(1500);
CALL SYSPROC.ADMIN_CMD('REORG TABLE LEARNING.VOLUNTEER_RECORD');

ALTER TABLE LEARNING.OTHER_RECORD ALTER NAME SET DATA TYPE VARCHAR(60);
CALL SYSPROC.ADMIN_CMD('REORG TABLE LEARNING.OTHER_RECORD');
ALTER TABLE LEARNING.OTHER_RECORD ALTER UNIT SET DATA TYPE VARCHAR(60);
CALL SYSPROC.ADMIN_CMD('REORG TABLE LEARNING.OTHER_RECORD');
ALTER TABLE LEARNING.OTHER_RECORD ALTER EXTERNAL_LINK SET DATA TYPE VARCHAR(1500);
CALL SYSPROC.ADMIN_CMD('REORG TABLE LEARNING.OTHER_RECORD');