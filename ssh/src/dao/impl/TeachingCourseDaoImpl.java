package dao.impl;


import dao.SystemConfigDao;
import dao.TeachingCourseDao;
import dbconn.DbConn;
import model.Course;
import model.HasSchoolSchema;
import model.SystemConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TeachingCourseDaoImpl extends BaseDao implements TeachingCourseDao {
    //TODO 頁數整合
    public final static Integer RECORD_PER_PAGE = 4;
    private SystemConfigDao systemConfigDao = new SystemConfigDaoImpl(this);


    public TeachingCourseDaoImpl(HasSchoolSchema hasSchoolSchemaObject) {
        super(hasSchoolSchemaObject);
    }


    @Override
    public List<Course> getTeachingCourse(String staff_code, String term_year, String term_sem, String class_name, String course_name, Integer page) throws SQLException {
        SystemConfig systemConfig = systemConfigDao.getSystemConfig();
        String sql = "SELECT * FROM (\n" +
                "SELECT ROW_NUMBER() OVER () AS RN,C.*\n" +
                "FROM TEA_TEACHING_COURSE AS C WHERE 1 = 1";
        if (staff_code != null) {
            sql += " AND staff_code=?";
        }
        if (term_year != null) {
            sql += " AND term_year=?";
        }
        if (term_sem != null) {
            sql += " AND term_sem=?";
        }
        if (class_name != null) {
            sql += " AND class_cname LIKE ?";
        }
        if (course_name != null) {
            sql += " AND course_cname LIKE ?";
        }
        sql += ")";
        sql += "WHERE RN BETWEEN ? AND ?\n";
        List<Course> courseList = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try (Connection pcon = DbConn.getConnection(this)) {
            preparedStatement = pcon.prepareStatement(sql);
            int i = 1;

            if (staff_code != null) {
                preparedStatement.setString(i++, staff_code);
            }
            if (term_year != null) {
                preparedStatement.setString(i++, term_year);
            }
            if (term_sem != null) {
                preparedStatement.setString(i++, term_sem);
            }
            if (class_name != null) {
                preparedStatement.setString(i++, "%" + class_name + "%");
            }
            if (course_name != null) {
                preparedStatement.setString(i++, "%" + course_name + "%");
            }
            preparedStatement.setInt(i++, ((page - 1) * RECORD_PER_PAGE) + 1);
            preparedStatement.setInt(i++, ((page) * RECORD_PER_PAGE));

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Course course = new Course(
                        resultSet.getString("term_year"),
                        resultSet.getString("term_sem"),
                        resultSet.getString("class_cname"),
                        resultSet.getString("course_cname"),
                        resultSet.getString("course_num"),
                        resultSet.getString("names")
                );
                if(course.getTerm_year().equals(Integer.toString(systemConfig.getCourseStudyRecord().getActiveYear()))&&course.getTerm_sem().equals(Integer.toString(systemConfig.getCourseStudyRecord().getActiveSem())))
                {
                    Date s = systemConfig.getCourseStudyRecord().getStudentStartDateTime();
                    Date e = systemConfig.getCourseStudyRecord().getStudentEndDateTime();
                    Date n = new Date();
                    if(n.after(s) && n.before(e))
                    {
                        course.setUploadable(true);
                    }
                    else
                    {
                        course.setUploadable(false);
                    }
                }
                else
                {
                    course.setUploadable(false);
                }
                courseList.add(course);
            }
            return courseList;
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
    public Integer getPageNumber(String staff_code, String term_year, String term_sem, String class_name, String course_name) throws SQLException {
        String sql = "SELECT COUNT(*) FROM TEA_TEACHING_COURSE AS C WHERE 1 = 1";
        if (staff_code != null) {
            sql += " AND staff_code=?";
        }
        if (term_year != null) {
            sql += " AND term_year=?";
        }
        if (term_sem != null) {
            sql += " AND term_sem=?";
        }
        if (class_name != null) {
            sql += " AND class_cname LIKE ?";
        }
        if (course_name != null) {
            sql += " AND course_cname LIKE ?";
        }
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int pagenumber = 1;
        try (Connection pcon = DbConn.getConnection(this)) {
            preparedStatement = pcon.prepareStatement(sql);
            int i = 1;
            if (staff_code != null) {
                preparedStatement.setString(i++, staff_code);
            }
            if (term_year != null) {
                preparedStatement.setString(i++, term_year);
            }
            if (term_sem != null) {
                preparedStatement.setString(i++, term_sem);
            }
            if (class_name != null) {
                preparedStatement.setString(i++, "%" + class_name + "%");
            }
            if (course_name != null) {
                preparedStatement.setString(i++, "%" + course_name + "%");
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
}
