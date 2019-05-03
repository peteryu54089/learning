DROP TABLE STUDYPLAN_2;
CREATE TABLE STUDY_PLAN
(
	ID INTEGER generated always as identity
		primary key,
	TOPIC VARCHAR(20) not null,
	DESCRIPTION VARCHAR(50) not null,
	CREATED_AT TIMESTAMP(6) default CURRENT TIMESTAMP not null,
	TERM_YEAR INTEGER,
	TERM_SEMESTER INTEGER,
	RGNO INTEGER,
	MAIN_FILE_ID INTEGER
			references UPLOAD_FILES
				on delete cascade,
	SUB_FILE_ID INTEGER
			references UPLOAD_FILES
				on delete cascade
);

