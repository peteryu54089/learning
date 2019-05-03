package dao.impl;

import dao.CourseRecordUnlockStatusDao;
import dao.PerformanceUnlockStatusDao;
import dao.StudentDao;
import dbconn.DbConn;
import model.Authority;
import model.HasSchoolSchema;
import model.MisSystemData;
import model.role.Role;
import model.role.Student;
import util.MisSystemUtil;
import util.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by David on 2018/1/29.
 */
public class StudentDaoImpl extends BaseDao implements StudentDao {
    public final static Integer RECORD_PER_PAGE = 10;

    public StudentDaoImpl(HasSchoolSchema hasSchoolSchemaObject) {
        super(hasSchoolSchemaObject);
    }

    @Override
    public Student getStudentByIdNumber(String idno) throws SQLException {
        String sql = "SELECT " +
                "SLI.*, " +
                "ES.EMAIL, ES.MOBILE, ES.AVATAR, ES.SOCIAL_ACCOUNT, ES.BIO, ES.INTEREST, ES.NICKNAME, ES.ALT_EMAIL " +
                "FROM STU_CLASS_INFO SLI LEFT JOIN EXTRA_STU_DATA ES ON ES.RGNO = SLI.PK WHERE IDNO = ? ";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Student student = null;
        try (Connection pcon = DbConn.getConnection(this)) {
            preparedStatement = pcon.prepareStatement(sql);
            int i = 0;
            // TODO 抓學期 學年度
//            preparedStatement.setString(1, "105");
//            preparedStatement.setString(2, "2");
            preparedStatement.setString(++i, idno);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                student = convertResultSetToStudent(resultSet);
            }
            return student;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null)
                preparedStatement.close();
            if (resultSet != null)
                resultSet.close();
        }
        return null;
    }

    @Override
    public Student getStudentByRegNumber(String termYear, String termSem, String reg_no) throws SQLException {
        String sql = "SELECT\n" +
                "  S.*, " +
                "  COMMON.TO_MG_DATE(A.BIRTHDAY) BIRTH,\n" +
                "  A.CNAME AS STU_CNAME,\n" +
                "  A.ENAME   STU_ENAME,\n" +
                "  NULL      STAFF_CODE,\n" +
                "  ES.EMAIL, ES.MOBILE, ES.AVATAR, ES.SOCIAL_ACCOUNT, ES.BIO, ES.INTEREST, ES.NICKNAME, ES.ALT_EMAIL, " +
                "  S.PK   AS RGNO\n" +
                "FROM STU_CLASS_INFO S\n" +
                "       INNER JOIN HS_STU_BASIS AS A ON S.PK = A.RGNO\n" +
                "       LEFT JOIN ACCOUNT AS AA ON S.PK = AA.PK AND BITAND(AA.ROLE_CODE, 8) > 0\n" +
                "       LEFT JOIN EXTRA_STU_DATA ES ON ES.RGNO = A.RGNO " +
                "WHERE S.TERM_YEAR = ?\n" +
                "  AND S.TERM_SEM = ?\n" +
                "  AND S.REG_NO = ?";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Student student = null;
        try (Connection pcon = DbConn.getConnection(this)) {
            preparedStatement = pcon.prepareStatement(sql);
            preparedStatement.setString(1, termYear);
            preparedStatement.setString(2, termSem);
            preparedStatement.setString(3, reg_no);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                student = convertResultSetToStudent(resultSet);
            }
            return student;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null)
                preparedStatement.close();
            if (resultSet != null)
                resultSet.close();
        }
        return null;
    }

    @Override
    public Integer getRgnoFromRegNo(String termYear, String termSem, String reg_no) throws SQLException {
        try (Connection pcon = DbConn.getConnection(this)) {
            try (PreparedStatement ps = pcon.prepareStatement(
                    "SELECT * FROM STU_RGNO_MAPPING WHERE TERM_YEAR = ? AND TERM_SEM = ? AND REG_NO = ?"
            )) {
                int i = 0;
                ps.setString(++i, termYear);
                ps.setString(++i, termSem);
                ps.setString(++i, reg_no);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt("RGNO");
                    }
                }
            }
        }

        return null;
    }

    @Override
    public Student getStudentByRegNumber(String reg_no) throws SQLException {
        // TODO 抓學期 學年度
        int year = 0;
        int sem = 0;
        MisSystemData misSystemData = null;
        try {    // 取得學務系統學年期
            misSystemData = MisSystemUtil.getStuAffairsSbjYS(this.schoolSchema);
            year = misSystemData.getSbj_year();
            sem = misSystemData.getSbj_sem();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getStudentByRegNumber(Integer.toString(year), Integer.toString(sem), reg_no);


//        return getStudentByRegNumber("105", "2", reg_no);
    }

    @Override
    public Student getStudentByRgno(Integer rgno) {
        List<Student> r = getStudentByRgno(Collections.singletonList(rgno));
        if (r == null || r.size() == 0)
            return null;

        return r.get(0);
    }

    public List<Student> getStudentByRgno(List<Integer> rgnoList) {
        if (rgnoList.size() == 0) {
            return Collections.emptyList();
        }

        String sql = "SELECT\n" +
                "  S.*, " +
                "  COMMON.TO_MG_DATE(A.BIRTHDAY) BIRTH,\n" +
                "  A.CNAME AS STU_CNAME,\n" +
                "  A.ENAME   STU_ENAME,\n" +
                "  NULL      STAFF_CODE,\n" +
                "  ES.EMAIL, ES.MOBILE, ES.AVATAR, ES.SOCIAL_ACCOUNT, ES.BIO, ES.INTEREST, ES.NICKNAME, ES.ALT_EMAIL, " +
                "  S.PK   AS RGNO\n" +
                "FROM STU_CLASS_INFO S\n" +
                "       INNER JOIN HS_STU_BASIS AS A ON S.PK = A.RGNO\n" +
                "       LEFT JOIN ACCOUNT AS AA ON S.PK = AA.PK AND BITAND(AA.ROLE_CODE, 8) > 0\n" +
                "       LEFT JOIN EXTRA_STU_DATA ES ON ES.RGNO = A.RGNO " +
                "";
        StringBuilder sb = new StringBuilder("WHERE S.PK IN (?");
        for (int i = rgnoList.size(); i > 1; i--) {
            sb.append(", ?");
        }
        sb.append(")");
        sb.insert(0, sql);

        Student student = null;
        try (Connection pcon = DbConn.getConnection(this);
             PreparedStatement preparedStatement = pcon.prepareStatement(sb.toString());
        ) {
            int idx = 0;
            for (Integer rgno : rgnoList) {
                preparedStatement.setInt(++idx, rgno);
            }

            List<Student> ret = new ArrayList<>();
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    ret.add(convertResultSetToStudent(resultSet));
                }
            }
            return ret;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Student> getStudentList(String register_number, String year, String semester, String classCode, String stu_Cname, String idno, String staff_code, Integer page) throws SQLException {

        StringBuilder sb = new StringBuilder("SELECT * FROM (\n" +
                "  SELECT ROW_NUMBER() OVER (ORDER BY D.CLS_CODE, D.CLS_NO) AS RN,D.*\n" +
                "  FROM (SELECT\n" +
                "  S.*, " +
                "  COMMON.TO_MG_DATE(A.BIRTHDAY) BIRTH,\n" +
                "  A.CNAME AS STU_CNAME,\n" +
                "  A.ENAME   STU_ENAME,\n" +
                "  S.PK   AS RGNO,\n" +
                "  ES.EMAIL, ES.MOBILE, ES.AVATAR, ES.SOCIAL_ACCOUNT, ES.BIO, ES.INTEREST, ES.NICKNAME, ES.ALT_EMAIL " +
                (staff_code == null ? ", NULL AS STAFF_CODE FROM STU_CLASS_INFO S\n" : "FROM STU_CLASS_INFO_WITH_TUTOR S\n") +
                "       INNER JOIN HS_STU_BASIS AS A ON S.PK = VARCHAR(A.RGNO)\n" +
                "       LEFT JOIN EXTRA_STU_DATA ES ON ES.RGNO = S.PK\n" +
                ") AS D WHERE  1=1");

        List<String> data = new LinkedList<>();
        CourseRecordUnlockStatusDao courseRecordUnlockStatusDao = new CourseRecordUnlockStatusDaoImpl(this);
        PerformanceUnlockStatusDao performanceUnlockStatusDao = new PerformanceUnlockStatusDaoImpl(this);
        bindCommonVars(sb, data, register_number, year, semester, classCode, stu_Cname, idno, staff_code);
        sb
                .append(")")
                .append("WHERE RN BETWEEN ? AND ?\n");
        List<Student> studentList = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int sysYear = 0;
        int sysSem = 0;
        int cyear =new SystemConfigDaoImpl(this).getSystemConfig().getCourseStudyRecord().getActiveYear();
        int csem = new SystemConfigDaoImpl(this).getSystemConfig().getCourseStudyRecord().getActiveSem();
        int pyear = new SystemConfigDaoImpl(this).getSystemConfig().getPerformance().getSubmitYear();
        MisSystemData misSystemData = null;
        try {    // 取得學務系統學年期
            misSystemData = MisSystemUtil.getStuAffairsSbjYS(this.schoolSchema);
            sysYear = misSystemData.getSbj_year();
            sysSem = misSystemData.getSbj_sem();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try (Connection pcon = DbConn.getConnection(this)) {
            preparedStatement = pcon.prepareStatement(sb.toString());
            int i = 1;

            for (String s : data) {
                preparedStatement.setString(i++, s);
            }

            preparedStatement.setInt(i++, ((page - 1) * RECORD_PER_PAGE) + 1);
            preparedStatement.setInt(i++, ((page) * RECORD_PER_PAGE));

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Student student = convertResultSetToStudent(resultSet);
                student.setCourseRecordUnlock(courseRecordUnlockStatusDao.isInUnlockStatus(Integer.toString(cyear), Integer.toString(csem), student.getRgno()));
                student.setPerformanceUnlock(performanceUnlockStatusDao.isInUnlockStatus(Integer.toString(pyear), student.getRgno()));
                studentList.add(student);
            }
            return studentList;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null)
                preparedStatement.close();
            if (resultSet != null)
                resultSet.close();
        }
        return null;
    }

    @Override
    public List<Student> getStudentListByClass(String year, String semester, String classCode) throws SQLException {
        StringBuilder sb = new StringBuilder("SELECT * FROM (\n" +
                "  SELECT ROW_NUMBER() OVER (ORDER BY D.CLS_CODE, D.CLS_NO) AS RN,D.*\n" +
                "  FROM (SELECT\n" +
                "  S.*, " +
                "  COMMON.TO_MG_DATE(A.BIRTHDAY) BIRTH,\n" +
                "  A.CNAME AS STU_CNAME,\n" +
                "  A.ENAME   STU_ENAME,\n" +
                "  S.PK   AS RGNO,\n" +
                "  ES.EMAIL, ES.MOBILE, ES.AVATAR, ES.SOCIAL_ACCOUNT, ES.BIO, ES.INTEREST, ES.NICKNAME, ES.ALT_EMAIL " +
                ", NULL AS STAFF_CODE FROM STU_CLASS_INFO S\n" +
                "       INNER JOIN HS_STU_BASIS AS A ON S.PK = VARCHAR(A.RGNO)\n" +
                "       LEFT JOIN EXTRA_STU_DATA ES ON ES.RGNO = S.PK\n" +
                ") AS D WHERE  1=1");

        List<String> data = new LinkedList<>();
//        CourseRecordUnlockStatusDao courseRecordUnlockStatusDao = new CourseRecordUnlockStatusDaoImpl(this);
//        PerformanceUnlockStatusDao performanceUnlockStatusDao = new PerformanceUnlockStatusDaoImpl(this);
        bindCommonVars(sb, data, null, year, semester, classCode, null, null, null);
        sb.append(")");
        List<Student> studentList = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int sysYear = 0;
        int sysSem = 0;
        MisSystemData misSystemData = null;
        try {    // 取得學務系統學年期
            misSystemData = MisSystemUtil.getStuAffairsSbjYS(this.schoolSchema);
            sysYear = misSystemData.getSbj_year();
            sysSem = misSystemData.getSbj_sem();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try (Connection pcon = DbConn.getConnection(this)) {
            preparedStatement = pcon.prepareStatement(sb.toString());
            int i = 1;

            for (String s : data) {
                preparedStatement.setString(i++, s);
            }

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Student student = convertResultSetToStudent(resultSet);
//                student.setCourseRecordUnlock(courseRecordUnlockStatusDao.isInUnlockStatus(Integer.toString(sysYear), Integer.toString(sysSem), student.getRgno()));
//                student.setPerformanceUnlock(performanceUnlockStatusDao.isInUnlockStatus(Integer.toString(sysYear), student.getRgno()));
                studentList.add(student);
            }
            return studentList;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null)
                preparedStatement.close();
            if (resultSet != null)
                resultSet.close();
        }
        return null;
    }

    @Override
    public List<Student> getStudentListByGrade(String year, String semester, String grade) throws SQLException {
        StringBuilder sb = new StringBuilder("SELECT * FROM (\n" +
                "  SELECT ROW_NUMBER() OVER (ORDER BY D.CLS_CODE, D.CLS_NO) AS RN,D.*\n" +
                "  FROM (SELECT\n" +
                "  S.*, " +
                "  COMMON.TO_MG_DATE(A.BIRTHDAY) BIRTH,\n" +
                "  A.CNAME AS STU_CNAME,\n" +
                "  A.ENAME   STU_ENAME,\n" +
                "  S.PK   AS RGNO,\n" +
                "  ES.EMAIL, ES.MOBILE, ES.AVATAR, ES.SOCIAL_ACCOUNT, ES.BIO, ES.INTEREST, ES.NICKNAME, ES.ALT_EMAIL " +
                ", NULL AS STAFF_CODE FROM STU_CLASS_INFO S\n" +
                "       INNER JOIN HS_STU_BASIS AS A ON S.PK = VARCHAR(A.RGNO)\n" +
                "       LEFT JOIN EXTRA_STU_DATA ES ON ES.RGNO = S.PK\n" +
                ") AS D WHERE  1=1 AND D.GRADE=" + grade);

        List<String> data = new LinkedList<>();
//        CourseRecordUnlockStatusDao courseRecordUnlockStatusDao = new CourseRecordUnlockStatusDaoImpl(this);
//        PerformanceUnlockStatusDao performanceUnlockStatusDao = new PerformanceUnlockStatusDaoImpl(this);
        bindCommonVars(sb, data, null, year, semester, null, null, null, null);
        sb.append(")");
        List<Student> studentList = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int sysYear = 0;
        int sysSem = 0;
        MisSystemData misSystemData = null;
        try {    // 取得學務系統學年期
            misSystemData = MisSystemUtil.getStuAffairsSbjYS(this.schoolSchema);
            sysYear = misSystemData.getSbj_year();
            sysSem = misSystemData.getSbj_sem();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try (Connection pcon = DbConn.getConnection(this)) {
            preparedStatement = pcon.prepareStatement(sb.toString());
            int i = 1;

            for (String s : data) {
                preparedStatement.setString(i++, s);
            }

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Student student = convertResultSetToStudent(resultSet);
//                student.setCourseRecordUnlock(courseRecordUnlockStatusDao.isInUnlockStatus(Integer.toString(sysYear), Integer.toString(sysSem), student.getRgno()));
//                student.setPerformanceUnlock(performanceUnlockStatusDao.isInUnlockStatus(Integer.toString(sysYear), student.getRgno()));
                studentList.add(student);
            }
            return studentList;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null)
                preparedStatement.close();
            if (resultSet != null)
                resultSet.close();
        }
        return null;
    }

    @Override
    public Integer getPageNumber(String register_number, String year, String semester, String classCode, String stu_Cname, String idno, String staff_code, Integer page) throws SQLException {
        int total = getTotalAmount(register_number, year, semester, classCode, stu_Cname, idno, staff_code);

        int pageNumber = 1;
        if (total > 0) {

            pageNumber = ((total - 1) / RECORD_PER_PAGE) + 1;
        }

        return pageNumber;
    }

    public Integer getTotalAmount(String register_number, String year, String semester, String classCode, String stu_Cname, String idno, String staff_code) throws SQLException {
        StringBuilder sb = new StringBuilder("SELECT  COUNT(*) FROM\n" +
                "  (SELECT\n" +
                "  S.*,\n" +
                "  A.CNAME AS STU_CNAME,\n" +
                "  A.ENAME   STU_ENAME,\n" +
                "  S.PK   AS RGNO,\n" +
                "  ES.EMAIL, ES.MOBILE, ES.AVATAR, ES.SOCIAL_ACCOUNT, ES.BIO, ES.INTEREST, ES.NICKNAME, ES.ALT_EMAIL " +
                (staff_code == null ? ", NULL AS STAFF_CODE FROM STU_CLASS_INFO S\n" : "FROM STU_CLASS_INFO_WITH_TUTOR S\n") +
                "       INNER JOIN HS_STU_BASIS AS A ON S.PK = A.RGNO\n" +
                "       LEFT JOIN EXTRA_STU_DATA ES ON ES.RGNO = S.PK\n" +
                ")\n" +
                "WHERE  1=1");
        List<String> data = new LinkedList<>();
        bindCommonVars(sb, data, register_number, year, semester, classCode, stu_Cname, idno, staff_code);
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try (Connection pcon = DbConn.getConnection(this)) {
            preparedStatement = pcon.prepareStatement(sb.toString());
            int i = 1;


            for (String s : data) {
                preparedStatement.setString(i++, s);
            }

            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null)
                preparedStatement.close();
            if (resultSet != null)
                resultSet.close();
        }
        return 0;
    }

    @Override
    public boolean updateStudent(Student student) {
        String sql = "MERGE INTO EXTRA_STU_DATA a USING (\n" +
                "    SELECT ? AS RGNO , \n" +
                "        ? AS EMAIL,\n" +
                "        ? AS MOBILE,\n" +
                "        ? AS AVATAR,\n" +
                "        ? AS SOCIAL_ACCOUNT,\n" +
                "        ? AS BIO,\n" +
                "        ? AS INTEREST,\n" +
                "        ? AS NICKNAME,\n" +
                "        ? AS ALT_EMAIL\n" +
                "    FROM SYSIBM.SYSDUMMY1\n" +
                ") b ON (a.RGNO = b.RGNO)\n" +
                "WHEN MATCHED THEN\n" +
                "    UPDATE SET\n" +
                "        EMAIL = b.EMAIL, MOBILE = b.MOBILE, AVATAR = b.AVATAR,\n" +
                "        SOCIAL_ACCOUNT = b.SOCIAL_ACCOUNT, BIO = b.BIO, INTEREST = b.INTEREST,\n" +
                "        NICKNAME = b.NICKNAME, ALT_EMAIL = b.ALT_EMAIL\n" +
                "WHEN NOT MATCHED THEN INSERT (" +
                "  RGNO, EMAIL, MOBILE, \n" +
                "  AVATAR, SOCIAL_ACCOUNT, BIO, \n" +
                "  INTEREST, NICKNAME, ALT_EMAIL" +
                ") VALUES (" +
                "  b.RGNO, b.EMAIL, b.MOBILE, \n" +
                "  b.AVATAR, b.SOCIAL_ACCOUNT, b.BIO, \n" +
                "  b.INTEREST, b.NICKNAME, b.ALT_EMAIL" +
                ")";

        try (Connection pcon = DbConn.getConnection(this)) {
            try (PreparedStatement ps = pcon.prepareStatement(sql)) {
                int i = 0;
                ps.setString(++i, student.getIdno());
                ps.setString(++i, student.getEmail());
                ps.setString(++i, student.getMobile_telno());
                ps.setString(++i, student.getAvatar());
                ps.setString(++i, student.getSocialAccount());
                ps.setString(++i, student.getBio());
                ps.setString(++i, student.getInterest());
                ps.setString(++i, student.getNickname());
                ps.setString(++i, student.getAltMail());

                return ps.executeUpdate() >= 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void bindCommonVars(final StringBuilder sb, final List<String> data,
                                       String register_number, String year, String semester, String classCode, String stu_Cname, String idno, String staff_code) {
        if (!StringUtils.isNullOrEmpty(register_number)) {
            sb.append(" AND reg_no=?");
            data.add(register_number);
        }
        if (!StringUtils.isNullOrEmpty(year)) {
            sb.append(" AND term_year=?");
            data.add(year);
        }
        if (!StringUtils.isNullOrEmpty(semester)) {
            sb.append(" AND term_sem=?");
            data.add(semester);
        }
        if (!StringUtils.isNullOrEmpty(classCode)) {
            sb.append(" AND CLS_CODE=?");
            data.add(classCode);
        }
        if (!StringUtils.isNullOrEmpty(stu_Cname)) {
            sb.append(" AND STU_CNAME LIKE ?");
            data.add("%" + stu_Cname + "%");
        }
        if (!StringUtils.isNullOrEmpty(idno)) {
            sb.append(" AND idno=?");
            data.add(idno);
        }
        if (!StringUtils.isNullOrEmpty(staff_code)) {
            sb.append(" AND staff_code=?");
            data.add(staff_code);
        }
    }

    @Override
    public Role getDetailByRegNumber(String regNumber, Authority.RoleIndex roleIndex) throws SQLException {
        Student student = getStudentByRegNumber(regNumber);
        return student;
    }

    private static Student convertResultSetToStudent(ResultSet resultSet) throws SQLException {
        return new Student(
                resultSet.getString("REG_NO"),
                resultSet.getString("TERM_YEAR"),
                resultSet.getString("TERM_SEM"),
                resultSet.getString("CLS_CODE"),
                resultSet.getString("CLS_CNAME"),
                resultSet.getString("CLS_NO"),
                resultSet.getString("STU_CNAME"),
                resultSet.getString("STU_ENAME"),
                resultSet.getString("IDNO"),
                resultSet.getString("BIRTH"),
                resultSet.getString("EMAIL"),
                resultSet.getString("MOBILE"),
                resultSet.getString("STAFF_CODE"),
                resultSet.getInt("RGNO"),
                resultSet.getString("AVATAR"),
                resultSet.getString("SOCIAL_ACCOUNT"),
                resultSet.getString("BIO"),
                resultSet.getString("NICKNAME"),
                resultSet.getString("INTEREST"),
                resultSet.getString("ALT_EMAIL")
        );
    }

}
