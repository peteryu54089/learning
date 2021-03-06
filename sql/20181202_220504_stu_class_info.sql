﻿CREATE OR REPLACE VIEW STU_CLASS_INFO AS (
  SELECT
    R.SBJ_YEAR TERM_YEAR,
    R.SBJ_SEM  TERM_SEM,
    B.RGNO     PK,
    B.REG_NO,
    B.IDNO,
    SC.CLS_CODE,
    SC.CLS_CNAME,
    R.CLS_NO,
    SC.GRADE
  FROM HS014357.STU_BASIS B
         INNER JOIN HS014357.STU_REGISTER R ON B.RGNO = R.RGNO
         INNER JOIN HS014357.STU_CLASS SC
                    ON R.SBJ_YEAR = SC.SBJ_YEAR and R.SBJ_SEM = SC.SBJ_SEM and R.CLS_CODE = SC.CLS_CODE
);

CREATE OR REPLACE VIEW STU_CLASS_INFO_WITH_TUTOR AS (
  SELECT
    SC.*,
    TUTOR.STAFF_CODE
  FROM STU_CLASS_INFO SC
         LEFT JOIN HS014357.COU_DOCENT TUTOR
                   ON SC.TERM_YEAR = TUTOR.SBJ_YEAR and SC.TERM_SEM = TUTOR.SBJ_SEM and
                      SC.CLS_CODE = TUTOR.CLS_CODE
);
