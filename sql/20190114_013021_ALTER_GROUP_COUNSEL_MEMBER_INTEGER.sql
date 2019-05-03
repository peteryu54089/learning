alter table GROUP_COUNSEL_MEMBER alter column REGISTER_NUMBER set data type INTEGER;

call sysproc.admin_cmd('reorg table GROUP_COUNSEL_MEMBER');