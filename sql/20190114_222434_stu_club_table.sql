CREATE OR REPLACE VIEW HS_STU_CLUB_RECORD       AS (SELECT * FROM HS014357.STU_CLUB_RECORD);
CREATE OR REPLACE VIEW HS_STU_CLUB              AS (SELECT * FROM HS014357.STU_CLUB);
CREATE OR REPLACE VIEW HS_STU_CLUB_MEMBER_CADRE AS (SELECT * FROM HS014357.STU_CLUB_MEMBER_CADRE);

CREATE OR REPLACE VIEW STU_CLUB_RECORD AS (
    SELECT A.CR_NUM,
           SB.RGNO,
           B.CLUB_NAME             UNIT,
           TO_MG_DATE(A.CR_SDATE)  STARTTIME,
           TO_MG_DATE(A.CR_EDATE)  ENDTIME,
           
           A.SBJ_YEAR || A.SBJ_SEM TERM,
           D.CADRE_NAME            JOB,
           3                       LEVEL,
           
           CURRENT_TIMESTAMP       CREATED_AT,
           B.URL                   EXTERNAL_LINK,
           2                       SOURCE,
           
           NULL                    SUBMIT_YEAR,
           A.CR_DETAIL             CONTENT,
           0                       CHECK,
           0                       STATUS,
           NULL                    SELECTEDYEAR
    FROM HS_STU_CLUB_RECORD A
           INNER JOIN HS_STU_CLUB B ON A.CLUB_NUM = B.CLUB_NUM
           INNER JOIN HS_STU_CLUB_MEMBER_CADRE C
                      ON A.CLUB_NUM = C.CLUB_NUM
                        AND A.SBJ_YEAR = C.SBJ_YEAR
                        AND A.SBJ_SEM = C.SBJ_SEM
                        AND A.RGNO = C.RGNO
           INNER JOIN COMMON.STU_CLUB_CADRE D ON C.CADRE_NUM = D.CADRE_NUM
           INNER JOIN HS_STU_BASIS SB on A.RGNO = SB.RGNO
);

