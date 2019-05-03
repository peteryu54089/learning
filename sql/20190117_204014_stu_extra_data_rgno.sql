DROP TABLE EXTRA_STU_DATA;
CREATE TABLE
    EXTRA_STU_DATA
    (
        RGNO INTEGER NOT NULL,
        EMAIL VARCHAR(100),
        MOBILE VARCHAR(20),
        AVATAR VARCHAR(100),
        SOCIAL_ACCOUNT VARCHAR(50),
        BIO VARCHAR(255),
        INTEREST VARCHAR(100),
        NICKNAME VARCHAR(50),
        ALT_EMAIL VARCHAR(100),
        PRIMARY KEY (RGNO)
    );

CREATE OR REPLACE VIEW ACCOUNT (PK, REGISTER_NUMBER, TERM_YEAR, TERM_SEMESTER, PASSWORD, NAME, ENAME, ID_NUMBER, EMAIL, MOBILE_TELNO, ROLE_CODE, BIRTHDAY, AVATAR, SOURCE) AS   (
    SELECT
      STA.STAFF_CODE                                             PK,
      STA.STAFF_CODE                                             REGISTER_NUMBER,
      Y.YEAR                                                     TERM_YEAR,
      Y.SEMESTER                                                 TERM_SEMESTER,
      '(empty)'                                                  PASSWORD,
      STA.STAFF_CNAME                                            NAME,
      COALESCE(STA.STAFF_ENAME, '')                              ENAME,
      STA.IDNO                                                   ID_NUMBER,
      COALESCE(EXT_STA_DATA.EMAIL, STA.EMAIL)                    EMAIL,
      COALESCE(EXT_STA_DATA.MOBILE, '')                          MOBILE_TELNO,
      BITOR(COALESCE(RE.ROLE_VALUE, 0), STA_ROLE_INFO.ROLE_CODE) ROLE_CODE,
      NULL                                                       BIRTHDAY,
      EXT_STA_DATA.AVATAR                                        AVATAR,
      'STAFF' AS                                                 SOURCE
    FROM HS_STAFF STA
           INNER JOIN (
        SELECT
          STAFF_CODE,
          SUM(ROLE_CODE) ROLE_CODE
        FROM (
               SELECT
                 STA.STAFF_CODE,
                 32 ROLE_CODE
               FROM
                 HS_STAFF STA
               WHERE STA.STAFF_CODE IN (SELECT DISTINCT STAFF_CODE FROM HS_VCOURSE_TEACHING)
               UNION ALL
               SELECT
                 STA.STAFF_CODE,
                 16 ROLE_CODE
               FROM
                 HS_STAFF STA
               WHERE STA.STAFF_CODE IN (SELECT DISTINCT STAFF_CODE FROM HS_COU_DOCENT)
               UNION ALL
               SELECT
                 STA.STAFF_CODE,
                 0 ROLE_CODE
               FROM
                 HS_STAFF STA
             ) STA_WITH_ROLE
        GROUP BY STAFF_CODE
      ) AS STA_ROLE_INFO ON STA.STAFF_CODE = STA_ROLE_INFO.STAFF_CODE
           LEFT JOIN EXTRA_STAFF_DATA EXT_STA_DATA ON EXT_STA_DATA.STAFF_CODE = STA.STAFF_CODE
           CROSS JOIN TABLE(LEARNING.CURRENT_SEMESTER()) Y
           LEFT JOIN ROLE_EXTEND RE ON RE.SOURCE = 'STAFF' AND RE.KEY = STA.STAFF_CODE
    UNION ALL
    SELECT
      VARCHAR(STU.RGNO)                    PK,
      STU.REG_NO                           REGISTER_NUMBER,
      INTEGER(REG.SBJ_YEAR)                TERM_YEAR,
      INTEGER(REG.SBJ_SEM)                 TERM_SEMESTER,
      '(empty)'                            PASSWORD,
      STU.CNAME                            NAME,
      STU.ENAME                            ENAME,
      STU.IDNO                             ID_NUMBER,
      COALESCE(EXT_STU_DATA.EMAIL, '')     EMAIL,
      COALESCE(EXT_STU_DATA.MOBILE, '')    MOBILE_TELNO,
      BITOR(COALESCE(RE.ROLE_VALUE, 0), 8) ROLE_CODE,
      STU.BIRTHDAY                         BIRTHDAY,
      EXT_STU_DATA.AVATAR                  AVATAR,
      'STUDENT' AS                         SOURCE
    FROM
      HS_STU_BASIS STU
        INNER JOIN HS_STU_REGISTER REG ON REG.RGNO = STU.RGNO
        LEFT JOIN EXTRA_STU_DATA EXT_STU_DATA ON STU.RGNO = EXT_STU_DATA.RGNO
        LEFT JOIN ROLE_EXTEND RE ON RE.SOURCE = 'STUDENT' AND RE.KEY = VARCHAR(STU.RGNO)
    WHERE
        REG.REG_FLAG = '1'
    UNION ALL
    SELECT
      VARCHAR(EXT_ACCOUNT.ID)                PK,
      EXT_ACCOUNT.REGISTER_NUMBER,
      EXT_ACCOUNT.TERM_YEAR,
      EXT_ACCOUNT.TERM_SEMESTER,
      EXT_ACCOUNT.PASSWORD,
      EXT_ACCOUNT.NAME,
      EXT_ACCOUNT.ENAME,
      EXT_ACCOUNT.ID_NUMBER,
      COALESCE(EXT_ACCOUNT.EMAIL, '')        EMAIL,
      COALESCE(EXT_ACCOUNT.MOBILE_TELNO, '') MOBILE_TELNO,
      BITOR(COALESCE(RE.ROLE_VALUE, 0), 0)   ROLE_CODE,
      EXT_ACCOUNT.BIRTHDAY                   BIRTHDAY,
      EXT_ACCOUNT.AVATAR                     AVATAR,
      'EXTRA' AS                             SOURCE
    FROM
      EXTRA_ACCOUNT EXT_ACCOUNT
        LEFT JOIN ROLE_EXTEND RE ON RE.SOURCE = 'EXTRA' AND RE.KEY = EXT_ACCOUNT.REGISTER_NUMBER
  );

