CREATE OR REPLACE VIEW
    EDU_EXCEL_4_1_TMP
    (
        REPEAT,
        REENROLL,
        CURR_CODE,
        RGNO,
        IDNO,
        BIRTHDAY,
        TERM_YEAR,
        TERM_SEM,
        SBJ_NUM,
        GRADE,
        RCREDIT,
        SEM_SCORE,
        IS_PASS,
        RE_TEST,
        RE_TEST_PASS,
        COUNT_IN,
        DESCRIPTION
    ) AS
    (
        SELECT
            e.REPEAT,
            e.REENROLL,
            d.CURR_CODE                     CURR_CODE,
            c.RGNO                          RGNO,
            c.IDNO                          IDNO,
            COMMON.TO_MG_DATE(c.BIRTHDAY) BIRTHDAY,
            e.SBJ_YEAR                      TERM_YEAR,
            e.SBJ_SEM                       TERM_SEM,
            d.SBJ_NUM                       SBJ_NUM,
            f.GRADE                         GRADE,
            d.RCREDIT                       RCREDIT,
            a.SEM_SCORE                     SEM_SCORE,
            CASE
                WHEN a.SEM_STAR_SIGN IS NULL
                THEN 1
                ELSE 0
            END                        IS_PASS,
            COALESCE(MAKEUP_SCORE, -1) RE_TEST,
            CASE
                WHEN MAKEUP_STAR_SIGN IS NULL
                THEN 1
                ELSE 0
            END  RE_TEST_PASS,
            -1   COUNT_IN,
            '??' DESCRIPTION
        FROM
            HS_STU_SEM_SCORE a
        INNER JOIN
            HS_CSELECT b
        ON
            a.CSEL_NUM = b.CSEL_NUM
        INNER JOIN
            HS_STU_BASIS c
        ON
            b.RGNO = c.RGNO
        INNER JOIN
            HS_SUBJECT d
        ON
            b.SBJ_NUM = d.SBJ_NUM
        INNER JOIN
            HS_STU_REGISTER e
        ON
            c.RGNO = e.RGNO
        INNER JOIN
            HS_STU_CLASS f
        ON
            e.CLS_CODE = f.CLS_CODE
        AND e.SBJ_YEAR = f.SBJ_YEAR
        AND e.SBJ_SEM = f.SBJ_SEM
    );

CREATE OR REPLACE VIEW
    EDU_EXCEL_8_1
    (
        IDNO,
        BIRTHDAY,
        O_YEAR,
        O_SEM,
        SBJ_NUM,
        GRADE,
        RCREDIT,
        METHOD,
        RECORD_LINK,
        CONTENT,
        VIDEO_LINK,
        CRID,
        TERM_YEAR,
        TERM_SEM
    ) AS
    (
        SELECT
            d.IDNO,
            COMMON.TO_MG_DATE(d.BIRTHDAY) BIRTHDAY,
            e.O_YEAR,
            e.O_SEM,
            e.SBJ_NUM,
            'X' GRADE,
            c.RCREDIT,
            e.METHOD,
            '' RECORD_LINK,
            a.CONTENT,
            ''   VIDEO_LINK,
            a.ID CRID,
            a.TERM_YEAR,
            a.TERM_SEM
        FROM
            STUDENT_COURSE_RECORD a
        INNER JOIN
            STU_RGNO_MAPPING b
        ON
            a.TERM_YEAR = b.TERM_YEAR
        AND a.TERM_SEM = b.TERM_SEM
        AND a.RGNO = b.RGNO
        INNER JOIN
            HS_SUBJECT c
        ON
            a.COURSE_NUM = c.SBJ_NUM
        INNER JOIN
            HS_STU_BASIS d
        ON
            b.RGNO = d.RGNO
        INNER JOIN
            EDU_EXCEL_4_1 e
        ON
            b.IDNO = e.IDNO
        AND c.SBJ_NUM = e.SBJ_NUM
        WHERE
            a.STATUS = '5'
    );
    
CREATE OR REPLACE VIEW
    EDU_EXCEL_8_2
    (
        IDNO,
        BIRTHDAY,
        O_YEAR,
        O_SEM,
        SBJ_NUM,
        GRADE,
        RCREDIT,
        RECORD_LINK,
        CONTENT,
        VIDEO_LINK,
        CRID,
        TERM_YEAR,
        TERM_SEM
    ) AS
    (
        SELECT
            d.IDNO,
            COMMON.TO_MG_DATE(d.BIRTHDAY) BIRTHDAY,
            e.O_YEAR,
            e.O_SEM,
            e.SBJ_NUM,
            'X' GRADE,
            c.RCREDIT,
            '' RECORD_LINK,
            a.CONTENT,
            ''   VIDEO_LINK,
            a.ID CRID,
            a.TERM_YEAR,
            a.TERM_SEM
        FROM
            STUDENT_COURSE_RECORD a
        INNER JOIN
            STU_RGNO_MAPPING b
        ON
            a.RGNO = b.RGNO
        INNER JOIN
            HS_SUBJECT c
        ON
            a.COURSE_NUM = c.SBJ_NUM
        INNER JOIN
            HS_STU_BASIS d
        ON
            b.RGNO = d.RGNO
        INNER JOIN
            EDU_EXCEL_4_2 e
        ON
            b.IDNO = e.IDNO
        AND c.SBJ_NUM = e.SBJ_NUM
        WHERE
            a.STATUS = '5'
    );

CREATE OR REPLACE VIEW
    EDU_EXCEL_2
    (
        IDNO,
        BIRTHDAY,
        DIV_CALIAS,
        GRADE,
        CLS_CNAME,
        CLS_NO,
        TYPE,
        TERM_YEAR,
        TERM_SEM
    ) AS
    (
        SELECT
            a.IDNO,
            COMMON.TO_MG_DATE(a.BIRTHDAY) BIRTHDAY,
            b.DIV_CALIAS,
            d.GRADE,
            d.CLS_CNAME,
            c.CLS_NO,
            'XX'       TYPE,
            c.SBJ_YEAR TERM_YEAR,
            c.SBJ_SEM  TERM_SEM
        FROM
            HS_STU_BASIS a
        LEFT JOIN
            HS_DIVISION b
        ON
            a.DIV_CODE = b.DIV_CODE
        INNER JOIN
            HS_STU_REGISTER c
        ON
            a.RGNO = c.RGNO
        INNER JOIN
            HS_STU_CLASS d
        ON
            c.CLS_CODE = d.CLS_CODE
        AND c.SBJ_YEAR = d.SBJ_YEAR
        AND c.SBJ_SEM = d.SBJ_SEM
    );

