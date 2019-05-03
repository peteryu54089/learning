package dao.impl;

import dao.CourseRecordDao;
import dao.CourseRecordDocumentDao;
import dao.SelectCourseDao;
import dao.SystemConfigDao;
import dbconn.DbConn;
import model.CourseRecord;
import model.HasSchoolSchema;
import model.SystemConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseRecordDaoImpl extends BaseDao implements CourseRecordDao {
    //TODO 頁數整合
    public final static Integer RECORD_PER_PAGE = 4;
    private SystemConfigDao systemConfigDao = new SystemConfigDaoImpl(this);

    //TODO 換資料表

    public CourseRecordDaoImpl(HasSchoolSchema hasSchoolSchemaObject) {
        super(hasSchoolSchemaObject);
    }

    @Override
    public List<CourseRecord> getCourseRecord(Integer rgno, String term_year, String term_sem, String course_num, String course_cname, String status, String stu_name, boolean tutor, String check, Integer page) throws SQLException {
        SystemConfig systemConfig = systemConfigDao.getSystemConfig();
        String sql = "SELECT * FROM\n" +
                "  (SELECT ROW_NUMBER() OVER () AS RN,C.*\n" +
                "FROM (\n" +
                "SELECT * FROM\n" +
                "STU_COURSE_RECORD_WITH_DETAILS\n" +
                "WHERE DELETED_AT is NULL) AS C WHERE 1=1";
        if (tutor) {
            sql += " AND status!=1";
        }
        if (rgno != null) {
            sql += " AND RGNO=?";
        }
        if (term_year != null) {
            sql += " AND term_year=?";
        }
        if (term_sem != null) {
            sql += " AND term_sem=?";
        }
        if (course_num != null) {
            sql += " AND course_num=?";
        }
        if (course_cname != null) {
            sql += " AND course_cname LIKE ?";
        }
        if (status != null) {
            sql += " AND status=?";
        }
        if (stu_name != null) {
            sql += " AND grd_cname LIKE ?";
        }
        if (check != null) {
            sql += " AND check=?";
        }
        sql += "ORDER BY COURSE_NUM, CREATED_AT DESC)";
        if (page != null)
            sql += "WHERE RN BETWEEN ? AND ?\n";
        List<CourseRecord> courseRecordList = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try (Connection pcon = DbConn.getConnection(this)) {
            preparedStatement = pcon.prepareStatement(sql);
            int i = 1;
            if (rgno != null) {
                preparedStatement.setInt(i++, rgno);
            }
            if (term_year != null) {
                preparedStatement.setString(i++, term_year);
            }
            if (term_sem != null) {
                preparedStatement.setString(i++, term_sem);
            }
            if (course_num != null) {
                preparedStatement.setString(i++, course_num);
            }
            if (course_cname != null) {
                preparedStatement.setString(i++, "%" + course_cname + "%");
            }
            if (status != null) {
                preparedStatement.setString(i++, status);
            }
            if (stu_name != null) {
                preparedStatement.setString(i++, "%" + stu_name + "%");
            }
            if (check != null) {
                preparedStatement.setString(i++, check);
            }
            if (page != null) {
                preparedStatement.setInt(i++, ((page - 1) * RECORD_PER_PAGE) + 1);
                preparedStatement.setInt(i++, ((page) * RECORD_PER_PAGE));
            }
            resultSet = preparedStatement.executeQuery();
            CourseRecordDocumentDao courseRecordDocumentDao = new CourseRecordDocumentDaoImpl(this);
            SelectCourseDao selectCourseDao = new SelectCourseDaoImpl(this);
            while (resultSet.next()) {
                CourseRecord courseRecord = new CourseRecord(
                        resultSet.getString("id"),
                        resultSet.getString("REG_NO"),
                        resultSet.getInt("RGNO"),
                        resultSet.getString("term_year"),
                        resultSet.getString("term_sem"),
                        resultSet.getString("course_num"),
                        resultSet.getString("course_cname"),
                        resultSet.getString("content"),
                        resultSet.getString("status"),
                        resultSet.getTimestamp("submitted_at"),
                        resultSet.getTimestamp("created_at"),
                        resultSet.getTimestamp("deleted_at"),
                        resultSet.getString("verifier_staff_code"),
                        resultSet.getTimestamp("verified_at"),
                        resultSet.getString("verify_message"),
                        resultSet.getString("verifier_staff_code"),
                        resultSet.getString("CNAME"),
                        resultSet.getString("Check"));
                courseRecord.setVerify_name(resultSet.getString("STAFF_CNAME"));
                courseRecord.setCourseRecordDocumentList(courseRecordDocumentDao.getCourseRecordDocument(resultSet.getString("id")));
                courseRecord.setCourse(selectCourseDao.getSelectCourseByCourseNo(resultSet.getString("course_num")));

                if(courseRecord.getTerm_year().equals(Integer.toString(systemConfig.getCourseStudyRecord().getActiveYear()))&&courseRecord.getTerm_sem().equals(Integer.toString(systemConfig.getCourseStudyRecord().getActiveSem())))
                {
                    java.util.Date s = systemConfig.getCourseStudyRecord().getStudentStartDateTime();
                    java.util.Date e = systemConfig.getCourseStudyRecord().getStudentEndDateTime();
                    java.util.Date n = new java.util.Date();
                    if(n.after(s) && n.before(e) && courseRecord.getStatus().equals("1"))
                    {
                        courseRecord.setSubmittable(true);
                    }
                    else
                    {
                        courseRecord.setSubmittable(false);
                    }
                    s = systemConfig.getCourseStudyRecord().getTeacherStartDateTime();
                    e = systemConfig.getCourseStudyRecord().getTeacherEndDateTime();
                    if(n.after(s) && n.before(e))
                    {
                        courseRecord.setVerifyable(true);
                    }
                    else
                    {
                        courseRecord.setVerifyable(false);
                    }
                }
                else
                {
                    courseRecord.setVerifyable(false);
                    courseRecord.setSubmittable(false);
                }
                courseRecordList.add(courseRecord);

            }
            return courseRecordList;
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
    public List<CourseRecord> getCourseRecord(Integer rgno, String term_year, String term_sem,
                                              List<String> course_num, String course_cname, String status, String stu_name, boolean tutor, String check, Integer page) throws SQLException {
        SystemConfig systemConfig = systemConfigDao.getSystemConfig();
        String sql = "SELECT * FROM\n" +
                "  (SELECT ROW_NUMBER() OVER () AS RN,C.*\n" +
                "FROM (\n" +
                "SELECT * FROM\n" +
                "(SELECT * FROM STU_COURSE_RECORD_WITH_DETAILS)\n" +
                "WHERE DELETED_AT is NULL) AS C WHERE 1=1";
        if (tutor) {
            sql += " AND status!=1";
        }
        if (rgno != null) {
            sql += " AND RGNO=?";
        }
        if (term_year != null) {
            sql += " AND term_year=?";
        }
        if (term_sem != null) {
            sql += " AND term_sem=?";
        }
        if (course_num != null && course_num.size() > 0) {
            sql += " AND course_num IN (?";
            for (int x = 1, y = course_num.size(); x < y; x++)
                sql += ", ?";
            sql += ")";
        }
        if (course_cname != null) {
            sql += " AND course_cname LIKE ?";
        }
        if (status != null) {
            sql += " AND status=?";
        }
        if (stu_name != null) {
            sql += " AND grd_cname LIKE ?";
        }
        if (check != null) {
            sql += " AND check=?";
        }
        sql += "ORDER BY COURSE_NUM, CREATED_AT DESC)";
        if (page != null)
            sql += "WHERE RN BETWEEN ? AND ?\n";
        List<CourseRecord> courseRecordList = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try (Connection pcon = DbConn.getConnection(this)) {
            preparedStatement = pcon.prepareStatement(sql);
            int i = 1;
            if (rgno != null) {
                preparedStatement.setInt(i++, rgno);
            }
            if (term_year != null) {
                preparedStatement.setString(i++, term_year);
            }
            if (term_sem != null) {
                preparedStatement.setString(i++, term_sem);
            }

            if (course_num != null && course_num.size() > 0) {
                for (int x = 0, y = course_num.size(); x < y; x++) {
                    preparedStatement.setString(i++, course_num.get(x));
                }
            }

            if (course_cname != null) {
                preparedStatement.setString(i++, "%" + course_cname + "%");
            }
            if (status != null) {
                preparedStatement.setString(i++, status);
            }
            if (stu_name != null) {
                preparedStatement.setString(i++, "%" + stu_name + "%");
            }
            if (check != null) {
                preparedStatement.setString(i++, check);
            }
            if (page != null) {
                preparedStatement.setInt(i++, ((page - 1) * RECORD_PER_PAGE) + 1);
                preparedStatement.setInt(i++, ((page) * RECORD_PER_PAGE));
            }
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                CourseRecord courseRecord = new CourseRecord(
                        resultSet.getString("id"),
                        resultSet.getString("REG_NO"),
                        resultSet.getInt("RGNO"),
                        resultSet.getString("term_year"),
                        resultSet.getString("term_sem"),
                        resultSet.getString("course_num"),
                        resultSet.getString("course_cname"),
                        resultSet.getString("content"),
                        resultSet.getString("status"),
                        resultSet.getTimestamp("submitted_at"),
                        resultSet.getTimestamp("created_at"),
                        resultSet.getTimestamp("deleted_at"),
                        resultSet.getString("verifier_staff_code"),
                        resultSet.getTimestamp("verified_at"),
                        resultSet.getString("verify_message"),
                        resultSet.getString("verifier_staff_code"),
                        resultSet.getString("CNAME"),
                        resultSet.getString("Check"));

                courseRecord.setVerify_name(resultSet.getString("STAFF_CNAME"));
                if(courseRecord.getTerm_year().equals(Integer.toString(systemConfig.getCourseStudyRecord().getActiveYear()))&&courseRecord.getTerm_sem().equals(Integer.toString(systemConfig.getCourseStudyRecord().getActiveSem())))
                {
                    java.util.Date s = systemConfig.getCourseStudyRecord().getStudentStartDateTime();
                    java.util.Date e = systemConfig.getCourseStudyRecord().getStudentEndDateTime();
                    java.util.Date n = new java.util.Date();
                    if(n.after(s) && n.before(e) && courseRecord.getStatus().equals("1"))
                    {
                        courseRecord.setSubmittable(true);
                    }
                    else
                    {
                        courseRecord.setSubmittable(false);
                    }
                    s = systemConfig.getCourseStudyRecord().getTeacherStartDateTime();
                    e = systemConfig.getCourseStudyRecord().getTeacherEndDateTime();
                    if(n.after(s) && n.before(e))
                    {
                        courseRecord.setVerifyable(true);
                    }
                    else
                    {
                        courseRecord.setVerifyable(false);
                    }
                }
                else
                {
                    courseRecord.setVerifyable(false);
                    courseRecord.setSubmittable(false);
                }
                courseRecordList.add(courseRecord);

            }
            return courseRecordList;
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
    public Integer getPageNumber(Integer rgno, String term_year, String term_sem, String course_num, String course_cname, String status, String stu_name, boolean tutor) throws SQLException {
        String sql = "SELECT COUNT(*) FROM\n" +
                "  (SELECT * FROM STU_COURSE_RECORD_WITH_DETAILS)\n" +
                "   WHERE DELETED_AT is NULL ";
        if (tutor) {
            sql += " AND status!=1";
        }
        if (rgno != null) {
            sql += " AND RGNO=?";
        }
        if (term_year != null) {
            sql += " AND term_year=?";
        }
        if (term_sem != null) {
            sql += " AND term_sem=?";
        }
        if (course_num != null) {
            sql += " AND course_num=?";
        }
        if (course_cname != null) {
            sql += " AND course_cname LIKE ?";
        }
        if (status != null) {
            sql += " AND status=?";
        }
        if (stu_name != null) {
            sql += " AND grd_cname LIKE ?";
        }
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int pagenumber = 1;
        try (Connection pcon = DbConn.getConnection(this)) {
            preparedStatement = pcon.prepareStatement(sql);
            int i = 1;
            if (rgno != null) {
                preparedStatement.setInt(i++, rgno);
            }
            if (term_year != null) {
                preparedStatement.setString(i++, term_year);
            }
            if (term_sem != null) {
                preparedStatement.setString(i++, term_sem);
            }
            if (course_num != null) {
                preparedStatement.setString(i++, course_num);
            }
            if (course_cname != null) {
                preparedStatement.setString(i++, "%" + course_cname + "%");
            }
            if (status != null) {
                preparedStatement.setString(i++, status);
            }
            if (stu_name != null) {
                preparedStatement.setString(i++, "%" + stu_name + "%");
            }
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                pagenumber = resultSet.getInt(1);
                pagenumber = ((pagenumber - 1) / RECORD_PER_PAGE) + 1;
            }
            return pagenumber;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null)
                preparedStatement.close();
            if (resultSet != null)
                resultSet.close();
        }
        return 1;
    }


    @Override
    public Integer uploadCourseRecord(Integer rgno, Integer term_year, Integer term_sem, String course_num, String course_cname, String content) throws SQLException {
        String sql = "INSERT INTO STUDENT_COURSE_RECORD (RGNO,TERM_YEAR,TERM_SEM,COURSE_NUM,COURSE_CNAME,CONTENT) VALUES (?,?,?,?,?,?)";
        PreparedStatement preparedStatement = null;
        Integer pid = -1;
        try (Connection pcon = DbConn.getConnection(this)) {
            preparedStatement = pcon.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, rgno);
            preparedStatement.setInt(2, term_year);
            preparedStatement.setInt(3, term_sem);
            preparedStatement.setString(4, course_num);
            preparedStatement.setString(5, course_cname);
            preparedStatement.setString(6, content);
            preparedStatement.executeUpdate();
            ResultSet rs = preparedStatement.getGeneratedKeys();
            if (rs.next()) {
                pid = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null)
                preparedStatement.close();
        }
        return pid;
    }

    @Override
    public CourseRecord getCourseRecordByID(Integer rgno, String ID) throws SQLException {
        String sql = "SELECT * FROM\n" +
                "(SELECT * FROM STU_COURSE_RECORD_WITH_DETAILS)\n" + 
                "WHERE ID = ?";
        if (rgno != null)
            sql += "AND RGNO=?";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        CourseRecord courseRecord = null;
        try (Connection pcon = DbConn.getConnection(this)) {
            preparedStatement = pcon.prepareStatement(sql);
            preparedStatement.setString(1, ID);
            if (rgno != null)
                preparedStatement.setInt(2, rgno);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                courseRecord = new CourseRecord(
                        resultSet.getString("id"),
                        resultSet.getString("REG_NO"),
                        resultSet.getInt("RGNO"),
                        resultSet.getString("term_year"),
                        resultSet.getString("term_sem"),
                        resultSet.getString("course_num"),
                        resultSet.getString("course_cname"),
                        resultSet.getString("content"),
                        resultSet.getString("status"),
                        resultSet.getTimestamp("submitted_at"),
                        resultSet.getTimestamp("created_at"),
                        resultSet.getTimestamp("deleted_at"),
                        resultSet.getString("staff_cname"),
                        resultSet.getTimestamp("verified_at"),
                        resultSet.getString("verify_message"),
                        resultSet.getString("verifier_staff_code"),
                        resultSet.getString("CNAME"),
                        resultSet.getString("Check"));
                courseRecord.setVerify_name(resultSet.getString("STAFF_CNAME"));
            }

            CourseRecordDocumentDao courseRecordDocumentDao = new CourseRecordDocumentDaoImpl(this);
            courseRecord.setCourseRecordDocumentList(courseRecordDocumentDao.getCourseRecordDocument(resultSet.getString("id")));
            SystemConfigDao systemConfigDao = new SystemConfigDaoImpl(this);
            SystemConfig systemConfig = systemConfigDao.getSystemConfig();
            if(courseRecord.getTerm_year().equals(Integer.toString(systemConfig.getCourseStudyRecord().getActiveYear()))&&courseRecord.getTerm_sem().equals(Integer.toString(systemConfig.getCourseStudyRecord().getActiveSem())))
            {
                java.util.Date s = systemConfig.getCourseStudyRecord().getStudentStartDateTime();
                java.util.Date e = systemConfig.getCourseStudyRecord().getStudentEndDateTime();
                java.util.Date n = new java.util.Date();
                if(n.after(s) && n.before(e)&& courseRecord.getStatus().equals("1"))
                {
                    courseRecord.setSubmittable(true);
                }
                else
                {
                    courseRecord.setSubmittable(false);
                }
                s = systemConfig.getCourseStudyRecord().getTeacherStartDateTime();
                e = systemConfig.getCourseStudyRecord().getTeacherEndDateTime();
                if(n.after(s) && n.before(e))
                {
                    courseRecord.setVerifyable(true);
                }
                else
                {
                    courseRecord.setVerifyable(false);
                }
            }
            else
            {
                courseRecord.setVerifyable(false);
                courseRecord.setSubmittable(false);
            }
            return courseRecord;
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
    public void updateCourseRecordByID(Integer rgno, String ID, CourseRecord courseRecord) throws SQLException {
        String sql = "UPDATE STUDENT_COURSE_RECORD SET CONTENT = ?, STATUS = ?,SUBMITTED_AT = ?,VERIFIED_AT = ?,VERIFY_MESSAGE = ? , VERIFIER_STAFF_CODE = ?,CHECK = ? WHERE RGNO = ? AND id = ?";
        PreparedStatement preparedStatement = null;
        try (Connection pcon = DbConn.getConnection(this)) {
            preparedStatement = pcon.prepareStatement(sql);
            preparedStatement.setString(1, courseRecord.getContent());
            preparedStatement.setString(2, courseRecord.getStatus());
            preparedStatement.setTimestamp(3, courseRecord.getSubmitted_at());
            preparedStatement.setTimestamp(4, courseRecord.getVerify_at());
            preparedStatement.setString(5, courseRecord.getVerify_message());
            preparedStatement.setString(6, courseRecord.getVerifier_staff_code());
            preparedStatement.setString(7, courseRecord.getCheck());
            preparedStatement.setInt(8, rgno);
            preparedStatement.setString(9, ID);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null)
                preparedStatement.close();
        }
    }

    @Override
    public void deleteCourseRecord(Integer rgno, String ID) throws SQLException {
        String sql = "UPDATE STUDENT_COURSE_RECORD SET DELETED_AT = ? WHERE RGNO = ? AND ID = ?";
        PreparedStatement preparedStatement = null;
        try (Connection pcon = DbConn.getConnection(this)) {
            preparedStatement = pcon.prepareStatement(sql);
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            preparedStatement.setTimestamp(1, timestamp);
            preparedStatement.setInt(2, rgno);
            preparedStatement.setString(3, ID);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null)
                preparedStatement.close();
        }
    }

}
