alter table AUTOBIOGRAPHY alter column TERM_YEAR set not null;
call sysproc.admin_cmd('reorg table AUTOBIOGRAPHY');

alter table AUTOBIOGRAPHY alter column TERM_SEMESTER set not null;
call sysproc.admin_cmd('reorg table AUTOBIOGRAPHY');

alter table CADRE_RECORD alter column REGISTER_NUMBER set not null;
call sysproc.admin_cmd('reorg table CADRE_RECORD');

alter table COMPETITION_RECORD alter column REGISTER_NUMBER set not null;
call sysproc.admin_cmd('reorg table COMPETITION_RECORD');

alter table LICENSE_RECORD alter column REGISTER_NUMBER set not null;
call sysproc.admin_cmd('reorg table LICENSE_RECORD');


alter table OTHER_RECORD alter column REGISTER_NUMBER set not null;
call sysproc.admin_cmd('reorg table OTHER_RECORD');


alter table VOLUNTEER_RECORD alter column REGISTER_NUMBER set not null;
call sysproc.admin_cmd('reorg table VOLUNTEER_RECORD');

