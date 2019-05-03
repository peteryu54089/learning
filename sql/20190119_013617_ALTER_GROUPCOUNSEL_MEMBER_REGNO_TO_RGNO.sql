alter table GROUP_COUNSEL_MEMBER rename column REGISTER_NUMBER to RGNO;

alter table GROUP_COUNSEL_MEMBER
	add constraint GROUP_COUNSEL_MEMBER_pk
		unique (GROUP_COUNSEL_ID, RGNO);