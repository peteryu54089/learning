package dao.impl;

import dao.SystemConfigDao;
import dbconn.DbConn;
import model.HasSchoolSchema;
import model.SystemConfig;
import model.SystemConfig.KeyPair;
import util.DbUtils;

import java.sql.*;
import java.text.ParseException;
import java.util.*;

public class SystemConfigDaoImpl extends BaseDao implements SystemConfigDao {
    private static final List<List<String>> DEFAULT_DATA_SET;

    static {
        final List<List<String>> dataSet = new ArrayList<>();
        final String DATA_TABLE = "ABSENCE_UPDATE_DATETIME\tDATETIME\t2000-01-01 00:00:00\n" +
                "ABSENCE_UPDATE_INTERVAL\tINTEGER\t1\n" +
                "AUTO_BIO_MAX_SIZE\tINTEGER\t2097152\n" +
                "AUTO_BIO_TYPES\tSTRING\t.pdf, .odf, .jpg, .docx, .doc, .png\n" +
                "COUNSELING_RECORD_MAX_SIZE\tINTEGER\t2097152\n" +
                "COUNSELING_RECORD_TYPES\tSTRING\t.pdf, .odf, .jpg, .docx, .doc, .png\n" +
                "COURSE_STUDY_RECORD_ACTIVE_TERM_YEAR\tINTEGER\t107\n" +
                "COURSE_STUDY_RECORD_ACTIVE_TERM_SEM\tINTEGER\t1\n" +
                "COURSE_STUDY_RECORD_MAIL_INTERVAL\tINTEGER\t3\n" +
                "COURSE_STUDY_RECORD_MAX_AMOUNT_PER_RECORD\tINTEGER\t3\n" +
                "COURSE_STUDY_RECORD_MAX_SUBMIT_AMOUNT\tINTEGER\t3\n" +
                "COURSE_STUDY_RECORD_MAX_SIZE\tINTEGER\t2097152\n" +
                "COURSE_STUDY_RECORD_STUDENT_END_DATETIME\tDATETIME\t2019-01-31 23:59:59\n" +
                "COURSE_STUDY_RECORD_STUDENT_START_DATETIME\tDATETIME\t2018-01-01 00:00:00\n" +
                "COURSE_STUDY_RECORD_TEACHER_END_DATETIME\tDATETIME\t2019-01-31 23:59:00\n" +
                "COURSE_STUDY_RECORD_TEACHER_START_DATETIME\tDATETIME\t2018-01-01 00:00:00\n" +
                "COURSE_STUDY_RECORD_TYPE\tSTRING\t.pdf, .odf, .jpg, .docx, .doc, .png\n" +
                "LIFE_PLAN_MAX_SIZE\tINTEGER\t2097152\n" +
                "LIFE_PLAN_TYPES\tSTRING\t.pdf, .odf, .jpg, .docx, .doc, .png\n" +
                "OTHER_DOCUMENT_MAX_AMOUNT\tINTEGER\t10\n" +
                "OTHER_DOCUMENT_MAX_SIZE\tINTEGER\t2097152\n" +
                "OTHER_DOCUMENT_TYPE\tSTRING\t.pdf, .odf, .jpg, .docx, .doc, .png\n" +
                "PERFORMANCE_SUBMIT_YEAR\tINTEGER\t107\n" +
                "PERFORMANCE_DOCUMENT_MAX_SIZE\tINTEGER\t2097152\n" +
                "PERFORMANCE_DOCUMENT_TYPE\tSTRING\t.pdf, .jpg, .png\n" +
                "PERFORMANCE_MAX_SUBMIT_AMOUNT\tINTEGER\t10\n" +
                "PERFORMANCE_STUDENT_END_DATETIME\tDATETIME\t2019-02-02 23:59:59\n" +
                "PERFORMANCE_STUDENT_START_DATETIME\tDATETIME\t2018-07-03 00:00:00\n" +
                "PERFORMANCE_VIDEO_MAX_SIZE\tINTEGER\t10485760\n" +
                "PERFORMANCE_VIDEO_TYPE\tSTRING\t.mp3, .flv, .mp4\n" +
                "SCHOOL_ID\tSTRING\t014357\n" +
                "SCHOOL_ADDRESS\tSTRING\t10608台北市忠孝東路三段一號\n" +
                "SCHOOL_BANNER_PATH\tSTRING\tLOGO.png\n" +
                "SCHOOL_LOGO_PATH\tSTRING\tTaipeitech.jpg\n" +
                "SCHOOL_NAME\tSTRING\tNTUT 學校\n" +
                "SCHOOL_PHONE\tSTRING\t(02) 2771-2171\n" +
                "SCHOOL_WEBSITE\tSTRING\thttps://www.ntut.edu.tw\n" +
                "STUDY_PLAN_MAX_SIZE\tINTEGER\t2097152\n" +
                "STUDY_PLAN_TYPES\tSTRING\t.pdf, .odf, .jpg, .docx, .doc, .png\n" +
                "LAST_SYNCED_STU_CLUB_ID\tINTEGER\t0\n" +
                "LAST_SYNCED_STU_CLUB_TIME\tDATETIME\t0000-00-00 00:00:00\n" +
                "";

        for (final String row : DATA_TABLE.trim().split("\n")) {
            final String[] cells = row.trim().split("\t");
            if (cells.length == 3) {
                dataSet.add(Arrays.asList(cells));
            }
        }

        DEFAULT_DATA_SET = Collections.unmodifiableList(dataSet);
    }

    public SystemConfigDaoImpl(HasSchoolSchema hasSchoolSchemaObject) {
        super(hasSchoolSchemaObject);
    }

    @Override
    public SystemConfig getSystemConfig() {
        initializeDefault();
        Map<String, KeyPair> configs = new HashMap<>();
        String sql = "SELECT * FROM CONFIG ORDER BY KEY";
        try (Connection pcon = DbConn.getConnection(this)) {
            try (PreparedStatement preparedStatement = pcon.prepareStatement(sql)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        String key = resultSet.getString("KEY");
                        String dataType = resultSet.getString(2); // BYPASS DB2 JDBC MAGIC STRING
                        String value = DbUtils.convertClobToString(resultSet.getClob("VALUE"));
                        configs.put(
                                key,
                                new KeyPair(key, dataType, value)
                        );
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try (Connection pcon = DbConn.getConnection(this)) {
            try (PreparedStatement preparedStatement = pcon.prepareStatement(
                    "SELECT A.* FROM COMMON.SCHOOL A INNER JOIN CONFIG B ON A.SCH_CODE = B.VALUE AND B.KEY=?"
            )) {
                preparedStatement.setString(1, "SCHOOL_ID");
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        configs.put("SCHOOL_ADDRESS", new KeyPair("SCHOOL_ADDRESS", resultSet.getString("SCH_ADDRESS")));
                        configs.put("SCHOOL_NAME", new KeyPair("SCHOOL_NAME", resultSet.getString("SCH_CNAME")));
                        configs.put("SCHOOL_PHONE", new KeyPair("SCHOOL_PHONE", resultSet.getString("SCH_TELNO")));
                        configs.put("SCHOOL_WEBSITE", new KeyPair("SCHOOL_WEBSITE", resultSet.getString("SCH_URL")));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            return new SystemConfig(configs);
        } catch (NullPointerException | ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void saveSystemConfig(SystemConfig systemConfig) {
        String sql = "MERGE INTO CONFIG a USING (\n" +
                "    SELECT 1,  \n" +
                "        ? AS KEY,\n" +
                "        ? AS DATA_TYPE,\n" +
                "        ? AS VALUE\n" +
                "    FROM SYSIBM.SYSDUMMY1\n" +
                ") b ON (a.KEY = b.KEY)\n" +
                "WHEN MATCHED THEN\n" +
                "    UPDATE SET\n" +
                "        KEY = b.KEY, DATA_TYPE = b.DATA_TYPE, VALUE = b.VALUE\n" +
                "WHEN NOT MATCHED THEN INSERT (KEY, DATA_TYPE, VALUE) VALUES \n" +
                "    (b.KEY, b.DATA_TYPE, b.VALUE)";

        final List<KeyPair> list = new ArrayList<>();
        final SystemConfig.SchoolInfo schoolInfo = systemConfig.getSchoolInfo();
        final SystemConfig.AutoBio autoBio = systemConfig.getAutoBio();
        final SystemConfig.StudyPlan studyPlan = systemConfig.getStudyPlan();
        final SystemConfig.LifePlan lifePlan = systemConfig.getLifePlan();
        final SystemConfig.CounselingRecord counselingRecord = systemConfig.getCounselingRecord();
        final SystemConfig.CourseStudyRecord courseStudyRecord = systemConfig.getCourseStudyRecord();
        final SystemConfig.OtherDocument otherDocument = systemConfig.getOtherDocument();
        final SystemConfig.Performance performance = systemConfig.getPerformance();
        final SystemConfig.AbsenceUpdateInfo absenceUpdateInfo = systemConfig.getAbsenceUpdateInfo();
        final SystemConfig.SyncStatus syncStatus = systemConfig.getSyncStatus();

        list.add(new KeyPair("AUTO_BIO_MAX_SIZE", autoBio.getMaxSize()));
        list.add(new KeyPair("AUTO_BIO_TYPES", autoBio.getAllowTypesString()));

        list.add(new KeyPair("COUNSELING_RECORD_MAX_SIZE", counselingRecord.getMaxSize()));
        list.add(new KeyPair("COUNSELING_RECORD_TYPES", counselingRecord.getAllowTypesString()));

        list.add(new KeyPair("COURSE_STUDY_RECORD_ACTIVE_TERM_YEAR", courseStudyRecord.getActiveYear()));
        list.add(new KeyPair("COURSE_STUDY_RECORD_ACTIVE_TERM_SEM", courseStudyRecord.getActiveSem()));

        list.add(new KeyPair("COURSE_STUDY_RECORD_MAIL_INTERVAL", courseStudyRecord.getMailInterval()));
        list.add(new KeyPair("COURSE_STUDY_RECORD_MAX_AMOUNT_PER_RECORD", courseStudyRecord.getMaxAmountPerRecord()));
        list.add(new KeyPair("COURSE_STUDY_RECORD_MAX_SUBMIT_AMOUNT", courseStudyRecord.getMaxSubmitAmount()));
        list.add(new KeyPair("COURSE_STUDY_RECORD_MAX_SIZE", courseStudyRecord.getMaxSize()));
        list.add(new KeyPair("COURSE_STUDY_RECORD_STUDENT_START_DATETIME", courseStudyRecord.getStudentStartDateTime()));
        list.add(new KeyPair("COURSE_STUDY_RECORD_STUDENT_END_DATETIME", courseStudyRecord.getStudentEndDateTime()));
        list.add(new KeyPair("COURSE_STUDY_RECORD_TEACHER_START_DATETIME", courseStudyRecord.getTeacherStartDateTime()));
        list.add(new KeyPair("COURSE_STUDY_RECORD_TEACHER_END_DATETIME", courseStudyRecord.getTeacherEndDateTime()));
        list.add(new KeyPair("COURSE_STUDY_RECORD_TYPE", courseStudyRecord.getAllowTypesString()));

        list.add(new KeyPair("LIFE_PLAN_MAX_SIZE", lifePlan.getMaxSize()));
        list.add(new KeyPair("LIFE_PLAN_TYPES", lifePlan.getAllowTypesString()));

        list.add(new KeyPair("OTHER_DOCUMENT_MAX_AMOUNT", otherDocument.getMaxAmount()));
        list.add(new KeyPair("OTHER_DOCUMENT_MAX_SIZE", otherDocument.getMaxSize()));
        list.add(new KeyPair("OTHER_DOCUMENT_TYPE", otherDocument.getAllowTypesString()));

        list.add(new KeyPair("PERFORMANCE_SUBMIT_YEAR", performance.getSubmitYear()));
        list.add(new KeyPair("PERFORMANCE_DOCUMENT_MAX_SIZE", performance.getMaxDocSize()));
        list.add(new KeyPair("PERFORMANCE_DOCUMENT_TYPE", performance.getAllowDocTypesString()));
        list.add(new KeyPair("PERFORMANCE_MAX_SUBMIT_AMOUNT", performance.getMaxSubmitAmount()));
        list.add(new KeyPair("PERFORMANCE_STUDENT_START_DATETIME", performance.getStudentStartDateTime()));
        list.add(new KeyPair("PERFORMANCE_STUDENT_END_DATETIME", performance.getStudentEndDateTime()));
        list.add(new KeyPair("PERFORMANCE_VIDEO_MAX_SIZE", performance.getMaxVideoSize()));
        list.add(new KeyPair("PERFORMANCE_VIDEO_TYPE", performance.getAllowVideoTypesString()));

        list.add(new KeyPair("SCHOOL_ADDRESS", schoolInfo.getAddress()));
        list.add(new KeyPair("SCHOOL_BANNER_PATH", schoolInfo.getBannerPath()));
        list.add(new KeyPair("SCHOOL_LOGO_PATH", schoolInfo.getLogoPath()));
        list.add(new KeyPair("SCHOOL_NAME", schoolInfo.getName()));
        list.add(new KeyPair("SCHOOL_PHONE", schoolInfo.getPhone()));
        list.add(new KeyPair("SCHOOL_WEBSITE", schoolInfo.getWebsite()));

        list.add(new KeyPair("STUDY_PLAN_MAX_SIZE", studyPlan.getMaxSize()));
        list.add(new KeyPair("STUDY_PLAN_TYPES", studyPlan.getAllowTypesString()));

        list.add(new KeyPair("ABSENCE_UPDATE_DATETIME", absenceUpdateInfo.getLastUpdateDate()));
        list.add(new KeyPair("ABSENCE_UPDATE_INTERVAL", absenceUpdateInfo.getUpdateInterval()));

        list.add(new KeyPair("LAST_SYNCED_STU_CLUB_ID", syncStatus.getLastSyncedStuClubId()));
        list.add(new KeyPair("LAST_SYNCED_STU_CLUB_TIME", syncStatus.getLastSyncedStuClubTime()));

        try (Connection pcon = DbConn.getConnection(this)) {
            pcon.setAutoCommit(false);
            for (KeyPair pair : list) {
                try (PreparedStatement preparedStatement = pcon.prepareStatement(sql)) {
                    Clob clob = pcon.createClob();
                    clob.setString(1, pair.getValue());
                    preparedStatement.setString(1, pair.getKey());
                    preparedStatement.setString(2, pair.getDataType());
                    preparedStatement.setClob(3, clob);
                    preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            pcon.commit();
            pcon.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isNightSchool() {
        Set<String> nightLevelCode = new HashSet<>(Arrays.asList("A", "B", "C"));
        String sql = "SELECT A.*, C.LEVEL_CNAME\n" +
                "FROM COMMON.SCHOOL A\n" +
                "       INNER JOIN CONFIG B ON B.KEY = 'SCHOOL_ID'\n" +
                "  AND A.SCH_CODE = B.VALUE\n" +
                "       INNER JOIN\n" +
                "     COMMON.SCH_LEVEL C\n" +
                "     ON\n" +
                "       A.LEVEL_CODE = C.LEVEL_CODE";
        try (Connection pcon = DbConn.getConnection(this)) {
            try (PreparedStatement ps = pcon.prepareStatement(sql)) {
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return nightLevelCode.contains(rs.getString("LEVEL_CODE"));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private void initializeDefault() {
        String sql = "MERGE INTO CONFIG a USING (SELECT 1, ? AS KEY FROM SYSIBM.SYSDUMMY1) b ON (a.KEY = b.KEY)\n" +
                "     WHEN NOT MATCHED THEN INSERT (KEY, DATA_TYPE, VALUE) VALUES \n" +
                "    (b.KEY,  ?, ?)";

        try (Connection pcon = DbConn.getConnection(this)) {
            for (List<String> cells : DEFAULT_DATA_SET) {
                try (PreparedStatement preparedStatement = pcon.prepareStatement(sql)) {
                    Clob clob = pcon.createClob();
                    clob.setString(1, cells.get(2));
                    preparedStatement.setString(1, cells.get(0));
                    preparedStatement.setString(2, cells.get(1));
                    preparedStatement.setClob(3, clob);
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
