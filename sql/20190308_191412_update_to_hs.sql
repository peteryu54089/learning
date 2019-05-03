CREATE OR REPLACE VIEW "HS_CURRICULAR" AS SELECT * FROM HS014357.CURRICULAR;

CREATE OR REPLACE VIEW "COURSE_INFO"
AS  
SELECT TERM_SEM,TERM_YEAR,CLASS_CNAME,COURSE_NUM,COURSE_CNAME,listagg(STAFF_CNAME,',') within group (ORDER BY STAFF_CNAME) as names
  FROM (SELECT C.TERM_SEM,C.TERM_YEAR,C.CLASS_CNAME,C.COURSE_CNAME,C.COURSE_NUM,D.STAFF_CNAME
        FROM (SELECT A.COURSE_CNAME,A.TERM_YEAR,A.TERM_SEM,A.CLASS_CNAME,A.COURSE_NUM,B.STAFF_CODE
              FROM  HS_VCOURSE AS A
                LEFT JOIN HS_TEACHING AS B
                  ON A.COURSE_NUM = B.SBJ_NUM) AS C,
          (SELECT STAFF_CODE,STAFF_CNAME FROM HS_STAFF ORDER BY STAFF_CODE) AS D
        WHERE C.STAFF_CODE = D.STAFF_CODE)
  GROUP BY TERM_SEM,TERM_YEAR,CLASS_CNAME,COURSE_NUM,COURSE_CNAME;

CREATE OR REPLACE VIEW COURSE_UNVERIFIED_LIST AS (
  SELECT A.COURSE_NUM,
         B.SBJ_YEAR,
         B.SBJ_SEM,
         B.CURR_CNAME,
         C.STAFF_CODE,
         D.NAME,
         D.EMAIL,
         A.TERM_YEAR RECORD_YEAR,
         A.TERM_SEM  RECORD_SEM,
         A.STATUS,
         COUNT(*)    CNT
  FROM STUDENT_COURSE_RECORD A
         INNER JOIN HS_SUBJECT B ON A.COURSE_NUM = B.SBJ_NUM
         INNER JOIN HS_VCOURSE_TEACHING C ON B.SBJ_NUM = C.COURSE_NUM
         INNER JOIN ACCOUNT D ON C.STAFF_CODE = D.REGISTER_NUMBER AND
                                 (D.SOURCE = 'STAFF' OR D.SOURCE = 'EXTRA')
  WHERE A.STATUS = 2
  GROUP BY A.COURSE_NUM,
           B.SBJ_YEAR,
           B.SBJ_SEM,
           B.CURR_CNAME,
           C.STAFF_CODE,
           D.NAME,
           D.EMAIL,
           A.TERM_YEAR,
           A.TERM_SEM,
           A.STATUS
  HAVING EMAIL != ''
);

CREATE OR REPLACE VIEW EDU_EXCEL_1 AS (
       SELECT CASE
                     WHEN COND LIKE '%部定%' AND COND LIKE '%一般%' AND COND LIKE '%必修%' THEN 1
                     WHEN COND LIKE '%部定%' AND COND LIKE '%實習%' AND COND LIKE '%必修%' THEN 2
                     WHEN COND LIKE '%部定%' AND COND LIKE '%專業%' AND COND LIKE '%必修%' THEN 3
                     WHEN COND LIKE '%部定%' AND COND LIKE '%選修%' THEN 4
                     WHEN COND LIKE '%部定%' AND COND LIKE '%活動%' AND COND LIKE '%必修%' THEN 5
                     WHEN COND LIKE '%校定%' AND COND LIKE '%一般%' AND COND LIKE '%必修%' THEN 6
                     WHEN COND LIKE '%校定%' AND COND LIKE '%一般%' AND COND LIKE '%選修%' THEN 7
                     WHEN COND LIKE '%校定%' AND COND LIKE '%專業%' AND COND LIKE '%必修%' THEN 8
                     WHEN COND LIKE '%校定%' AND COND LIKE '%專業%' AND COND LIKE '%選修%' THEN 9
                     WHEN COND LIKE '%校定%' AND COND LIKE '%實習%' AND COND LIKE '%必修%' THEN 10
                     WHEN COND LIKE '%校定%' AND COND LIKE '%實習%' AND COND LIKE '%選修%' THEN 11
                     WHEN COND LIKE '%校定%' AND COND LIKE '%專精%' AND COND LIKE '%選修%' THEN 12
                     WHEN COND LIKE '%必修%' THEN 13
                     WHEN COND LIKE '%選修%' THEN 14
                     END f1,
              'X'   f2,
              'X'   f3,
              'X'   f4,
              'X'   f5,
              n.*
       FROM (
                   SELECT a.TCREDIT                      TCREDIT,
                          a.CURR_CNAME                   course_cname,
                          a.CURR_CODE,
                          CAST(NULL AS INTEGER)          TERM_YEAR,
                          CAST(NULL AS INTEGER)          TERM_SEM,
                          (C2T.C2_CNAME || C3T.C3_CNAME) COND
                   FROM HS_CURRICULAR a
                               LEFT JOIN COMMON.CURR_OUTLINE C1T ON C1T.C1_CODE = a.C1_CODE
                               LEFT JOIN COMMON.CURR_OPTION C2T ON C2T.C2_CODE = a.C2_CODE
                               LEFT JOIN COMMON.CURR_CATEGORY C3T ON C3T.C3_CODE = a.C3_CODE
            ) n
);

CREATE OR REPLACE VIEW EDU_EXCEL_2 AS (
  SELECT a.IDNO,                          
         LEARNING.TO_MG_DATE(a.BIRTHDAY) BIRTHDAY, 
         b.DIV_CALIAS,                    
         d.GRADE,                         
         d.CLS_CNAME,                     
         c.CLS_NO,                        
         'XX'                   TYPE,     
         c.SBJ_YEAR             TERM_YEAR,
         c.SBJ_SEM              TERM_SEM
  FROM HS_STU_BASIS a
         LEFT JOIN HS_DIVISION b ON a.DIV_CODE = b.DIV_CODE
         INNER JOIN HS_STU_REGISTER c ON a.RGNO = c.RGNO
         INNER JOIN HS_STU_CLASS d
                    ON c.CLS_CODE = d.CLS_CODE AND c.SBJ_YEAR = d.SBJ_YEAR AND c.SBJ_SEM = d.SBJ_SEM
);

CREATE OR REPLACE VIEW EDU_EXCEL_3_1 AS (
       SELECT c.IDNO,
              LEARNING.TO_MG_DATE(c.BIRTHDAY)     BIRTHDAY,
              d.CURR_CODE,
              f.GRADE,
              d.RCREDIT,
              a.SEM_SCORE,
              CASE
                     WHEN a.SEM_STAR_SIGN IS NULL THEN 1

                     ELSE 0
                     END                      PASS,
              COALESCE(MAKEUP_SCORE, -1) RETEST_SCORE,
              CASE
                     WHEN MAKEUP_STAR_SIGN IS NULL THEN 1
                     ELSE 0
                     END                      RETEST_PASS,
              -1                         COUNT_IN,
              '??'                       DESCRIPTION,
              e.SBJ_YEAR                 TERM_YEAR,
              e.SBJ_SEM                  TERM_SEM
       FROM HS_STU_SEM_SCORE a
                   INNER JOIN HS_CSELECT b ON a.CSEL_NUM = b.CSEL_NUM
                   INNER JOIN HS_STU_BASIS c ON b.RGNO = c.RGNO
                   INNER JOIN HS_SUBJECT d ON b.SBJ_NUM = d.SBJ_NUM
                   INNER JOIN HS_STU_REGISTER e ON c.RGNO = e.RGNO
                   INNER JOIN HS_STU_CLASS f
                              ON e.CLS_CODE = f.CLS_CODE AND e.SBJ_YEAR = f.SBJ_YEAR AND e.SBJ_SEM = f.SBJ_SEM
       WHERE b.RETAKE_FLAG IS NULL
);

CREATE OR REPLACE VIEW EDU_EXCEL_3_2 AS (
  SELECT c.IDNO,
         LEARNING.TO_MG_DATE(c.BIRTHDAY)     BIRTHDAY,
         e.SBJ_YEAR                 TERM_YEAR,
         e.SBJ_SEM                  TERM_SEM,
         d.CURR_CODE,
         f.GRADE,
         d.RCREDIT,
         a.SEM_SCORE,
         CASE
           WHEN a.SEM_STAR_SIGN IS NULL THEN 1

           ELSE 0
           END                      RETAKE_PASS,
         COALESCE(MAKEUP_SCORE, -1) RETEST_SEM,
         CASE
           WHEN MAKEUP_STAR_SIGN IS NULL THEN 1
           ELSE 0
           END                      RETEST_PASS,
         -1                         COUNT_IN,
         '??'                       DESCRIPTION,
         d.SBJ_NUM
  FROM HS_STU_SEM_SCORE a
         INNER JOIN HS_CSELECT b ON a.CSEL_NUM = b.CSEL_NUM
         INNER JOIN HS_STU_BASIS c ON b.RGNO = c.RGNO
         INNER JOIN HS_SUBJECT d ON b.SBJ_NUM = d.SBJ_NUM
         INNER JOIN HS_STU_REGISTER e ON c.RGNO = e.RGNO
         INNER JOIN HS_STU_CLASS f
                    ON e.CLS_CODE = f.CLS_CODE AND e.SBJ_YEAR = f.SBJ_YEAR AND e.SBJ_SEM = f.SBJ_SEM
  WHERE b.RETAKE_FLAG = 1
);

CREATE OR REPLACE VIEW EDU_EXCEL_4_1 AS (
  SELECT a.IDNO,
         a.BIRTHDAY,
         a.TERM_YEAR O_YEAR,
         a.TERM_SEM  O_SEM,
         a.CURR_CODE,
         a.GRADE,
         a.RCREDIT,
         '-1'        METHOD,
         b.SEM_SCORE,
         b.IS_PASS,
         ''          DESCRIPTION,
         b.TERM_YEAR,
         b.TERM_SEM,
         a.SBJ_NUM,
         b.SBJ_NUM NEW_SBJ_NUM
  FROM EDU_EXCEL_4_1_TMP a
         INNER JOIN EDU_EXCEL_4_1_TMP b ON a.RGNO = b.RGNO AND a.SBJ_NUM = b.SBJ_NUM AND
                                           (a.TERM_YEAR || a.TERM_SEM) < (b.TERM_YEAR || b.TERM_SEM)
);

CREATE OR REPLACE VIEW EDU_EXCEL_4_1_TMP AS (
  SELECT e.REPEAT,
         e.REENROLL,
         d.CURR_CODE                CURR_CODE,
         c.RGNO                     RGNO,
         c.IDNO                     IDNO,         
         LEARNING.TO_MG_DATE(c.BIRTHDAY)     BIRTHDAY,     
         e.SBJ_YEAR                 TERM_YEAR,    
         e.SBJ_SEM                  TERM_SEM,     
         d.SBJ_NUM                  SBJ_NUM,      
         f.GRADE                    GRADE,        
         d.RCREDIT                  RCREDIT,      
         a.SEM_SCORE                SEM_SCORE,    
         CASE
           WHEN a.SEM_STAR_SIGN IS NULL THEN 1
           
           ELSE 0
           END                      IS_PASS,      
         COALESCE(MAKEUP_SCORE, -1) RE_TEST,      
         CASE
           WHEN MAKEUP_STAR_SIGN IS NULL THEN 1
           ELSE 0
           END                      RE_TEST_PASS, 
         -1                         COUNT_IN,     
         '??'                       DESCRIPTION   
  FROM HS_STU_SEM_SCORE a
         INNER JOIN HS_CSELECT b ON a.CSEL_NUM = b.CSEL_NUM
         INNER JOIN HS_STU_BASIS c ON b.RGNO = c.RGNO
         INNER JOIN HS_SUBJECT d ON b.SBJ_NUM = d.SBJ_NUM
         INNER JOIN HS_STU_REGISTER e ON c.RGNO = e.RGNO
         INNER JOIN HS_STU_CLASS f
                    ON e.CLS_CODE = f.CLS_CODE AND e.SBJ_YEAR = f.SBJ_YEAR AND e.SBJ_SEM = f.SBJ_SEM
  
);

CREATE OR REPLACE VIEW EDU_EXCEL_4_2 AS (
       SELECT a.IDNO,
              a.BIRTHDAY,
              a.TERM_YEAR O_YEAR,
              a.TERM_SEM  O_SEM,
              a.CURR_CODE,
              a.GRADE,
              a.RCREDIT,
              a.SEM_SCORE,
              a.IS_PASS,
              a.RE_TEST,
              a.RE_TEST_PASS,
              -1          COUNT_IN,
              ''          DESCRIPTION,
              a.TERM_YEAR,
              a.TERM_SEM,
              a.SBJ_NUM
       FROM EDU_EXCEL_4_1_TMP a
       WHERE a.REENROLL = 1
);

CREATE OR REPLACE VIEW EDU_EXCEL_6 AS (
  SELECT b.IDNO,
         LEARNING.TO_MG_DATE(b.BIRTHDAY) BIRTHDAY,
         CASE a.AR_CNAME
           WHEN '公假' THEN '01'
           WHEN '事假' THEN '02'
           WHEN '病假' THEN '03'
           WHEN '婚假' THEN '04'
           WHEN '產前假' THEN '05'
           WHEN '娩假' THEN '06'
           WHEN '陪產假' THEN '07'
           WHEN '流產假' THEN '08'
           WHEN '育嬰假' THEN '09'
           WHEN '生理假' THEN '10'
           WHEN '喪假' THEN '11'
           WHEN '曠課' THEN '12'
           ELSE '??'
           END                  TYPE,
         LEARNING.TO_MG_DATE(a.ABS_DATE) ABS_MG_DATE_S,
         LEARNING.TO_MG_DATE(a.ABS_DATE) ABS_MG_DATE_E,
         COUNT(*)               SECTION_COUNT,
         a.SBJ_YEAR TERM_YEAR,
         a.SBJ_SEM TERM_SEM
  FROM STU_ABSENCE a
         INNER JOIN HS_STU_BASIS b ON a.RGNO = b.RGNO
  WHERE a.SPRD_NAME NOT IN ('早自習', '午休', '第八節')
  GROUP BY b.IDNO, b.BIRTHDAY, a.ABS_DATE, a.AR_CNAME,a.SBJ_YEAR, a.SBJ_SEM
);

CREATE OR REPLACE VIEW EDU_EXCEL_7_1 AS (
       SELECT b.IDNO,
              LEARNING.TO_MG_DATE(d.BIRTHDAY) BIRTHDAY,
              c.CURR_CODE,
              'X'                    GRADE,
              c.RCREDIT,
              ''                     RECORD_LINK,
              a.CONTENT,
              ''                     VIDEO_LINK,
              a.ID                   CRID,
              a.TERM_YEAR,
              a.TERM_SEM
       FROM STUDENT_COURSE_RECORD a
                   INNER JOIN STU_RGNO_MAPPING b
                              ON a.RGNO = b.RGNO
                   INNER JOIN HS_SUBJECT c ON a.COURSE_NUM = c.SBJ_NUM
                   INNER JOIN HS_STU_BASIS d ON b.RGNO = d.RGNO
       WHERE a.STATUS = '5'
);

CREATE OR REPLACE VIEW EDU_EXCEL_7_2 AS (
  SELECT b.IDNO,
         LEARNING.TO_MG_DATE(d.BIRTHDAY) BIRTHDAY,
         e.TERM_YEAR,
         e.TERM_SEM,
         c.CURR_CODE,
         'X'                    GRADE,
         c.RCREDIT,
         ''                     RECORD_LINK,
         a.CONTENT,
         ''                     VIDEO_LINK,
         a.ID                   CRID
  FROM STUDENT_COURSE_RECORD a
         INNER JOIN STU_RGNO_MAPPING b
                    ON a.RGNO = b.RGNO
         INNER JOIN HS_SUBJECT c ON a.COURSE_NUM = c.SBJ_NUM
         INNER JOIN HS_STU_BASIS d ON b.RGNO = d.RGNO
         INNER JOIN EDU_EXCEL_3_2 e ON b.IDNO = e.IDNO AND c.SBJ_NUM = e.SBJ_NUM
  WHERE a.STATUS = '5'
);

CREATE OR REPLACE VIEW EDU_EXCEL_8_1 AS (
  SELECT d.IDNO,
         TO_MG_DATE(d.BIRTHDAY) BIRTHDAY,
         e.O_YEAR,
         e.O_SEM,
         e.SBJ_NUM,
         'X'                    GRADE,
         c.RCREDIT,
         e.METHOD,
         ''                     RECORD_LINK,
         a.CONTENT,
         ''                     VIDEO_LINK,
         a.ID                   CRID,
         a.TERM_YEAR,
         a.TERM_SEM
  FROM STUDENT_COURSE_RECORD a
         INNER JOIN STU_RGNO_MAPPING b
                    ON a.TERM_YEAR = b.TERM_YEAR AND a.TERM_SEM = b.TERM_SEM AND a.RGNO = b.RGNO
         INNER JOIN HS_SUBJECT c ON a.COURSE_NUM = c.SBJ_NUM
         INNER JOIN HS_STU_BASIS d ON b.RGNO = d.RGNO
         INNER JOIN EDU_EXCEL_4_1 e ON b.IDNO = e.IDNO AND c.SBJ_NUM = e.SBJ_NUM
  WHERE a.STATUS = '5'
);

CREATE OR REPLACE VIEW EDU_EXCEL_8_2 AS (
       SELECT d.IDNO,
              TO_MG_DATE(d.BIRTHDAY) BIRTHDAY,
              e.O_YEAR,
              e.O_SEM,
              e.SBJ_NUM,
              'X'                    GRADE,
              c.RCREDIT,
              ''                     RECORD_LINK,
              a.CONTENT,
              ''                     VIDEO_LINK,
              a.ID                   CRID,
              a.TERM_YEAR,
              a.TERM_SEM
       FROM STUDENT_COURSE_RECORD a
                   INNER JOIN STU_RGNO_MAPPING b
                              ON a.RGNO = b.RGNO
                   INNER JOIN HS_SUBJECT c ON a.COURSE_NUM = c.SBJ_NUM
                   INNER JOIN HS_STU_BASIS d ON b.RGNO = d.RGNO
                   INNER JOIN EDU_EXCEL_4_2 e ON b.IDNO = e.IDNO AND c.SBJ_NUM = e.SBJ_NUM
       WHERE a.STATUS = '5'
);

CREATE OR REPLACE VIEW
    STU_CLASS_INFO
    (
        TERM_YEAR,
        TERM_SEM,
        PK,
        REG_NO,
        IDNO,
        CNAME,
        CLS_CODE,
        CLS_CNAME,
        CLS_NO,
        GRADE
    ) AS
    (
        SELECT
            R.SBJ_YEAR TERM_YEAR,
            R.SBJ_SEM  TERM_SEM,
            B.RGNO     PK,
            B.REG_NO,
            B.IDNO,
            B.CNAME,
            SC.CLS_CODE,
            SC.CLS_CNAME,
            R.CLS_NO,
            SC.GRADE
        FROM
            HS_STU_BASIS B
        INNER JOIN
            HS_STU_REGISTER R
        ON
            B.RGNO = R.RGNO
        INNER JOIN
            HS_STU_CLASS SC
        ON
            R.SBJ_YEAR = SC.SBJ_YEAR
        AND R.SBJ_SEM = SC.SBJ_SEM
        AND R.CLS_CODE = SC.CLS_CODE
    );
    
CREATE OR REPLACE VIEW
    STU_CLASS_INFO_WITH_TUTOR
    (
        TERM_YEAR,
        TERM_SEM,
        PK,
        REG_NO,
        IDNO,
        CNAME,
        CLS_CODE,
        CLS_CNAME,
        CLS_NO,
        GRADE,
        STAFF_CODE
    ) AS
    (
        SELECT
            SC.*,
            TUTOR.STAFF_CODE
        FROM
            STU_CLASS_INFO SC
        LEFT JOIN
            HS_COU_DOCENT TUTOR
        ON
            SC.TERM_YEAR = TUTOR.SBJ_YEAR
        AND SC.TERM_SEM = TUTOR.SBJ_SEM
        AND SC.CLS_CODE = TUTOR.CLS_CODE
    );

CREATE OR REPLACE VIEW
    STU_COURSE_RECORD_WITH_DETAILS
    (
        ID,
        RGNO,
        TERM_YEAR,
        TERM_SEM,
        COURSE_NUM,
        COURSE_CNAME,
        CONTENT,
        STATUS,
        SUBMITTED_AT,
        CREATED_AT,
        DELETED_AT,
        VERIFIED_AT,
        VERIFY_MESSAGE,
        VERIFIER_STAFF_CODE,
        CHECK,
        UPLOAD_TO_GLOBAL_TIME,
        UPLOAD_TO_GLOBAL_STAFF_CODE,
        CNAME,
        STAFF_CNAME,
        REG_NO
    ) AS
    (
        SELECT
            A.*,
            B.CNAME,
            C.STAFF_CNAME,
            B.REG_NO
        FROM
            STUDENT_COURSE_RECORD AS A
        INNER JOIN
            HS_STU_BASIS AS B
        ON
            A.RGNO = B.RGNO
        LEFT JOIN
            HS_STAFF C
        ON
            A.VERIFIER_STAFF_CODE = C.STAFF_CODE
    );