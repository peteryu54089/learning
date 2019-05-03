CREATE TABLE AUTOBIOGRAPHY (
    ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY,
    REGISTER_NUMBER VARCHAR(12) NOT NULL,
    "USE" VARCHAR(20) NOT NULL,
    DESCRIPTION VARCHAR(50) NOT NULL,
    FILE_PATH VARCHAR(50) NOT NULL,
    EXTRA_FILE_PATH VARCHAR(50) NOT NULL,
    PRIMARY KEY (ID)
);