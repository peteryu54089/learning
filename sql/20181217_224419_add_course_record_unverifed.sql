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
         INNER JOIN HS014357.SUBJECT B ON A.COURSE_NUM = B.SBJ_NUM
         INNER JOIN HS014357.VCOURSE_TEACHING C ON B.SBJ_NUM = C.COURSE_NUM
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