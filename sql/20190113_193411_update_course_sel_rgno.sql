CREATE OR REPLACE VIEW STU_SELECT_COURSE AS (
  SELECT C.RGNO,B.* FROM HS_VCOURSE_SELECT AS A
  INNER JOIN COURSE_INFO AS B ON  A.COURSE_NUM = B.COURSE_NUM
  INNER JOIN STU_RGNO_MAPPING C ON A.TERM_YEAR = C.TERM_YEAR AND A.TERM_SEM = C.TERM_SEM AND A.REG_NO = C.REG_NO
);
