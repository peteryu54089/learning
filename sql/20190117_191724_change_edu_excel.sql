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
         INNER JOIN EDU_EXCEL_4_1 e ON b.IDNO = e.IDNO AND c.SBJ_NUM = e.NEW_SBJ_NUM
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
                              ON a.TERM_YEAR = b.TERM_YEAR AND a.TERM_SEM = b.TERM_SEM AND a.RGNO = b.RGNO
                   INNER JOIN HS014357.SUBJECT c ON a.COURSE_NUM = c.SBJ_NUM
                   INNER JOIN HS014357.STU_BASIS d ON b.RGNO = d.RGNO
                   INNER JOIN EDU_EXCEL_4_2 e ON b.IDNO = e.IDNO AND c.SBJ_NUM = e.SBJ_NUM
       WHERE a.STATUS = '5'
);

