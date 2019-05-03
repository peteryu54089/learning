CREATE TABLE UPLOAD_FILES
(
	ID int generated always as identity
		constraint UPLOAD_FILES_pk
			primary key,
	RGNO int,
	STAFF_CODE VARCHAR(12),
	FILE_PATH varchar(512) not null,
	FILE_NAME varchar(512) not null,
    TERM_YEAR int NOT NULL,
    TERM_SEM int NOT NULL,
	CREATED_AT TIMESTAMP default CURRENT_TIMESTAMP not null,
	DELETED_AT TIMESTAMP,
    CONSTRAINT UPLOAD_FILES_CK_AT_LEAST_ONE_USER_REQUIRED CHECK (
        (
            RGNO IS NOT NULL AND STAFF_CODE IS NULL
        ) OR (
            RGNO IS NULL AND STAFF_CODE IS NOT NULL
        )
    )
);
