CREATE OR REPLACE VIEW
        ACCOUNT
        (
                PK             ,
                REGISTER_NUMBER,
                TERM_YEAR      ,
                TERM_SEMESTER  ,
                PASSWORD       ,
                NAME           ,
                ENAME          ,
                ID_NUMBER      ,
                EMAIL          ,
                MOBILE_TELNO   ,
                ROLE_CODE      ,
                BIRTHDAY       ,
                AVATAR         ,
                SOURCE
        )
        AS
        (
                SELECT
                        STA.STAFF_CODE                                                PK             ,
                        STA.STAFF_CODE                                                REGISTER_NUMBER,
                        Y.YEAR                                                        TERM_YEAR      ,
                        Y.SEMESTER                                                    TERM_SEMESTER  ,
                        '(empty)'                                                     PASSWORD       ,
                        STA.STAFF_CNAME                                               NAME           ,
                        COALESCE(STA.STAFF_ENAME, '')                                 ENAME          ,
                        STA.IDNO                                                      ID_NUMBER      ,
                        COALESCE(EXT_STA_DATA.EMAIL, STA.EMAIL)                       EMAIL          ,
                        COALESCE(EXT_STA_DATA.MOBILE, '')                             MOBILE_TELNO   ,
                        BITOR(COALESCE(RE.ROLE_VALUE, 0), STA_ROLE_INFO.ROLE_CODE)    ROLE_CODE      ,
                        NULL                                                          BIRTHDAY       ,
                        EXT_STA_DATA.AVATAR                                           AVATAR         ,
                        'STAFF'                                                    AS SOURCE
                FROM
                        HS_STAFF STA
                INNER JOIN
                        (
                                SELECT
                                        STAFF_CODE,
                                        SUM(ROLE_CODE) ROLE_CODE
                                FROM
                                        (
                                                SELECT
                                                        STA.STAFF_CODE,
                                                        32 ROLE_CODE
                                                FROM
                                                        HS_STAFF STA
                                                WHERE
                                                        STA.STAFF_CODE IN
                                                        (
                                                                SELECT DISTINCT
                                                                        STAFF_CODE
                                                                FROM
                                                                        HS_VCOURSE_TEACHING)
                                                
                                                UNION ALL
                                                
                                                SELECT
                                                        STA.STAFF_CODE,
                                                        16 ROLE_CODE
                                                FROM
                                                        HS_STAFF STA
                                                WHERE
                                                        STA.STAFF_CODE IN
                                                        (
                                                                SELECT DISTINCT
                                                                        STAFF_CODE
                                                                FROM
                                                                        HS_STU_DOCENT)
                                                
                                                UNION ALL
                                                
                                                SELECT
                                                        STA.STAFF_CODE,
                                                        0 ROLE_CODE
                                                FROM
                                                        HS_STAFF STA ) STA_WITH_ROLE
                                GROUP BY
                                        STAFF_CODE ) AS STA_ROLE_INFO
                ON
                        STA.STAFF_CODE = STA_ROLE_INFO.STAFF_CODE
                LEFT JOIN
                        EXTRA_STAFF_DATA EXT_STA_DATA
                ON
                        EXT_STA_DATA.STAFF_CODE = STA.STAFF_CODE
                CROSS JOIN
                        TABLE(COMMON.CURRENT_SEMESTER()) Y
                LEFT JOIN
                        ROLE_EXTEND RE
                ON
                        RE.SOURCE = 'STAFF'
                AND     RE.KEY    = STA.STAFF_CODE
                
                UNION ALL
                
                SELECT
                        VARCHAR(STU.RGNO)                                      PK             ,
                        STU.REG_NO                                             REGISTER_NUMBER,
                        INTEGER(REG.SBJ_YEAR)                                  TERM_YEAR      ,
                        INTEGER(REG.SBJ_SEM)                                   TERM_SEMESTER  ,
                        '(empty)'                                              PASSWORD       ,
                        STU.CNAME                                              NAME           ,
                        STU.ENAME                                              ENAME          ,
                        STU.IDNO                                               ID_NUMBER      ,
                        COALESCE(EXT_STU_DATA.EMAIL, STU.EMAIL, '')            EMAIL          ,
                        COALESCE(EXT_STU_DATA.MOBILE, STU.MOBILE_TELNO, '')    MOBILE_TELNO   ,
                        BITOR(COALESCE(RE.ROLE_VALUE, 0), 8)                   ROLE_CODE      ,
                        STU.BIRTHDAY                                           BIRTHDAY       ,
                        EXT_STU_DATA.AVATAR                                    AVATAR         ,
                        'STUDENT'                                           AS SOURCE
                FROM
                        HS_STU_BASIS STU
                INNER JOIN
                        HS_STU_REGISTER REG
                ON
                        REG.RGNO = STU.RGNO
                LEFT JOIN
                        EXTRA_STU_DATA EXT_STU_DATA
                ON
                        STU.RGNO = EXT_STU_DATA.RGNO
                LEFT JOIN
                        ROLE_EXTEND RE
                ON
                        RE.SOURCE = 'STUDENT'
                AND     RE.KEY    = VARCHAR(STU.RGNO)
                WHERE
                        REG.REG_FLAG = '1'
                
                UNION ALL
                
                SELECT
                        VARCHAR(EXT_ACCOUNT.ID) PK                         ,
                        EXT_ACCOUNT.REGISTER_NUMBER                        ,
                        EXT_ACCOUNT.TERM_YEAR                              ,
                        EXT_ACCOUNT.TERM_SEMESTER                          ,
                        EXT_ACCOUNT.PASSWORD                               ,
                        EXT_ACCOUNT.NAME                                   ,
                        EXT_ACCOUNT.ENAME                                  ,
                        EXT_ACCOUNT.ID_NUMBER                              ,
                        COALESCE(EXT_ACCOUNT.EMAIL, '')           EMAIL       ,
                        COALESCE(EXT_ACCOUNT.MOBILE_TELNO, '')    MOBILE_TELNO,
                        BITOR(COALESCE(RE.ROLE_VALUE, 0), 0)      ROLE_CODE   ,
                        EXT_ACCOUNT.BIRTHDAY                      BIRTHDAY    ,
                        EXT_ACCOUNT.AVATAR                        AVATAR      ,
                        'EXTRA'                                AS SOURCE
                FROM
                        EXTRA_ACCOUNT EXT_ACCOUNT
                LEFT JOIN
                        ROLE_EXTEND RE
                ON
                        RE.SOURCE = 'EXTRA'
                AND     RE.KEY    = EXT_ACCOUNT.REGISTER_NUMBER
        );
		
CREATE OR REPLACE VIEW
        EDU_EXCEL_3_1 AS
        (
                SELECT
                        c.IDNO                                                          ,
                        COMMON.TO_MG_DATE(c.BIRTHDAY) BIRTHDAY                          ,
                        d.CURR_CODE                                                     ,
                        f.GRADE                                                         ,
                        d.RCREDIT                                                       ,
                        a.SEM_SCORE                                                     ,
                        CASE WHEN a.SEM_STAR_SIGN IS NULL THEN 1 ELSE 0 END  PASS        ,
                        COALESCE(MAKEUP_SCORE, -1)                           RETEST_SCORE,
                        CASE WHEN MAKEUP_STAR_SIGN IS NULL THEN 1 ELSE 0 END RETEST_PASS ,
                        -1                                                   COUNT_IN    ,
                        '??'                                                 DESCRIPTION ,
                        e.SBJ_YEAR                                           TERM_YEAR   ,
                        e.SBJ_SEM                                            TERM_SEM
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
                AND     e.SBJ_YEAR = f.SBJ_YEAR
                AND     e.SBJ_SEM  = f.SBJ_SEM
                WHERE
                        b.RETAKE_FLAG IS NULL
        );

CREATE OR REPLACE VIEW
        EDU_EXCEL_7_1 AS
        (
                SELECT
                        b.IDNO                                ,
                        COMMON.TO_MG_DATE(d.BIRTHDAY) BIRTHDAY,
                        c.CURR_CODE                           ,
                        'X' GRADE                             ,
                        c.RCREDIT                             ,
                        '' RECORD_LINK                        ,
                        a.CONTENT                             ,
                        ''   VIDEO_LINK                         ,
                        a.ID CRID                               ,
                        a.TERM_YEAR                             ,
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
                WHERE
                        a.STATUS = '5'
        );

CREATE OR REPLACE VIEW
        EDU_EXCEL_2 AS
        (
                SELECT
                        a.IDNO                                ,
                        COMMON.TO_MG_DATE(a.BIRTHDAY) BIRTHDAY,
                        b.DIV_CALIAS                          ,
                        d.GRADE                               ,
                        d.CLS_CNAME                           ,
                        c.CLS_NO                              ,
                        'XX'       TYPE                             ,
                        c.SBJ_YEAR TERM_YEAR                        ,
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
                AND     c.SBJ_YEAR = d.SBJ_YEAR
                AND     c.SBJ_SEM  = d.SBJ_SEM
        );

CREATE OR REPLACE VIEW
        EDU_EXCEL_3_2 AS
        (
                SELECT
                        c.IDNO                                                         ,
                        COMMON.TO_MG_DATE(c.BIRTHDAY) BIRTHDAY                         ,
                        e.SBJ_YEAR                    TERM_YEAR                        ,
                        e.SBJ_SEM                     TERM_SEM                         ,
                        d.CURR_CODE                                                    ,
                        f.GRADE                                                        ,
                        d.RCREDIT                                                      ,
                        a.SEM_SCORE                                                    ,
                        CASE WHEN a.SEM_STAR_SIGN IS NULL THEN 1 ELSE 0 END  RETAKE_PASS,
                        COALESCE(MAKEUP_SCORE, -1)                           RETEST_SEM ,
                        CASE WHEN MAKEUP_STAR_SIGN IS NULL THEN 1 ELSE 0 END RETEST_PASS,
                        -1                                                   COUNT_IN   ,
                        '??'                                                 DESCRIPTION,
                        d.SBJ_NUM
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
                AND     e.SBJ_YEAR = f.SBJ_YEAR
                AND     e.SBJ_SEM  = f.SBJ_SEM
                WHERE
                        b.RETAKE_FLAG = 1
        );

CREATE OR REPLACE VIEW
        EDU_EXCEL_6 AS
        (
                SELECT
                        b.IDNO                                                                                                                                                                                                                                                                                          ,
                        COMMON.TO_MG_DATE(b.BIRTHDAY)                                                                                                                                                                                                                                                      BIRTHDAY     ,
                        CASE a.AR_CNAME WHEN '公假' THEN '01' WHEN '事假' THEN '02' WHEN '病假' THEN '03' WHEN '婚假' THEN '04' WHEN '產前假' THEN '05' WHEN '娩假' THEN '06' WHEN '陪產假' THEN '07' WHEN '流產假' THEN '08' WHEN '育嬰假' THEN '09' WHEN '生理假' THEN '10' WHEN '喪假' THEN '11' WHEN '曠課' THEN '12' ELSE '??' END TYPE         ,
                        COMMON.TO_MG_DATE(a.ABS_DATE)                                                                                                                                                                                                                                                      ABS_MG_DATE_S,
                        COMMON.TO_MG_DATE(a.ABS_DATE)                                                                                                                                                                                                                                                      ABS_MG_DATE_E,
                        COUNT(*)                                                                                                                                                                                                                                                                           SECTION_COUNT,
                        a.SBJ_YEAR                                                                                                                                                                                                                                                                         TERM_YEAR    ,
                        a.SBJ_SEM                                                                                                                                                                                                                                                                          TERM_SEM
                FROM
                        STU_ABSENCE a
                INNER JOIN
                        HS_STU_BASIS b
                ON
                        a.RGNO = b.RGNO
                WHERE
                        a.SPRD_NAME NOT IN ('早自習',
                                            '午休' ,
                                            '第八節')
                GROUP BY
                        b.IDNO    ,
                        b.BIRTHDAY,
                        a.ABS_DATE,
                        a.AR_CNAME,
                        a.SBJ_YEAR,
                        a.SBJ_SEM
        );

CREATE OR REPLACE VIEW
        EDU_EXCEL_7_2 AS
        (
                SELECT
                        b.IDNO                                ,
                        COMMON.TO_MG_DATE(d.BIRTHDAY) BIRTHDAY,
                        e.TERM_YEAR                           ,
                        e.TERM_SEM                            ,
                        c.CURR_CODE                           ,
                        'X' GRADE                             ,
                        c.RCREDIT                             ,
                        '' RECORD_LINK                        ,
                        a.CONTENT                             ,
                        ''   VIDEO_LINK                         ,
                        a.ID CRID
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
                        EDU_EXCEL_3_2 e
                ON
                        b.IDNO    = e.IDNO
                AND     c.SBJ_NUM = e.SBJ_NUM
                WHERE
                        a.STATUS = '5'
        );