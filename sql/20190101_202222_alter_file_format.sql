ALTER TABLE LEARNING.AUTOBIOGRAPHY ALTER MAIN_FILE_PATH SET DATA TYPE VARCHAR(300);
CALL SYSPROC.ADMIN_CMD('REORG TABLE LEARNING.AUTOBIOGRAPHY');
ALTER TABLE LEARNING.AUTOBIOGRAPHY ALTER SUB_FILE_PATH SET DATA TYPE VARCHAR(300);
CALL SYSPROC.ADMIN_CMD('REORG TABLE LEARNING.AUTOBIOGRAPHY');

ALTER TABLE LEARNING.AUTOBIOGRAPHY ALTER MAIN_FILE_PATH SET DATA TYPE VARCHAR(300);
CALL SYSPROC.ADMIN_CMD('REORG TABLE LEARNING.AUTOBIOGRAPHY');
ALTER TABLE LEARNING.AUTOBIOGRAPHY ALTER SUB_FILE_PATH SET DATA TYPE VARCHAR(300);
CALL SYSPROC.ADMIN_CMD('REORG TABLE LEARNING.AUTOBIOGRAPHY');
ALTER TABLE LEARNING.AUTOBIOGRAPHY ALTER MAIN_ORIGINAL_FILENAME SET DATA TYPE VARCHAR(300);
CALL SYSPROC.ADMIN_CMD('REORG TABLE LEARNING.AUTOBIOGRAPHY');
ALTER TABLE LEARNING.AUTOBIOGRAPHY ALTER SUB_ORIGINAL_FILENAME SET DATA TYPE VARCHAR(300);
CALL SYSPROC.ADMIN_CMD('REORG TABLE LEARNING.AUTOBIOGRAPHY');

ALTER TABLE LEARNING.COMPETITION_RECORD ALTER DOCUMENT SET DATA TYPE VARCHAR(300);
CALL SYSPROC.ADMIN_CMD('REORG TABLE LEARNING.COMPETITION_RECORD');
ALTER TABLE LEARNING.COMPETITION_RECORD ALTER VIDEO SET DATA TYPE VARCHAR(300);
CALL SYSPROC.ADMIN_CMD('REORG TABLE LEARNING.COMPETITION_RECORD');

ALTER TABLE LEARNING.DOCUMENT ALTER FILE_PATH SET DATA TYPE VARCHAR(300);
CALL SYSPROC.ADMIN_CMD('REORG TABLE LEARNING.DOCUMENT');
ALTER TABLE LEARNING.DOCUMENT ALTER ORIGINAL_FILENAME SET DATA TYPE VARCHAR(300);
CALL SYSPROC.ADMIN_CMD('REORG TABLE LEARNING.DOCUMENT');

ALTER TABLE LEARNING.GROUP_COUNSEL ALTER ORIGINAL_FILENAME SET DATA TYPE VARCHAR(300);
CALL SYSPROC.ADMIN_CMD('REORG TABLE LEARNING.GROUP_COUNSEL');
ALTER TABLE LEARNING.GROUP_COUNSEL ALTER RANDOM_FILENAME SET DATA TYPE VARCHAR(300);
CALL SYSPROC.ADMIN_CMD('REORG TABLE LEARNING.GROUP_COUNSEL');

ALTER TABLE LEARNING.INDIVIDUAL_COUNSEL ALTER ORIGINAL_FILENAME SET DATA TYPE VARCHAR(300);
CALL SYSPROC.ADMIN_CMD('REORG TABLE LEARNING.INDIVIDUAL_COUNSEL');
ALTER TABLE LEARNING.INDIVIDUAL_COUNSEL ALTER RANDOM_FILENAME SET DATA TYPE VARCHAR(300);
CALL SYSPROC.ADMIN_CMD('REORG TABLE LEARNING.INDIVIDUAL_COUNSEL');

ALTER TABLE LEARNING.LIFEPLAN ALTER FILE_PATH SET DATA TYPE VARCHAR(300);
CALL SYSPROC.ADMIN_CMD('REORG TABLE LEARNING.LIFEPLAN');
ALTER TABLE LEARNING.LIFEPLAN ALTER ORIGINAL_FILENAME SET DATA TYPE VARCHAR(300);
CALL SYSPROC.ADMIN_CMD('REORG TABLE LEARNING.LIFEPLAN');

ALTER TABLE LEARNING.RESUME ALTER FILE_PATH SET DATA TYPE VARCHAR(300);
CALL SYSPROC.ADMIN_CMD('REORG TABLE LEARNING.RESUME');
ALTER TABLE LEARNING.RESUME ALTER ORIGINAL_FILENAME SET DATA TYPE VARCHAR(300);
CALL SYSPROC.ADMIN_CMD('REORG TABLE LEARNING.RESUME');

ALTER TABLE LEARNING.LICENSE_RECORD ALTER DOCUMENT SET DATA TYPE VARCHAR(300);
CALL SYSPROC.ADMIN_CMD('REORG TABLE LEARNING.LICENSE_RECORD');
ALTER TABLE LEARNING.LICENSE_RECORD ALTER VIDEO SET DATA TYPE VARCHAR(300);
CALL SYSPROC.ADMIN_CMD('REORG TABLE LEARNING.LICENSE_RECORD');


ALTER TABLE LEARNING.OTHER_RECORD ALTER DOCUMENT SET DATA TYPE VARCHAR(300);
CALL SYSPROC.ADMIN_CMD('REORG TABLE LEARNING.OTHER_RECORD');
ALTER TABLE LEARNING.OTHER_RECORD ALTER VIDEO SET DATA TYPE VARCHAR(300);
CALL SYSPROC.ADMIN_CMD('REORG TABLE LEARNING.OTHER_RECORD');

ALTER TABLE LEARNING.STUDENT_COURSE_RECORD_DOCUMENT ALTER DOCUMENT SET DATA TYPE VARCHAR(300);
CALL SYSPROC.ADMIN_CMD('REORG TABLE LEARNING.STUDENT_COURSE_RECORD_DOCUMENT');
ALTER TABLE LEARNING.STUDENT_COURSE_RECORD_DOCUMENT ALTER ORIGINAL_FILENAME SET DATA TYPE VARCHAR(300);
CALL SYSPROC.ADMIN_CMD('REORG TABLE LEARNING.STUDENT_COURSE_RECORD_DOCUMENT');
ALTER TABLE LEARNING.STUDENT_COURSE_RECORD_DOCUMENT DROP CONSTRAINT STUDENT_COURSE_RECORD_DOCUMENT_STUDENT_COURSE_RECORD_ID_FK;
ALTER TABLE LEARNING.STUDENT_COURSE_RECORD_DOCUMENT
ADD CONSTRAINT STUDENT_COURSE_RECORD_DOCUMENT_STUDENT_COURSE_RECORD_ID_FK
FOREIGN KEY (CRID) REFERENCES STUDENT_COURSE_RECORD (ID);







