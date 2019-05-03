DELETE FROM CADRE_RECORD;
DELETE FROM COMPETITION_RECORD;
DELETE FROM LICENSE_RECORD;
DELETE FROM OTHER_RECORD;
DELETE FROM VOLUNTEER_RECORD;
DELETE FROM PERFORMANCE_UNLOCK_STATUS;

alter table CADRE_RECORD alter column SUBMITTER set default '';
alter table COMPETITION_RECORD alter column SUBMITTER set default '';
alter table LICENSE_RECORD alter column SUBMITTER set default '';
alter table OTHER_RECORD alter column SUBMITTER set default '';
alter table VOLUNTEER_RECORD alter column SUBMITTER set default '';



-- START CADRE_RECORD --
alter table CADRE_RECORD rename column IDNO to RGNO;

alter table CADRE_RECORD alter column RGNO set data type INTEGER;

call sysproc.admin_cmd('reorg table CADRE_RECORD');

alter table CADRE_RECORD drop column BIRTH;

call sysproc.admin_cmd('reorg table CADRE_RECORD');

alter table CADRE_RECORD drop column DOCUMENT;

call sysproc.admin_cmd('reorg table CADRE_RECORD');

alter table CADRE_RECORD drop column REGISTER_NUMBER;

call sysproc.admin_cmd('reorg table CADRE_RECORD');

alter table CADRE_RECORD drop column DELETED_AT;

call sysproc.admin_cmd('reorg table CADRE_RECORD');

alter table CADRE_RECORD drop column DOCUMENT_ORIGINAL_FILENAME;

call sysproc.admin_cmd('reorg table CADRE_RECORD');

alter table CADRE_RECORD rename column VIDEO to DOCUMENT_FILE_ID;

alter table CADRE_RECORD alter column DOCUMENT_FILE_ID set data type INTEGER;

call sysproc.admin_cmd('reorg table CADRE_RECORD');

alter table CADRE_RECORD rename column VIDEO_ORIGINAL_FILENAME to VIDEO_FILE_ID;

alter table CADRE_RECORD alter column VIDEO_FILE_ID set data type INTEGER;

call sysproc.admin_cmd('reorg table CADRE_RECORD');

create index CADRE_RECORD_RGNO_index
	on CADRE_RECORD (RGNO);

alter table CADRE_RECORD
	add constraint CADRE_RECORD_UPLOAD_FILES_ID_fk
		foreign key (DOCUMENT_FILE_ID) references UPLOAD_FILES
			on delete set null;

alter table CADRE_RECORD
	add constraint CADRE_RECORD_UPLOAD_FILES_ID_fk_2
		foreign key (VIDEO_FILE_ID) references UPLOAD_FILES
			on delete set null;


-- END OF CADRE_RECORD --

--------------------------------------------------------------

-- START COMPETITION_RECORD --
alter table COMPETITION_RECORD rename column IDNO to RGNO;

alter table COMPETITION_RECORD alter column RGNO set data type INTEGER;

call sysproc.admin_cmd('reorg table COMPETITION_RECORD');

alter table COMPETITION_RECORD drop column BIRTH;

call sysproc.admin_cmd('reorg table COMPETITION_RECORD');

alter table COMPETITION_RECORD drop column DOCUMENT;

call sysproc.admin_cmd('reorg table COMPETITION_RECORD');

alter table COMPETITION_RECORD drop column REGISTER_NUMBER;

call sysproc.admin_cmd('reorg table COMPETITION_RECORD');

alter table COMPETITION_RECORD drop column DELETED_AT;

call sysproc.admin_cmd('reorg table COMPETITION_RECORD');

alter table COMPETITION_RECORD drop column DOCUMENT_ORIGINAL_FILENAME;

call sysproc.admin_cmd('reorg table COMPETITION_RECORD');

alter table COMPETITION_RECORD rename column VIDEO to DOCUMENT_FILE_ID;

alter table COMPETITION_RECORD alter column DOCUMENT_FILE_ID set data type INTEGER;

call sysproc.admin_cmd('reorg table COMPETITION_RECORD');

alter table COMPETITION_RECORD rename column VIDEO_ORIGINAL_FILENAME to VIDEO_FILE_ID;

alter table COMPETITION_RECORD alter column VIDEO_FILE_ID set data type INTEGER;

call sysproc.admin_cmd('reorg table COMPETITION_RECORD');

create index COMPETITION_RECORD_RGNO_index
	on COMPETITION_RECORD (RGNO);

alter table COMPETITION_RECORD
	add constraint COMPETITION_RECORD_UPLOAD_FILES_ID_fk
		foreign key (DOCUMENT_FILE_ID) references UPLOAD_FILES
			on delete set null;

alter table COMPETITION_RECORD
	add constraint COMPETITION_RECORD_UPLOAD_FILES_ID_fk_2
		foreign key (VIDEO_FILE_ID) references UPLOAD_FILES
			on delete set null;


-- END OF COMPETITION_RECORD --


--------------------------------------------------------------

-- START LICENSE_RECORD --
alter table LICENSE_RECORD rename column IDNO to RGNO;

alter table LICENSE_RECORD alter column RGNO set data type INTEGER;

call sysproc.admin_cmd('reorg table LICENSE_RECORD');

alter table LICENSE_RECORD drop column BIRTH;

call sysproc.admin_cmd('reorg table LICENSE_RECORD');

alter table LICENSE_RECORD drop column DOCUMENT;

call sysproc.admin_cmd('reorg table LICENSE_RECORD');

alter table LICENSE_RECORD drop column REGISTER_NUMBER;

call sysproc.admin_cmd('reorg table LICENSE_RECORD');

alter table LICENSE_RECORD drop column DELETED_AT;

call sysproc.admin_cmd('reorg table LICENSE_RECORD');

alter table LICENSE_RECORD drop column DOCUMENT_ORIGINAL_FILENAME;

call sysproc.admin_cmd('reorg table LICENSE_RECORD');

alter table LICENSE_RECORD rename column VIDEO to DOCUMENT_FILE_ID;

alter table LICENSE_RECORD alter column DOCUMENT_FILE_ID set data type INTEGER;

call sysproc.admin_cmd('reorg table LICENSE_RECORD');

alter table LICENSE_RECORD rename column VIDEO_ORIGINAL_FILENAME to VIDEO_FILE_ID;

alter table LICENSE_RECORD alter column VIDEO_FILE_ID set data type INTEGER;

call sysproc.admin_cmd('reorg table LICENSE_RECORD');

create index LICENSE_RECORD_RGNO_index
	on LICENSE_RECORD (RGNO);

alter table LICENSE_RECORD
	add constraint LICENSE_RECORD_UPLOAD_FILES_ID_fk
		foreign key (DOCUMENT_FILE_ID) references UPLOAD_FILES
			on delete set null;

alter table LICENSE_RECORD
	add constraint LICENSE_RECORD_UPLOAD_FILES_ID_fk_2
		foreign key (VIDEO_FILE_ID) references UPLOAD_FILES
			on delete set null;


-- END OF LICENSE_RECORD --



--------------------------------------------------------------

-- START OTHER_RECORD --
alter table OTHER_RECORD rename column IDNO to RGNO;

alter table OTHER_RECORD alter column RGNO set data type INTEGER;

call sysproc.admin_cmd('reorg table OTHER_RECORD');

alter table OTHER_RECORD drop column BIRTH;

call sysproc.admin_cmd('reorg table OTHER_RECORD');

alter table OTHER_RECORD drop column DOCUMENT;

call sysproc.admin_cmd('reorg table OTHER_RECORD');

alter table OTHER_RECORD drop column REGISTER_NUMBER;

call sysproc.admin_cmd('reorg table OTHER_RECORD');

alter table OTHER_RECORD drop column DELETED_AT;

call sysproc.admin_cmd('reorg table OTHER_RECORD');

alter table OTHER_RECORD drop column DOCUMENT_ORIGINAL_FILENAME;

call sysproc.admin_cmd('reorg table OTHER_RECORD');

alter table OTHER_RECORD rename column VIDEO to DOCUMENT_FILE_ID;

alter table OTHER_RECORD alter column DOCUMENT_FILE_ID set data type INTEGER;

call sysproc.admin_cmd('reorg table OTHER_RECORD');

alter table OTHER_RECORD rename column VIDEO_ORIGINAL_FILENAME to VIDEO_FILE_ID;

alter table OTHER_RECORD alter column VIDEO_FILE_ID set data type INTEGER;

call sysproc.admin_cmd('reorg table OTHER_RECORD');

create index OTHER_RECORD_RGNO_index
	on OTHER_RECORD (RGNO);

alter table OTHER_RECORD
	add constraint OTHER_RECORD_UPLOAD_FILES_ID_fk
		foreign key (DOCUMENT_FILE_ID) references UPLOAD_FILES
			on delete set null;

alter table OTHER_RECORD
	add constraint OTHER_RECORD_UPLOAD_FILES_ID_fk_2
		foreign key (VIDEO_FILE_ID) references UPLOAD_FILES
			on delete set null;


-- END OF OTHER_RECORD --



--------------------------------------------------------------

-- START VOLUNTEER_RECORD --
alter table VOLUNTEER_RECORD rename column IDNO to RGNO;

alter table VOLUNTEER_RECORD alter column RGNO set data type INTEGER;

call sysproc.admin_cmd('reorg table VOLUNTEER_RECORD');

alter table VOLUNTEER_RECORD drop column BIRTH;

call sysproc.admin_cmd('reorg table VOLUNTEER_RECORD');

alter table VOLUNTEER_RECORD drop column DOCUMENT;

call sysproc.admin_cmd('reorg table VOLUNTEER_RECORD');

alter table VOLUNTEER_RECORD drop column REGISTER_NUMBER;

call sysproc.admin_cmd('reorg table VOLUNTEER_RECORD');

alter table VOLUNTEER_RECORD drop column DELETED_AT;

call sysproc.admin_cmd('reorg table VOLUNTEER_RECORD');

alter table VOLUNTEER_RECORD drop column DOCUMENT_ORIGINAL_FILENAME;

call sysproc.admin_cmd('reorg table VOLUNTEER_RECORD');

alter table VOLUNTEER_RECORD rename column VIDEO to DOCUMENT_FILE_ID;

alter table VOLUNTEER_RECORD alter column DOCUMENT_FILE_ID set data type INTEGER;

call sysproc.admin_cmd('reorg table VOLUNTEER_RECORD');

alter table VOLUNTEER_RECORD rename column VIDEO_ORIGINAL_FILENAME to VIDEO_FILE_ID;

alter table VOLUNTEER_RECORD alter column VIDEO_FILE_ID set data type INTEGER;

call sysproc.admin_cmd('reorg table VOLUNTEER_RECORD');

create index VOLUNTEER_RECORD_RGNO_index
	on VOLUNTEER_RECORD (RGNO);

alter table VOLUNTEER_RECORD
	add constraint VOLUNTEER_RECORD_UPLOAD_FILES_ID_fk
		foreign key (DOCUMENT_FILE_ID) references UPLOAD_FILES
			on delete set null;

alter table VOLUNTEER_RECORD
	add constraint VOLUNTEER_RECORD_UPLOAD_FILES_ID_fk_2
		foreign key (VIDEO_FILE_ID) references UPLOAD_FILES
			on delete set null;


-- END OF VOLUNTEER_RECORD --



DROP TABLE PERFORMANCE_UNLOCK_STATUS;

create table PERFORMANCE_UNLOCK_STATUS
(
       ACTIVE_YEAR INTEGER not null,
       RGNO INTEGER not null,
       START_TIME TIMESTAMP(6) not null,
       END_TIME TIMESTAMP(6) not null,
       primary key (ACTIVE_YEAR, RGNO)
);


