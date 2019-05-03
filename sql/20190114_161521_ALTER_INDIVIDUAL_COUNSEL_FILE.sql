alter table INDIVIDUAL_COUNSEL drop column ORIGINAL_FILENAME;

call sysproc.admin_cmd('reorg table INDIVIDUAL_COUNSEL');

alter table INDIVIDUAL_COUNSEL drop column RANDOM_FILENAME;

call sysproc.admin_cmd('reorg table INDIVIDUAL_COUNSEL');

alter table INDIVIDUAL_COUNSEL
	add FILE_ID int;

call sysproc.admin_cmd('reorg table INDIVIDUAL_COUNSEL');

alter table INDIVIDUAL_COUNSEL
	add constraint INDIVIDUAL_COUNSEL_UPLOAD_FILES_ID_fk
		foreign key (FILE_ID) references UPLOAD_FILES
			on delete cascade;
			
alter table INDIVIDUAL_COUNSEL alter column REGISTER_NUMBER set data type INTEGER;

call sysproc.admin_cmd('reorg table INDIVIDUAL_COUNSEL');