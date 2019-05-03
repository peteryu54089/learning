CREATE OR REPLACE FUNCTION TO_MG_DATE(d DATE)
  RETURNS CHAR(7)
  LANGUAGE SQL
RETURN
  SELECT (RIGHT('000' || (YEAR(d) - 1911), 3) || RIGHT('00' || MONTH(d), 2) ||
          RIGHT('00' || DAY(d), 2))
  FROM SYSIBM.SYSDUMMY1;


CREATE OR REPLACE VIEW STU_RGNO_MAPPING AS (
  SELECT R.SBJ_YEAR TERM_YEAR,
         R.SBJ_SEM  TERM_SEM,
         SB.REG_NO,
         SB.IDNO,
         R.RGNO     RGNO,
         SC.GRADE
  FROM HS014357.STU_REGISTER R
         INNER JOIN HS014357.STU_BASIS SB
                    ON R.RGNO = SB.RGNO
         INNER JOIN HS014357.STU_CLASS SC
                    ON R.SBJ_YEAR = SC.SBJ_YEAR
                      and R.SBJ_SEM = SC.SBJ_SEM
                      and R.CLS_CODE = SC.CLS_CODE
);


--
CREATE OR REPLACE VIEW EDU_EXCEL_1 AS (
  SELECT -1           f1,           -- 課程類別
         'X'          f2,           -- 領域別
         'X'          f3,           -- 群別
         'X'          f4,           -- 科/班/學程別
         'X'          f5,           -- 開課年級
         a.RCREDIT    rcredit,      -- 學分數
         a.CURR_CNAME course_cname, -- 科目名稱
         a.SBJ_NUM,
         c.SBJ_YEAR   TERM_YEAR,
         c.SBJ_SEM    TERM_SEM
  FROM HS014357.SUBJECT a
         JOIN HS014357.CLASS_SBJ b ON a.SBJ_NUM = b.SBJ_NUM
         JOIN HS014357.CUNIT_NAME c ON b.CUNIT_NUM = c.CUNIT_NUM AND a.SBJ_YEAR = c.SBJ_YEAR AND a.SBJ_SEM = c.SBJ_SEM
);


CREATE OR REPLACE VIEW EDU_EXCEL_2 AS (
  SELECT a.IDNO,                          -- 身分證號
         TO_MG_DATE(a.BIRTHDAY) BIRTHDAY, -- 出生日期
         b.DIV_CALIAS,                    -- 組別
         d.GRADE,                         -- 年級
         d.CLS_CNAME,                     -- 班級
         c.CLS_NO,                        -- 座號 TODO: 延修生
         'XX'                   TYPE,     -- 修業類型
         c.SBJ_YEAR             TERM_YEAR,
         c.SBJ_SEM              TERM_SEM
  FROM HS014357.STU_BASIS a
         LEFT JOIN HS014357.DIVISION b ON a.DIV_CODE = b.DIV_CODE
         INNER JOIN HS014357.STU_REGISTER c ON a.RGNO = c.RGNO
         INNER JOIN HS014357.STU_CLASS d
                    ON c.CLS_CODE = d.CLS_CODE AND c.SBJ_YEAR = d.SBJ_YEAR AND c.SBJ_SEM = d.SBJ_SEM
);

CREATE OR REPLACE VIEW EDU_EXCEL_3_1 AS (
  SELECT c.IDNO,                                  -- 身分證號
         TO_MG_DATE(c.BIRTHDAY)     BIRTHDAY,     -- 出生日期
         d.SBJ_NUM,                               -- 科目代碼
         f.GRADE,                                 -- 開課年級
         d.RCREDIT,                               -- 科目學分
         a.SEM_SCORE,                             -- 原始成績
         CASE
           WHEN a.SEM_STAR_SIGN IS NULL THEN 1
           -- WHEN a.SEM_STAR_SIGN = '*' THEN 0
           ELSE 0
           END                      PASS,         -- 成績及格
         COALESCE(MAKEUP_SCORE, -1) RETEST_SCORE, -- 補考成績
         CASE
           WHEN MAKEUP_STAR_SIGN IS NULL THEN 1
           ELSE 0
           END                      RETEST_PASS,  -- 補考及格
         -1                         COUNT_IN,     -- 是否採計學分
         '??'                       DESCRIPTION,  -- 質性描述
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
  SELECT c.IDNO,                                 -- 身分證號
         TO_MG_DATE(c.BIRTHDAY)     BIRTHDAY,    -- 出生日期
         e.SBJ_YEAR                 TERM_YEAR,   -- 開課學年度
         e.SBJ_SEM                  TERM_SEM,    -- 開課學期
         d.SBJ_NUM,                              -- 科目代碼
         f.GRADE,                                -- 開課年級
         d.RCREDIT,                              -- 科目學分
         a.SEM_SCORE,                            -- 補修原始成績
         CASE
           WHEN a.SEM_STAR_SIGN IS NULL THEN 1
           -- WHEN a.SEM_STAR_SIGN = '*' THEN 0
           ELSE 0
           END                      RETAKE_PASS, -- 補修成績及格
         COALESCE(MAKEUP_SCORE, -1) RETEST_SEM,  -- 補考成績
         CASE
           WHEN MAKEUP_STAR_SIGN IS NULL THEN 1
           ELSE 0
           END                      RETEST_PASS, -- 補考及格
         -1                         COUNT_IN,    -- 是否採計學分
         '??'                       DESCRIPTION  -- 質性描述
  FROM HS014357.STU_SEM_SCORE a
         INNER JOIN HS014357.CSELECT b ON a.CSEL_NUM = b.CSEL_NUM
         INNER JOIN HS014357.STU_BASIS c ON b.RGNO = c.RGNO
         INNER JOIN HS014357.SUBJECT d ON b.SBJ_NUM = d.SBJ_NUM
         INNER JOIN HS014357.STU_REGISTER e ON c.RGNO = e.RGNO
         INNER JOIN HS014357.STU_CLASS f
                    ON e.CLS_CODE = f.CLS_CODE AND e.SBJ_YEAR = f.SBJ_YEAR AND e.SBJ_SEM = f.SBJ_SEM
  WHERE b.RETAKE_FLAG = 1
);



CREATE OR REPLACE VIEW EDU_EXCEL_4_1_TMP AS (
  SELECT e.REPEAT,
         e.REENROLL,
         d.CURR_CODE                CURR_CODE,
         c.RGNO                     RGNO,
         c.IDNO                     IDNO,         -- 身分證號
         TO_MG_DATE(c.BIRTHDAY)     BIRTHDAY,     -- 出生日期
         e.SBJ_YEAR                 TERM_YEAR,    -- 開課學年度
         e.SBJ_SEM                  TERM_SEM,     -- 開課學期
         d.SBJ_NUM                  SBJ_NUM,      -- 科目代碼
         f.GRADE                    GRADE,        -- 開課年級
         d.RCREDIT                  RCREDIT,      -- 科目學分
         a.SEM_SCORE                SEM_SCORE,    -- 原始成績
         CASE
           WHEN a.SEM_STAR_SIGN IS NULL THEN 1
           -- WHEN a.SEM_STAR_SIGN = '*' THEN 0
           ELSE 0
           END                      IS_PASS,      -- 成績及格
         COALESCE(MAKEUP_SCORE, -1) RE_TEST,      -- 補考成績
         CASE
           WHEN MAKEUP_STAR_SIGN IS NULL THEN 1
           ELSE 0
           END                      RE_TEST_PASS, -- 補考及格
         -1                         COUNT_IN,     -- 是否採計學分
         '??'                       DESCRIPTION   -- 質性描述
  FROM HS014357.STU_SEM_SCORE a
         INNER JOIN HS014357.CSELECT b ON a.CSEL_NUM = b.CSEL_NUM
         INNER JOIN HS014357.STU_BASIS c ON b.RGNO = c.RGNO
         INNER JOIN HS014357.SUBJECT d ON b.SBJ_NUM = d.SBJ_NUM
         INNER JOIN HS014357.STU_REGISTER e ON c.RGNO = e.RGNO
         INNER JOIN HS014357.STU_CLASS f
                    ON e.CLS_CODE = f.CLS_CODE AND e.SBJ_YEAR = f.SBJ_YEAR AND e.SBJ_SEM = f.SBJ_SEM
  --     INNER JOIN HS014357.CURRICULAR g ON d.CURR_CODE = g.CURR_CODE
);

CREATE OR REPLACE VIEW EDU_EXCEL_4_1 AS (
  SELECT a.IDNO,                  -- 身分證號
         a.BIRTHDAY,              -- 出生日期
         a.TERM_YEAR O_YEAR,      -- 原修課學年度
         a.TERM_SEM  O_SEM,       -- 原修課學期
         a.SBJ_NUM,               -- 原修課科目代碼
         a.GRADE,                 -- 原修課開課年級
         a.RCREDIT,               -- 原修課科目學分
         '-1'        METHOD,      -- 重修方式
         b.SEM_SCORE,             -- 重修成績
         b.IS_PASS,               -- 重修及格
         ''          DESCRIPTION, -- 質性描述
         b.TERM_YEAR,
         b.TERM_SEM,
         b.SBJ_NUM NEW_SBJ_NUM
  FROM EDU_EXCEL_4_1_TMP a
         INNER JOIN EDU_EXCEL_4_1_TMP b ON a.RGNO = b.RGNO AND a.CURR_CODE = b.CURR_CODE AND
                                           (a.TERM_YEAR || a.TERM_SEM) < (b.TERM_YEAR || b.TERM_SEM)
);

CREATE OR REPLACE VIEW EDU_EXCEL_4_2 AS (
  SELECT a.IDNO,                  -- 身分證號
         a.BIRTHDAY,              -- 出生日期
         a.TERM_YEAR O_YEAR,      -- 課學年度
         a.TERM_SEM  O_SEM,       -- 修課學期
         a.SBJ_NUM,               -- 科目代碼
         a.GRADE,                 -- 開課年級
         a.RCREDIT,               -- 科目學分
         a.SEM_SCORE,             -- 原始成績
         a.IS_PASS,               -- 成績及格
         a.RE_TEST,               -- 補考成績
         a.RE_TEST_PASS,          -- 補考及格
         -1          COUNT_IN,    -- 是否採計學分
         ''          DESCRIPTION, -- 質性描述
         a.TERM_YEAR,
         a.TERM_SEM
  FROM EDU_EXCEL_4_1_TMP a
  WHERE a.REENROLL = 1
);

CREATE OR REPLACE VIEW EDU_EXCEL_6 AS (
  SELECT b.IDNO,
         TO_MG_DATE(b.BIRTHDAY) BIRTHDAY,
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
         TO_MG_DATE(a.ABS_DATE) ABS_MG_DATE_S,
         TO_MG_DATE(a.ABS_DATE) ABS_MG_DATE_E,
         COUNT(*)               SECTION_COUNT,
         a.SBJ_YEAR TERM_YEAR,
         a.SBJ_SEM TERM_SEM
  FROM STU_ABSENCE a
         INNER JOIN HS014357.STU_BASIS b ON a.RGNO = b.RGNO
  WHERE a.SPRD_NAME NOT IN ('早自習', '午休', '第八節')
  GROUP BY b.IDNO, b.BIRTHDAY, a.ABS_DATE, a.AR_CNAME,a.SBJ_YEAR, a.SBJ_SEM
);



CREATE OR REPLACE VIEW EDU_EXCEL_7_1 AS (
  SELECT b.IDNO,
         TO_MG_DATE(d.BIRTHDAY) BIRTHDAY,
         a.COURSE_NUM,
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
                    ON a.TERM_YEAR = b.TERM_YEAR AND a.TERM_SEM = b.TERM_SEM AND a.REGISTER_NUMBER = b.REG_NO
         INNER JOIN HS014357.SUBJECT c ON a.COURSE_NUM = c.SBJ_NUM
         INNER JOIN HS014357.STU_BASIS d ON b.RGNO = d.RGNO
  WHERE a.STATUS = '5'
);



CREATE OR REPLACE VIEW EDU_EXCEL_7_2 AS (
  SELECT b.IDNO,
         TO_MG_DATE(d.BIRTHDAY) BIRTHDAY,
         e.TERM_YEAR,
         e.TERM_SEM,
         a.COURSE_NUM,
         'X'                    GRADE,
         c.RCREDIT,
         ''                     RECORD_LINK,
         a.CONTENT,
         ''                     VIDEO_LINK,
         a.ID                   CRID
  FROM STUDENT_COURSE_RECORD a
         INNER JOIN STU_RGNO_MAPPING b
                    ON a.TERM_YEAR = b.TERM_YEAR AND a.TERM_SEM = b.TERM_SEM AND a.REGISTER_NUMBER = b.REG_NO
         INNER JOIN HS014357.SUBJECT c ON a.COURSE_NUM = c.SBJ_NUM
         INNER JOIN HS014357.STU_BASIS d ON b.RGNO = d.RGNO
         INNER JOIN EDU_EXCEL_3_2 e ON b.IDNO = e.IDNO AND c.SBJ_NUM = e.SBJ_NUM
  WHERE a.STATUS = '5'
);



CREATE OR REPLACE VIEW EDU_EXCEL_8_1 AS (
  SELECT b.IDNO,
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
                    ON a.TERM_YEAR = b.TERM_YEAR AND a.TERM_SEM = b.TERM_SEM AND a.REGISTER_NUMBER = b.REG_NO
         INNER JOIN HS014357.SUBJECT c ON a.COURSE_NUM = c.SBJ_NUM
         INNER JOIN HS014357.STU_BASIS d ON b.RGNO = d.RGNO
         INNER JOIN EDU_EXCEL_4_1 e ON b.IDNO = e.IDNO AND c.SBJ_NUM = e.NEW_SBJ_NUM
  WHERE a.STATUS = '5'
);


CREATE OR REPLACE VIEW EDU_EXCEL_8_2 AS (
  SELECT b.IDNO,
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
                    ON a.TERM_YEAR = b.TERM_YEAR AND a.TERM_SEM = b.TERM_SEM AND a.REGISTER_NUMBER = b.REG_NO
         INNER JOIN HS014357.SUBJECT c ON a.COURSE_NUM = c.SBJ_NUM
         INNER JOIN HS014357.STU_BASIS d ON b.RGNO = d.RGNO
         INNER JOIN EDU_EXCEL_4_2 e ON b.IDNO = e.IDNO AND c.SBJ_NUM = e.SBJ_NUM
  WHERE a.STATUS = '5'
);


