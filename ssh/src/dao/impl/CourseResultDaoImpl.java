package dao.impl;


import dao.CourseResultDao;
import dbconn.DbConn;
import model.CourseResult;
import model.HasSchoolSchema;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CourseResultDaoImpl extends BaseDao implements CourseResultDao {
    //TODO 頁數整合
    public final static Integer RECORD_PER_PAGE = 10;

    public CourseResultDaoImpl(HasSchoolSchema hasSchoolSchemaObject) {
        super(hasSchoolSchemaObject);
    }

    @Override
    public List<CourseResult> getCourseResult(Integer rgno, String term_year, String term_sem, String name, Integer page) throws SQLException {
        String sql = "SELECT * FROM (SELECT ROW_NUMBER() OVER ( ) AS RN,I.*\n" +
                "      FROM STU_COURSE_RESULT AS I\n" +
                "            WHERE 1 = 1";
        if (rgno != null) {
            sql += " AND rgno=?";
        }
        if (term_year != null) {
            sql += " AND SBJ_year=?";
        }
        if (term_sem != null) {
            sql += " AND SBJ_sem=?";
        }
        if (name != null) {
            sql += " AND course_cname LIKE ?";
        }
        sql += ")";
        sql += "WHERE RN BETWEEN ? AND ?\n";
        List<CourseResult> CourseResultList = new ArrayList<>();
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
            if (name != null) {
                preparedStatement.setString(i++, "%" + name + "%");
            }
            preparedStatement.setInt(i++, ((page - 1) * RECORD_PER_PAGE) + 1);
            preparedStatement.setInt(i++, ((page) * RECORD_PER_PAGE));

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                CourseResult courseResult = new CourseResult(
                        resultSet.getString("sbj_num"),
                        resultSet.getString("sbj_year"),
                        resultSet.getString("sbj_sem"),
                        resultSet.getString("rgno"),
                        resultSet.getString("regular_score"),
                        resultSet.getString("period1_score"),
                        resultSet.getString("period2_score"),
                        resultSet.getString("final_score"),
                        resultSet.getString("sem_score"),
                        resultSet.getString("sem_star_sign"),
                        resultSet.getString("makeup_score"),
                        resultSet.getString("makeup_star_sign"),
                        resultSet.getString("course_cname"),
                        resultSet.getString("names")
                );
                CourseResultList.add(courseResult);
            }
            return CourseResultList;
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
    public Integer getPageNumber(Integer rgno, String term_year, String term_sem, String name) throws SQLException {

        String sql = "SELECT COUNT(*) FROM (SELECT ROW_NUMBER() OVER ( ) AS RN,I.*\n" +
                "      FROM STU_COURSE_RESULT I\n" +
                "            WHERE 1 = 1";
        if (rgno != null) {
            sql += " AND rgno=?";
        }
        if (term_year != null) {
            sql += " AND SBJ_year=?";
        }
        if (term_sem != null) {
            sql += " AND SBJ_sem=?";
        }
        if (name != null) {
            sql += " AND course_cname LIKE ?";
        }
        sql += ")";
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
            if (name != null) {
                preparedStatement.setString(i++, "%" + name + "%");
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
