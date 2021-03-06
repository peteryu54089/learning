ALTER TABLE LEARNING.DOCUMENT DROP REGISTER_NUMBER;
ALTER TABLE LEARNING.DOCUMENT DROP FILE_PATH;
ALTER TABLE LEARNING.DOCUMENT DROP DELETED_AT;
ALTER TABLE LEARNING.DOCUMENT DROP ORIGINAL_FILENAME;

ALTER TABLE LEARNING.DOCUMENT ADD COLUMN UPLOAD_FILE_ID INT;
ALTER TABLE LEARNING.DOCUMENT ADD CONSTRAINT UPLOAD_FILE_ID CHECK (UPLOAD_FILE_ID IS NOT NULL);
CALL SYSPROC.ADMIN_CMD('REORG TABLE LEARNING.DOCUMENT');
ALTER TABLE LEARNING.DOCUMENT ADD COLUMN RGNO INT;
ALTER TABLE LEARNING.DOCUMENT ADD CONSTRAINT RGNO CHECK (RGNO IS NOT NULL);
CALL SYSPROC.ADMIN_CMD('REORG TABLE LEARNING.DOCUMENT');

ALTER TABLE LEARNING.DOCUMENT ALTER COLUMN UPLOAD_FILE_ID SET NOT NULL;
CALL SYSPROC.ADMIN_CMD('REORG TABLE LEARNING.DOCUMENT');
ALTER TABLE LEARNING.DOCUMENT ALTER COLUMN RGNO SET NOT NULL;
CALL SYSPROC.ADMIN_CMD('REORG TABLE LEARNING.DOCUMENT');

ALTER TABLE LEARNING.DOCUMENT
ADD CONSTRAINT DOCUMENT_UPLOAD_FILES_ID_fk
FOREIGN KEY (UPLOAD_FILE_ID) REFERENCES UPLOAD_FILES (ID) ON DELETE CASCADE;

