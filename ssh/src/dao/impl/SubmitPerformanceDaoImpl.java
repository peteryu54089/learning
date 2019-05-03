package dao.impl;

import dao.SubmitPerformanceDao;
import dbconn.DbConn;
import model.HasSchoolSchema;
import model.PerformanceRecordSubmitRecord;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public class SubmitPerformanceDaoImpl extends BaseDao implements SubmitPerformanceDao {
    @Deprecated
    private final java.util.Date now;

    public SubmitPerformanceDaoImpl(HasSchoolSchema hasSchoolSchemaObject) {
        super(hasSchoolSchemaObject);
        now = new java.util.Date();
    }


    @Override
    public Report getReportByYearAndSem() {
            BiFunction<Integer, Integer, List<PerformanceRecordSubmitRecord>> getResult = (check, status) -> {
                List<PerformanceRecordSubmitRecord> ret = new ArrayList<>();
                String sql = getBaseSqlWithStatus(check, status) + "\n" +
                        "SELECT SELECTEDYEAR,\n" +
                        "       SUM(CAD_SCHOOL) CAD_SCHOOL,\n" +
                        "       SUM(CAD_SELF) CAD_SELF,\n" +
                        "       SUM(COM) COM,\n" +
                        "       SUM(LIC) LIC,\n" +
                        "       SUM(VOL) VOL,\n" +
                        "       SUM(OTH) OTH\n" +
                        "FROM (\n" +
                        "       SELECT SELECTEDYEAR,\n" +
                        "              RGNO,\n" +
                        "              COALESCE(MAX(DECODE(COL_NO, 1, CNT)), 0) CAD_SCHOOL,\n" +
                        "              COALESCE(MAX(DECODE(COL_NO, 2, CNT)), 0) CAD_SELF,\n" +
                        "              COALESCE(MAX(DECODE(COL_NO, 3, CNT)), 0) COM,\n" +
                        "              COALESCE(MAX(DECODE(COL_NO, 4, CNT)), 0) LIC,\n" +
                        "              COALESCE(MAX(DECODE(COL_NO, 5, CNT)), 0) VOL,\n" +
                        "              COALESCE(MAX(DECODE(COL_NO, 6, CNT)), 0) OTH\n" +
                        "       FROM ALL\n" +
                        "       GROUP BY SELECTEDYEAR, RGNO\n" +
                        "     ) x GROUP BY SELECTEDYEAR";
                try (Connection con = DbConn.getConnection(this)) {
                    try (PreparedStatement ps = con.prepareStatement(sql)) {
                        try (ResultSet rs = ps.executeQuery()) {
                            while (rs.next()) {
                                PerformanceRecordSubmitRecord obj = new PerformanceRecordSubmitRecord(
                                        rs.getInt("SELECTEDYEAR"),
                                        now,
                                        rs.getInt("CAD_SCHOOL"),
                                        rs.getInt("CAD_SELF"),
                                        rs.getInt("COM"),
                                        rs.getInt("LIC"),
                                        rs.getInt("VOL"),
                                        rs.getInt("OTH")
                                );
                                ret.add(obj);
                            }
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return ret;
            };

            return new Report(getResult.apply(0, 1), getResult.apply(1, 1), getResult.apply(1, 2));
    }

    @Override
    public Report getReportByYearAndSem(final Integer rgno) {

        BiFunction<Integer, Integer, List<PerformanceRecordSubmitRecord>> getResult = (check, status) -> {
            List<PerformanceRecordSubmitRecord> ret = new ArrayList<>();
            String sql = getBaseSqlWithStatus(check, status) + "\n" +
                    "SELECT SELECTEDYEAR,\n" +
                    "       RGNO,\n" +
                    "       COALESCE(MAX(DECODE(COL_NO, 1, CNT)), 0) CAD_SCHOOL,\n" +
                    "       COALESCE(MAX(DECODE(COL_NO, 2, CNT)), 0) CAD_SELF,\n" +
                    "       COALESCE(MAX(DECODE(COL_NO, 3, CNT)), 0) COM,\n" +
                    "       COALESCE(MAX(DECODE(COL_NO, 4, CNT)), 0) LIC,\n" +
                    "       COALESCE(MAX(DECODE(COL_NO, 5, CNT)), 0) VOL,\n" +
                    "       COALESCE(MAX(DECODE(COL_NO, 6, CNT)), 0) OTH\n" +
                    "FROM ALL\n" +
                    "WHERE RGNO = ? " +
                    "GROUP BY SELECTEDYEAR, RGNO";
            try (Connection con = DbConn.getConnection(this)) {
                try (PreparedStatement ps = con.prepareStatement(sql)) {
                    int i = 0;
                    ps.setInt(++i, rgno);
                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            PerformanceRecordSubmitRecord obj = new PerformanceRecordSubmitRecord(
                                    rs.getInt("SELECTEDYEAR"),
                                    now,
                                    rs.getString("RGNO"),
                                    rs.getInt("CAD_SCHOOL"),
                                    rs.getInt("CAD_SELF"),
                                    rs.getInt("COM"),
                                    rs.getInt("LIC"),
                                    rs.getInt("VOL"),
                                    rs.getInt("OTH")
                            );
                            ret.add(obj);
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return ret;
        };

        return new Report(getResult.apply(0, 1), getResult.apply(1, 1), getResult.apply(1, 2));
    }


    @Override
    public List<Object> getDetails(Integer year, Integer rgno, Integer status, String date) {
        List<Object> ret = new ArrayList<>();
        String baseSql = getBaseSqlWithStatus(1, status).replaceAll("COUNT\\(\\*\\) CNT\\n",
                "DOCUMENT_FILE_ID, VIDEO_FILE_ID, EXTERNAL_LINK\n"
        ).replaceAll("GROUP BY SELECTEDYEAR, RGNO", "");
        String sql = baseSql + "\n" +
                "SELECT XX.SELECTEDYEAR,\n" +
                "       XX.COL_NO,\n" +
                "       XX.RGNO,\n" +
                "       HSB.IDNO,\n" +
                "       XY.FILE_NAME DOCUMENT_FILENAME, " +
                "       XZ.FILE_NAME VIDEO_FILENAME, " +
                "       XX.EXTERNAL_LINK \n" +
                "FROM ALL XX " +
                "LEFT JOIN UPLOAD_FILES XY ON XX.DOCUMENT_FILE_ID = XY.ID \n" +
                "LEFT JOIN UPLOAD_FILES XZ ON XX.VIDEO_FILE_ID = XZ.ID \n" +
                "INNER JOIN HS_STU_BASIS HSB ON HSB.RGNO = XX.RGNO \n";

        if (year == null || year == 0) {
            sql += "WHERE (XX.SELECTEDYEAR IS NULL OR XX.SELECTEDYEAR = ?) ";
        } else {
            sql += "WHERE XX.SELECTEDYEAR = ? ";
        }

        if (rgno != null) {
            sql += " AND XX.RGNO = ? ";
        }

        try (Connection con = DbConn.getConnection(this)) {
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                int i = 0;
                ps.setInt(++i, year);
                if (rgno != null) {
                    ps.setInt(++i, rgno);
                }
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        String typeO = "";
                        int col = rs.getInt("COL_NO");
                        switch (col) {
                            case 1:
                                typeO = "幹部(校內)";
                                break;
                            case 2:
                                typeO = "幹部(學生自建)";
                                break;
                            case 3:
                                typeO = "競賽提交";
                                break;
                            case 4:
                                typeO = "證照提交";
                                break;
                            case 5:
                                typeO = "志工提交";
                                break;
                            case 6:
                                typeO = "其他活動";
                                break;
                        }

                        String finalTypeO = typeO;
                        ret.add(new Object() {
                            public final String type = finalTypeO;
                            public final Integer year = rs.getInt("SELECTEDYEAR");
                            public final Integer rgno = rs.getInt("RGNO");
                            public final String idno = rs.getString("IDNO");
                            public final String doc_org_fn = rs.getString("DOCUMENT_FILENAME");
                            public final String video_org_fn = rs.getString("VIDEO_FILENAME");
                            public final String ext_link = rs.getString("EXTERNAL_LINK");
                        });
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ret;
    }

    private static String getBaseSqlWithStatus(int check, int status) {
        return "\n" +
                "WITH A AS\n" +
                "       (\n" +
                "         SELECT 1        COL_NO,\n" +
                "                SELECTEDYEAR,\n" +
                "                RGNO,\n" +
                "                COUNT(*) CNT\n" +
                "         FROM CADRE_RECORD\n" +
                "         WHERE STATUS = '" + status + "'\n" +
                "           AND CHECK = '" + check + "'\n" +
                "           AND SOURCE = '2'\n" +
                "         GROUP BY SELECTEDYEAR, RGNO\n" +
                "       ),\n" +
                "     B AS\n" +
                "       (\n" +
                "         SELECT 2        COL_NO,\n" +
                "                SELECTEDYEAR,\n" +
                "                RGNO,\n" +
                "                COUNT(*) CNT\n" +
                "         FROM CADRE_RECORD\n" +
                "         WHERE STATUS = '" + status + "'\n" +
                "           AND CHECK = '" + check + "'\n" +
                "           AND SOURCE = '1'\n" +
                "         GROUP BY SELECTEDYEAR, RGNO\n" +
                "       ),\n" +
                "     C AS\n" +
                "       (\n" +
                "         SELECT 3        COL_NO,\n" +
                "                SELECTEDYEAR,\n" +
                "                RGNO,\n" +
                "                COUNT(*) CNT\n" +
                "         FROM COMPETITION_RECORD\n" +
                "         WHERE STATUS = '" + status + "'\n" +
                "           AND CHECK = '" + check + "'\n" +
                "         GROUP BY SELECTEDYEAR, RGNO\n" +
                "       ),\n" +
                "     D AS\n" +
                "       (\n" +
                "         SELECT 4        COL_NO,\n" +
                "                SELECTEDYEAR,\n" +
                "                RGNO,\n" +
                "                COUNT(*) CNT\n" +
                "         FROM LICENSE_RECORD\n" +
                "         WHERE STATUS = '" + status + "'\n" +
                "           AND CHECK = '" + check + "'\n" +
                "         GROUP BY SELECTEDYEAR, RGNO\n" +
                "       ),\n" +
                "     E AS\n" +
                "       (\n" +
                "         SELECT 5        COL_NO,\n" +
                "                SELECTEDYEAR,\n" +
                "                RGNO,\n" +
                "                COUNT(*) CNT\n" +
                "         FROM VOLUNTEER_RECORD\n" +
                "         WHERE STATUS = '" + status + "'\n" +
                "           AND CHECK = '" + check + "'\n" +
                "         GROUP BY SELECTEDYEAR, RGNO\n" +
                "       ),\n" +
                "     F AS\n" +
                "       (\n" +
                "         SELECT 6        COL_NO,\n" +
                "                SELECTEDYEAR,\n" +
                "                RGNO,\n" +
                "                COUNT(*) CNT\n" +
                "         FROM OTHER_RECORD\n" +
                "         WHERE STATUS = '" + status + "'\n" +
                "           AND CHECK = '" + check + "'\n" +
                "         GROUP BY SELECTEDYEAR, RGNO\n" +
                "       ),\n" +
                "     ALL AS\n" +
                "       (\n" +
                "         SELECT * FROM A UNION ALL\n" +
                "         SELECT * FROM B UNION ALL\n" +
                "         SELECT * FROM C UNION ALL\n" +
                "         SELECT * FROM D UNION ALL\n" +
                "         SELECT * FROM E UNION ALL\n" +
                "         SELECT * FROM F\n" +
                "       )";
    }

}
