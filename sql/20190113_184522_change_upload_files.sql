alter table UPLOAD_FILES drop column DELETED_AT;

call sysproc.admin_cmd('reorg table UPLOAD_FILES');

