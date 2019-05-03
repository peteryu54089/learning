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
         FROM (SELECT E.*,G.STAFF_CNAME
               FROM (SELECT C.*,D.COURSE_CNAME
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
                           HS_VCOURSE AS D
                     WHERE C.SBJ_NUM = D.COURSE_NUM) AS E,
                    HS_TEACHING AS F,
                    HS_STAFF AS G
               WHERE E.SBJ_NUM = F.SBJ_NUM
                 AND F.STAFF_CODE = G.STAFF_CODE) AS H
         GROUP BY H.SBJ_NUM,H.SBJ_YEAR,H.SBJ_SEM,H.RGNO,H.RETAKE_FLAG,H.REGULAR_SCORE,H.PERIOD1_SCORE,H.PERIOD2_SCORE,H.FINAL_SCORE,H.SEM_SCORE,H.SEM_STAR_SIGN,H.MAKEUP_SCORE,H.MAKEUP_STAR_SIGN,H.COURSE_CNAME)
);
CREATE OR REPLACE VIEW stu_select_course AS (
  SELECT A.REG_NO,B.* FROM HS_VCOURSE_SELECT AS A,COURSE_INFO AS B
  WHERE A.COURSE_NUM = B.COURSE_NUM
);
