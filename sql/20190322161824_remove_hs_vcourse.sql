CREATE OR REPLACE VIEW VCOURSE_TEACHING AS
SELECT b.SBJ_YEAR    term_year,
       b.SBJ_SEM     term_sem,
       d.CUNIT_CNAME class_cname,
       a.SBJ_NUM     course_num,
       b.CURR_CNAME  course_cname,
       a.STAFF_CODE  staff_code
FROM HS_TEACHING a
       JOIN HS_SUBJECT b ON a.SBJ_NUM = b.SBJ_NUM
       JOIN HS_CLASS_SBJ c ON b.SBJ_NUM = c.SBJ_NUM
       JOIN HS_CUNIT_NAME d ON c.CUNIT_NUM = d.CUNIT_NUM AND b.SBJ_YEAR = d.SBJ_YEAR AND b.SBJ_SEM = d.SBJ_SEM;

CREATE OR REPLACE VIEW VCOURSE AS
SELECT a.sbj_year    term_year,
       a.sbj_sem     term_sem,
       c.cunit_cname class_cname,
       a.sbj_num     course_num,
       a.curr_cname  course_cname,
       a.curr_ename  course_ename,
       a.rcredit     rcredit,
       a.rhour       rhour,
       a.remark      remark
FROM hs_subject a
       JOIN hs_class_sbj b ON a.sbj_num = b.sbj_num
       JOIN hs_cunit_name c ON b.cunit_num = c.cunit_num AND a.sbj_year = c.sbj_year AND a.sbj_sem = c.sbj_sem;



CREATE OR REPLACE VIEW VCOURSE_SELECT AS (
  SELECT a.SBJ_YEAR    term_year,
         a.SBJ_SEM     term_sem,
         d.CUNIT_CNAME class_cname,
         a.SBJ_NUM     course_num,
         b.CURR_CNAME  course_cname,
         e.REG_NO      reg_no,
         e.RGNO        rgno
  FROM HS_CSELECT a
         JOIN HS_SUBJECT b ON a.SBJ_NUM = b.SBJ_NUM
         JOIN HS_CLASS_SBJ c ON b.SBJ_NUM = c.SBJ_NUM
         JOIN HS_CUNIT_NAME d ON c.CUNIT_NUM = d.CUNIT_NUM AND b.SBJ_YEAR = d.SBJ_YEAR AND b.SBJ_SEM = d.SBJ_SEM
         JOIN HS_STU_BASIS e ON a.RGNO = e.RGNO);

CREATE OR REPLACE VIEW COURSE_INFO AS
SELECT TERM_SEM,
       TERM_YEAR,
       CLASS_CNAME,
       COURSE_NUM,
       COURSE_CNAME,
       listagg(STAFF_CNAME, ',') within group (ORDER BY STAFF_CNAME) as names
FROM (SELECT C.TERM_SEM, C.TERM_YEAR, C.CLASS_CNAME, C.COURSE_CNAME, C.COURSE_NUM, D.STAFF_CNAME
      FROM (SELECT A.COURSE_CNAME, A.TERM_YEAR, A.TERM_SEM, A.CLASS_CNAME, A.COURSE_NUM, B.STAFF_CODE
            FROM VCOURSE AS A
                   LEFT JOIN HS_TEACHING AS B
                             ON A.COURSE_NUM = B.SBJ_NUM) AS C,
           (SELECT STAFF_CODE, STAFF_CNAME FROM HS_STAFF ORDER BY STAFF_CODE) AS D
      WHERE C.STAFF_CODE = D.STAFF_CODE)
GROUP BY TERM_SEM, TERM_YEAR, CLASS_CNAME, COURSE_NUM, COURSE_CNAME;

CREATE OR REPLACE VIEW stu_course_result AS (
  SELECT *
  FROM (
         SELECT H.SBJ_NUM,
                H.SBJ_YEAR,
                H.SBJ_SEM,
                H.RGNO,
                H.RETAKE_FLAG,
                H.REGULAR_SCORE,
                H.PERIOD1_SCORE,
                H.PERIOD2_SCORE,
                H.FINAL_SCORE,
                H.SEM_SCORE,
                H.SEM_STAR_SIGN,
                H.MAKEUP_SCORE,
                H.MAKEUP_STAR_SIGN,
                H.COURSE_CNAME,
                listagg(STAFF_CNAME, ',') within GROUP (ORDER BY STAFF_CNAME) AS names
         FROM (SELECT E.*, G.STAFF_CNAME
               FROM (SELECT C.*, D.COURSE_CNAME
                     FROM (SELECT A.SBJ_NUM,
                                  A.SBJ_YEAR,
                                  A.SBJ_SEM,
                                  A.RGNO,
                                  A.RETAKE_FLAG,
                                  B.REGULAR_SCORE,
                                  B.PERIOD1_SCORE,
                                  B.PERIOD2_SCORE,
                                  B.FINAL_SCORE,
                                  B.SEM_SCORE,
                                  B.SEM_STAR_SIGN,
                                  B.MAKEUP_SCORE,
                                  B.MAKEUP_STAR_SIGN
                           FROM HS_CSELECT AS A
                                  LEFT JOIN HS_STU_SEM_SCORE AS B
                                            ON A.CSEL_NUM = B.CSEL_NUM) AS C,
                          VCOURSE AS D
                     WHERE C.SBJ_NUM = D.COURSE_NUM) AS E,
                    HS_TEACHING AS F,
                    HS_STAFF AS G
               WHERE E.SBJ_NUM = F.SBJ_NUM
                 AND F.STAFF_CODE = G.STAFF_CODE) AS H
         GROUP BY H.SBJ_NUM, H.SBJ_YEAR, H.SBJ_SEM, H.RGNO, H.RETAKE_FLAG, H.REGULAR_SCORE, H.PERIOD1_SCORE,
                  H.PERIOD2_SCORE, H.FINAL_SCORE, H.SEM_SCORE, H.SEM_STAR_SIGN, H.MAKEUP_SCORE, H.MAKEUP_STAR_SIGN,
                  H.COURSE_CNAME)
);

CREATE OR REPLACE VIEW STU_SELECT_COURSE AS (
  SELECT C.RGNO, B.*
  FROM VCOURSE_SELECT AS A
         INNER JOIN COURSE_INFO AS B ON A.COURSE_NUM = B.COURSE_NUM
         INNER JOIN STU_RGNO_MAPPING C ON A.TERM_YEAR = C.TERM_YEAR AND A.TERM_SEM = C.TERM_SEM AND A.REG_NO = C.REG_NO
);

CREATE OR REPLACE VIEW ACCOUNT AS (
  SELECT STA.STAFF_CODE                                             PK,
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
    SELECT STAFF_CODE,
           SUM(ROLE_CODE) ROLE_CODE
    FROM (
           SELECT STA.STAFF_CODE,
                  32 ROLE_CODE
           FROM HS_STAFF STA
           WHERE STA.STAFF_CODE IN (SELECT DISTINCT STAFF_CODE FROM VCOURSE_TEACHING)
           UNION ALL
           SELECT STA.STAFF_CODE,
                  16 ROLE_CODE
           FROM HS_STAFF STA
           WHERE STA.STAFF_CODE IN (SELECT DISTINCT STAFF_CODE FROM HS_STU_DOCENT)
           UNION ALL
           SELECT STA.STAFF_CODE,
                  0 ROLE_CODE
           FROM HS_STAFF STA
         ) STA_WITH_ROLE
    GROUP BY STAFF_CODE
  ) AS STA_ROLE_INFO ON STA.STAFF_CODE = STA_ROLE_INFO.STAFF_CODE
         LEFT JOIN EXTRA_STAFF_DATA EXT_STA_DATA ON EXT_STA_DATA.STAFF_CODE = STA.STAFF_CODE
         CROSS JOIN TABLE(COMMON.CURRENT_SEMESTER()) Y
         LEFT JOIN ROLE_EXTEND RE ON RE.SOURCE = 'STAFF' AND RE.KEY = STA.STAFF_CODE
  UNION ALL
  SELECT VARCHAR(STU.RGNO)                                   PK,
         STU.REG_NO                                          REGISTER_NUMBER,
         INTEGER(REG.SBJ_YEAR)                               TERM_YEAR,
         INTEGER(REG.SBJ_SEM)                                TERM_SEMESTER,
         '(empty)'                                           PASSWORD,
         STU.CNAME                                           NAME,
         STU.ENAME                                           ENAME,
         STU.IDNO                                            ID_NUMBER,
         COALESCE(EXT_STU_DATA.EMAIL, STU.EMAIL, '')         EMAIL,
         COALESCE(EXT_STU_DATA.MOBILE, STU.MOBILE_TELNO, '') MOBILE_TELNO,
         BITOR(COALESCE(RE.ROLE_VALUE, 0), 8)                ROLE_CODE,
         STU.BIRTHDAY                                        BIRTHDAY,
         EXT_STU_DATA.AVATAR                                 AVATAR,
         'STUDENT' AS                                        SOURCE
  FROM HS_STU_BASIS STU
         INNER JOIN HS_STU_REGISTER REG ON REG.RGNO = STU.RGNO
         LEFT JOIN EXTRA_STU_DATA EXT_STU_DATA ON STU.RGNO = EXT_STU_DATA.RGNO
         LEFT JOIN ROLE_EXTEND RE ON RE.SOURCE = 'STUDENT' AND RE.KEY = VARCHAR(STU.RGNO)
  WHERE REG.REG_FLAG = '1'
  UNION ALL
  SELECT VARCHAR(EXT_ACCOUNT.ID)                PK,
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
  FROM EXTRA_ACCOUNT EXT_ACCOUNT
         LEFT JOIN ROLE_EXTEND RE ON RE.SOURCE = 'EXTRA' AND RE.KEY = EXT_ACCOUNT.REGISTER_NUMBER
);


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
         INNER JOIN VCOURSE_TEACHING C ON B.SBJ_NUM = C.COURSE_NUM
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

drop view HS_VCOURSE;

drop view HS_VCOURSE_SELECT;

drop view HS_VCOURSE_TEACHING;
