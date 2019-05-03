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
         FROM HS014357.CURRICULAR a
                LEFT JOIN COMMON.CURR_OUTLINE C1T ON C1T.C1_CODE = a.C1_CODE
                LEFT JOIN COMMON.CURR_OPTION C2T ON C2T.C2_CODE = a.C2_CODE
                LEFT JOIN COMMON.CURR_CATEGORY C3T ON C3T.C3_CODE = a.C3_CODE
       ) n
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
  FROM HS014357.STU_SEM_SCORE a
         INNER JOIN HS014357.CSELECT b ON a.CSEL_NUM = b.CSEL_NUM
         INNER JOIN HS014357.STU_BASIS c ON b.RGNO = c.RGNO
         INNER JOIN HS014357.SUBJECT d ON b.SBJ_NUM = d.SBJ_NUM
         INNER JOIN HS014357.STU_REGISTER e ON c.RGNO = e.RGNO
         INNER JOIN HS014357.STU_CLASS f
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
  FROM HS014357.STU_SEM_SCORE a
         INNER JOIN HS014357.CSELECT b ON a.CSEL_NUM = b.CSEL_NUM
         INNER JOIN HS014357.STU_BASIS c ON b.RGNO = c.RGNO
         INNER JOIN HS014357.SUBJECT d ON b.SBJ_NUM = d.SBJ_NUM
         INNER JOIN HS014357.STU_REGISTER e ON c.RGNO = e.RGNO
         INNER JOIN HS014357.STU_CLASS f
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
         INNER JOIN HS014357.SUBJECT c ON a.COURSE_NUM = c.SBJ_NUM
         INNER JOIN HS014357.STU_BASIS d ON b.RGNO = d.RGNO
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
         INNER JOIN HS014357.SUBJECT c ON a.COURSE_NUM = c.SBJ_NUM
         INNER JOIN HS014357.STU_BASIS d ON b.RGNO = d.RGNO
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
         INNER JOIN HS014357.SUBJECT c ON a.COURSE_NUM = c.SBJ_NUM
         INNER JOIN HS014357.STU_BASIS d ON b.RGNO = d.RGNO
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
         INNER JOIN HS014357.SUBJECT c ON a.COURSE_NUM = c.SBJ_NUM
         INNER JOIN HS014357.STU_BASIS d ON b.RGNO = d.RGNO
         INNER JOIN EDU_EXCEL_4_2 e ON b.IDNO = e.IDNO AND c.SBJ_NUM = e.SBJ_NUM
  WHERE a.STATUS = '5'
);

