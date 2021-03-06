ALTER TABLE LEARNING.CADRE_RECORD ADD COLUMN UPLOAD_TO_GLOBAL_TIME TIMESTAMP WITH DEFAULT NULL;
CALL SYSPROC.ADMIN_CMD('REORG TABLE LEARNING.CADRE_RECORD');
ALTER TABLE LEARNING.CADRE_RECORD ADD COLUMN UPLOAD_TO_GLOBAL_STAFF_CODE VARCHAR(12);
CALL SYSPROC.ADMIN_CMD('REORG TABLE LEARNING.CADRE_RECORD');
ALTER TABLE LEARNING.CADRE_RECORD ALTER COLUMN UPLOAD_TO_GLOBAL_STAFF_CODE SET DEFAULT NULL ;

ALTER TABLE LEARNING.COMPETITION_RECORD ADD COLUMN UPLOAD_TO_GLOBAL_TIME TIMESTAMP WITH DEFAULT NULL ;
CALL SYSPROC.ADMIN_CMD('REORG TABLE LEARNING.COMPETITION_RECORD');
ALTER TABLE LEARNING.COMPETITION_RECORD ADD COLUMN UPLOAD_TO_GLOBAL_STAFF_CODE VARCHAR(12) WITH DEFAULT NULL ;
CALL SYSPROC.ADMIN_CMD('REORG TABLE LEARNING.COMPETITION_RECORD');


ALTER TABLE LEARNING.OTHER_RECORD ADD COLUMN UPLOAD_TO_GLOBAL_TIME TIMESTAMP WITH DEFAULT NULL ;
CALL SYSPROC.ADMIN_CMD('REORG TABLE LEARNING.OTHER_RECORD');
ALTER TABLE LEARNING.OTHER_RECORD ADD COLUMN UPLOAD_TO_GLOBAL_STAFF_CODE VARCHAR(12) WITH DEFAULT NULL;
CALL SYSPROC.ADMIN_CMD('REORG TABLE LEARNING.OTHER_RECORD');

ALTER TABLE LEARNING.STUDENT_COURSE_RECORD ADD COLUMN UPLOAD_TO_GLOBAL_TIME TIMESTAMP WITH DEFAULT NULL ;
CALL SYSPROC.ADMIN_CMD('REORG TABLE LEARNING.STUDENT_COURSE_RECORD');
ALTER TABLE LEARNING.STUDENT_COURSE_RECORD ADD COLUMN UPLOAD_TO_GLOBAL_STAFF_CODE VARCHAR(12) WITH DEFAULT NULL;
CALL SYSPROC.ADMIN_CMD('REORG TABLE LEARNING.STUDENT_COURSE_RECORD');


ALTER TABLE LEARNING.VOLUNTEER_RECORD ADD COLUMN UPLOAD_TO_GLOBAL_TIME TIMESTAMP WITH DEFAULT NULL ;
CALL SYSPROC.ADMIN_CMD('REORG TABLE LEARNING.VOLUNTEER_RECORD');
ALTER TABLE LEARNING.VOLUNTEER_RECORD ADD COLUMN UPLOAD_TO_GLOBAL_STAFF_CODE VARCHAR(12) WITH DEFAULT NULL;
CALL SYSPROC.ADMIN_CMD('REORG TABLE LEARNING.VOLUNTEER_RECORD');

ALTER TABLE LEARNING.LICENSE_RECORD ADD COLUMN UPLOAD_TO_GLOBAL_TIME TIMESTAMP WITH DEFAULT NULL ;
CALL SYSPROC.ADMIN_CMD('REORG TABLE LEARNING.LICENSE_RECORD');
ALTER TABLE LEARNING.LICENSE_RECORD ADD COLUMN UPLOAD_TO_GLOBAL_STAFF_CODE VARCHAR(12) WITH DEFAULT NULL;
CALL SYSPROC.ADMIN_CMD('REORG TABLE LEARNING.LICENSE_RECORD');