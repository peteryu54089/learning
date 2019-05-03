CREATE OR REPLACE VIEW STU_RGNO_MAPPING AS (
  SELECT R.SBJ_YEAR TERM_YEAR,
         R.SBJ_SEM  TERM_SEM,
         SB.REG_NO,
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
