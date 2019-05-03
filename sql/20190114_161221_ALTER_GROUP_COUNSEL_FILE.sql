alter table GROUP_COUNSEL drop column ORIGINAL_FILENAME;

call sysproc.admin_cmd('reorg table GROUP_COUNSEL');

alter table GROUP_COUNSEL drop column RANDOM_FILENAME;

call sysproc.admin_cmd('reorg table GROUP_COUNSEL');

alter table GROUP_COUNSEL
	add FILE_ID int;

call sysproc.admin_cmd('reorg table GROUP_COUNSEL');

alter table GROUP_COUNSEL
	add constraint GROUP_COUNSEL_UPLOAD_FILES_ID_fk
		foreign key (FILE_ID) references UPLOAD_FILES
			on delete cascade;