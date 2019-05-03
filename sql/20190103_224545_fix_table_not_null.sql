alter table INDIVIDUAL_COUNSEL alter column START_TIME set not null;

call sysproc.admin_cmd('reorg table INDIVIDUAL_COUNSEL');

alter table INDIVIDUAL_COUNSEL alter column END_TIME set not null;

call sysproc.admin_cmd('reorg table INDIVIDUAL_COUNSEL');

alter table INDIVIDUAL_COUNSEL alter column TITLE set not null;

call sysproc.admin_cmd('reorg table INDIVIDUAL_COUNSEL');

alter table INDIVIDUAL_COUNSEL alter column CREATED_AT set not null;

call sysproc.admin_cmd('reorg table INDIVIDUAL_COUNSEL');


alter table GROUP_COUNSEL alter column START_TIME set not null;

call sysproc.admin_cmd('reorg table GROUP_COUNSEL');

alter table GROUP_COUNSEL alter column END_TIME set not null;

call sysproc.admin_cmd('reorg table GROUP_COUNSEL');

alter table GROUP_COUNSEL alter column TITLE set not null;

call sysproc.admin_cmd('reorg table GROUP_COUNSEL');

alter table GROUP_COUNSEL alter column CREATED_AT set not null;

call sysproc.admin_cmd('reorg table GROUP_COUNSEL');