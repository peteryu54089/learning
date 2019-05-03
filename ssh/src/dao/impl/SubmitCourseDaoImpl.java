package dao.impl;

import dao.SubmitCourseDao;
import dao.SubmitPerformanceDao;
import dbconn.DbConn;
import model.*;
import util.DateUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public class SubmitCourseDaoImpl extends BaseDao implements SubmitCourseDao {
    public SubmitCourseDaoImpl(HasSchoolSchema hasSchoolSchemaObject) {
        super(hasSchoolSchemaObject);
    }

    @Override
    public Report getReportByYearAndSem() {
            List<CourseRecordSubmitRecord> ret1 = new ArrayList<>();
            List<CourseRecordSubmitRecord> ret2 = new ArrayList<>();
            List<CourseRecordSubmitRecord> ret3 = new ArrayList<>();
            List<CourseRecordSubmitRecord> ret4 = new ArrayList<>();
            List<CourseRecordSubmitRecord> ret5 = new ArrayList<>();

            //ret1 未驗證
            String sql = "\n" +
                    "  SELECT A.TERM_YEAR,\n" +
                    "    A.TERM_SEM,\n" +
                    "    COUNT(*) CNT\n" +
                    "  FROM STUDENT_COURSE_RECORD A\n" +
                    "    LEFT JOIN STU_RGNO_MAPPING B\n" +
                    "      ON A.TERM_YEAR = B.TERM_YEAR AND A.TERM_SEM = B.TERM_SEM AND A.RGNO = B.RGNO\n" +
                    "  WHERE A.STATUS = '2'\n" +
                    "  GROUP BY A.TERM_YEAR, A.TERM_SEM\n";
            try (Connection con = DbConn.getConnection(this)) {
                try (PreparedStatement ps = con.prepareStatement(sql)) {
                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            CourseRecordSubmitRecord obj = new CourseRecordSubmitRecord(
                                    rs.getInt("TERM_YEAR"),
                                    rs.getInt("TERM_SEM"),
                                    rs.getInt("CNT")
                            );
                            ret1.add(obj);
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            //ret2 驗證成功未勾選
        sql = "\n" +
                "  SELECT A.TERM_YEAR,\n" +
                "    A.TERM_SEM,\n" +
                "    COUNT(*) CNT\n" +
                "  FROM STUDENT_COURSE_RECORD A\n" +
                "    LEFT JOIN STU_RGNO_MAPPING B\n" +
                "      ON A.TERM_YEAR = B.TERM_YEAR AND A.TERM_SEM = B.TERM_SEM AND A.RGNO = B.RGNO\n" +
                "  WHERE A.STATUS = '3'\n" +
                "  GROUP BY A.TERM_YEAR, A.TERM_SEM\n";
        try (Connection con = DbConn.getConnection(this)) {
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        CourseRecordSubmitRecord obj = new CourseRecordSubmitRecord(
                                rs.getInt("TERM_YEAR"),
                                rs.getInt("TERM_SEM"),
                                rs.getInt("CNT")
                        );
                        ret2.add(obj);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //ret3 已勾選
        sql = "\n" +
                "  SELECT A.TERM_YEAR,\n" +
                "    A.TERM_SEM,\n" +
                "    COUNT(*) CNT\n" +
                "  FROM STUDENT_COURSE_RECORD A\n" +
                "    LEFT JOIN STU_RGNO_MAPPING B\n" +
                "      ON A.TERM_YEAR = B.TERM_YEAR AND A.TERM_SEM = B.TERM_SEM AND A.RGNO = B.RGNO\n" +
                "  WHERE A.STATUS = '5'\n" +
                "  GROUP BY A.TERM_YEAR, A.TERM_SEM\n";
        try (Connection con = DbConn.getConnection(this)) {
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        CourseRecordSubmitRecord obj = new CourseRecordSubmitRecord(
                                rs.getInt("TERM_YEAR"),
                                rs.getInt("TERM_SEM"),
                                rs.getInt("CNT")
                        );
                        ret3.add(obj);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //ret4 已報送(上傳全國)
        sql = "\n" +
                "  SELECT A.TERM_YEAR,\n" +
                "    A.TERM_SEM,\n" +
                "    COUNT(*) CNT\n" +
                "  FROM STUDENT_COURSE_RECORD A\n" +
                "    LEFT JOIN STU_RGNO_MAPPING B\n" +
                "      ON A.TERM_YEAR = B.TERM_YEAR AND A.TERM_SEM = B.TERM_SEM AND A.RGNO = B.RGNO\n" +
                "  WHERE A.STATUS = '6'\n" +
                "  GROUP BY A.TERM_YEAR, A.TERM_SEM\n";
        try (Connection con = DbConn.getConnection(this)) {
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        CourseRecordSubmitRecord obj = new CourseRecordSubmitRecord(
                                rs.getInt("TERM_YEAR"),
                                rs.getInt("TERM_SEM"),
                                rs.getInt("CNT")
                        );
                        ret4.add(obj);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //ret5 認證失敗
        sql = "\n" +
                "  SELECT A.TERM_YEAR,\n" +
                "    A.TERM_SEM,\n" +
                "    COUNT(*) CNT\n" +
                "  FROM STUDENT_COURSE_RECORD A\n" +
                "    LEFT JOIN STU_RGNO_MAPPING B\n" +
                "      ON A.TERM_YEAR = B.TERM_YEAR AND A.TERM_SEM = B.TERM_SEM AND A.RGNO = B.RGNO\n" +
                "  WHERE A.STATUS = '4'\n" +
                "  GROUP BY A.TERM_YEAR, A.TERM_SEM\n";
        try (Connection con = DbConn.getConnection(this)) {
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        CourseRecordSubmitRecord obj = new CourseRecordSubmitRecord(
                                rs.getInt("TERM_YEAR"),
                                rs.getInt("TERM_SEM"),
                                rs.getInt("CNT")
                        );
                        ret5.add(obj);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Report(ret1,ret2,ret3,ret4,ret5);
    }

    @Override
    public Report getReportByYearAndSem(Integer rgno) {
        List<CourseRecordSubmitRecord> ret1 = new ArrayList<>();
        List<CourseRecordSubmitRecord> ret2 = new ArrayList<>();
        List<CourseRecordSubmitRecord> ret3 = new ArrayList<>();
        List<CourseRecordSubmitRecord> ret4 = new ArrayList<>();
        List<CourseRecordSubmitRecord> ret5 = new ArrayList<>();

        //ret1 未驗證
        String sql = "\n" +
                "  SELECT A.TERM_YEAR,\n" +
                "    A.TERM_SEM,\n" +
                "    COUNT(*) CNT\n" +
                "  FROM STUDENT_COURSE_RECORD A\n" +
                "    LEFT JOIN STU_RGNO_MAPPING B\n" +
                "      ON A.TERM_YEAR = B.TERM_YEAR AND A.TERM_SEM = B.TERM_SEM AND A.RGNO = B.RGNO\n" +
                "  WHERE A.STATUS = '2' AND A.RGNO =" +rgno +"\n" +
                "  GROUP BY A.TERM_YEAR, A.TERM_SEM\n";
        try (Connection con = DbConn.getConnection(this)) {
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        CourseRecordSubmitRecord obj = new CourseRecordSubmitRecord(
                                rs.getInt("TERM_YEAR"),
                                rs.getInt("TERM_SEM"),
                                rs.getInt("CNT")
                        );
                        ret1.add(obj);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //ret2 驗證成功未勾選
        sql = "\n" +
                "  SELECT A.TERM_YEAR,\n" +
                "    A.TERM_SEM,\n" +
                "    COUNT(*) CNT\n" +
                "  FROM STUDENT_COURSE_RECORD A\n" +
                "    LEFT JOIN STU_RGNO_MAPPING B\n" +
                "      ON A.TERM_YEAR = B.TERM_YEAR AND A.TERM_SEM = B.TERM_SEM AND A.RGNO = B.RGNO\n" +
                "  WHERE A.STATUS = '3' AND A.RGNO = "+rgno+" \n" +
                "  GROUP BY A.TERM_YEAR, A.TERM_SEM\n";
        try (Connection con = DbConn.getConnection(this)) {
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        CourseRecordSubmitRecord obj = new CourseRecordSubmitRecord(
                                rs.getInt("TERM_YEAR"),
                                rs.getInt("TERM_SEM"),
                                rs.getInt("CNT")
                        );
                        ret2.add(obj);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //ret3 已勾選
        sql = "\n" +
                "  SELECT A.TERM_YEAR,\n" +
                "    A.TERM_SEM,\n" +
                "    COUNT(*) CNT\n" +
                "  FROM STUDENT_COURSE_RECORD A\n" +
                "    LEFT JOIN STU_RGNO_MAPPING B\n" +
                "      ON A.TERM_YEAR = B.TERM_YEAR AND A.TERM_SEM = B.TERM_SEM AND A.RGNO = B.RGNO\n" +
                "  WHERE A.STATUS = '5' AND A.RGNO = "+rgno+" \n" +
                "  GROUP BY A.TERM_YEAR, A.TERM_SEM\n";
        try (Connection con = DbConn.getConnection(this)) {
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        CourseRecordSubmitRecord obj = new CourseRecordSubmitRecord(
                                rs.getInt("TERM_YEAR"),
                                rs.getInt("TERM_SEM"),
                                rs.getInt("CNT")
                        );
                        ret3.add(obj);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //ret4 已報送
        sql = "\n" +
                "  SELECT A.TERM_YEAR,\n" +
                "    A.TERM_SEM,\n" +
                "    COUNT(*) CNT\n" +
                "  FROM STUDENT_COURSE_RECORD A\n" +
                "    LEFT JOIN STU_RGNO_MAPPING B\n" +
                "      ON A.TERM_YEAR = B.TERM_YEAR AND A.TERM_SEM = B.TERM_SEM AND A.RGNO = B.RGNO\n" +
                "  WHERE A.STATUS = '6' AND A.RGNO ="+ rgno+"\n" +
                "  GROUP BY A.TERM_YEAR, A.TERM_SEM\n";
        try (Connection con = DbConn.getConnection(this)) {
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        CourseRecordSubmitRecord obj = new CourseRecordSubmitRecord(
                                rs.getInt("TERM_YEAR"),
                                rs.getInt("TERM_SEM"),
                                rs.getInt("CNT")
                        );
                        ret4.add(obj);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //ret5 認證失敗
        sql = "\n" +
                "  SELECT A.TERM_YEAR,\n" +
                "    A.TERM_SEM,\n" +
                "    COUNT(*) CNT\n" +
                "  FROM STUDENT_COURSE_RECORD A\n" +
                "    LEFT JOIN STU_RGNO_MAPPING B\n" +
                "      ON A.TERM_YEAR = B.TERM_YEAR AND A.TERM_SEM = B.TERM_SEM AND A.RGNO = B.RGNO\n" +
                "  WHERE A.STATUS = '4' AND A.RGNO = "+rgno+"\n" +
                "  GROUP BY A.TERM_YEAR, A.TERM_SEM\n";
        try (Connection con = DbConn.getConnection(this)) {
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        CourseRecordSubmitRecord obj = new CourseRecordSubmitRecord(
                                rs.getInt("TERM_YEAR"),
                                rs.getInt("TERM_SEM"),
                                rs.getInt("CNT")
                        );
                        ret5.add(obj);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Report(ret1,ret2,ret3,ret4,ret5);
    }

    public List<Object> getDetails(Integer year, Integer sem, Integer q, Integer rgno) throws SQLException {
       String sql = "";
            sql = "SELECT A.TERM_YEAR,\n" +
                    "  A.TERM_SEM,\n" +
                    "  A.SUBMITTED_AT,\n" +
                    "  A.RGNO,\n" +
                    "  A.COURSE_CNAME,\n" +
                    "  B.IDNO,\n" +
                    "  (\n" +
                    "    SELECT listagg(C.ORIGINAL_FILENAME, ', ') FROM STUDENT_COURSE_RECORD_DOCUMENT C WHERE C.CRID = A.ID\n" +
                    "  ) AS FILES\n" +
                    "FROM STUDENT_COURSE_RECORD A\n" +
                    "  LEFT JOIN STU_RGNO_MAPPING B\n" +
                    "    ON A.TERM_YEAR = B.TERM_YEAR AND A.TERM_SEM = B.TERM_SEM AND A.RGNO = B.RGNO\n" +
                    "WHERE A.STATUS = ? AND A.TERM_YEAR = ? AND A.TERM_SEM = ?";
            if(rgno != null)
            {
                sql+= "AND A.RGNO = ?";
            }

        try (Connection con = DbConn.getConnection(this)) {
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                int i = 0;

                ps.setInt(++i,q);
                ps.setInt(++i, year);
                ps.setInt(++i, sem);

                if (rgno != null) {
                    ps.setInt(++i, rgno);
                }

                List<Object> list = new ArrayList<>();
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        list.add(new Object() {
                            public final String year = rs.getString("TERM_YEAR");
                            public final String sem = rs.getString("TERM_SEM");
                            public final String date = DateUtils.formatDateTime(rs.getDate("SUBMITTED_AT"));
                            public final String idno = rs.getString("IDNO");
                            public final String cn = rs.getString("COURSE_CNAME");
                            public final String files = rs.getString("FILES");
                        });
                    }
                }
                return list;
            }
        }
    }


}
