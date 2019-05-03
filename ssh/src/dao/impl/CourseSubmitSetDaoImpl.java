package dao.impl;

import dao.CourseSubmitSetDao;
import dbconn.DbConn;
import model.CourseSubmitSet;
import model.HasSchoolSchema;

import java.sql.*;

/**
 * Created by Ching-Yun Yu on 2018/5/31.
 */
public class CourseSubmitSetDaoImpl extends BaseDao implements CourseSubmitSetDao {
    public CourseSubmitSetDaoImpl(HasSchoolSchema hasSchoolSchemaObject) {
        super(hasSchoolSchemaObject);
    }


    @Override
    public CourseSubmitSet getCourseSubmitSet() throws SQLException {
        String tableName = " COURSE_SUBMIT_SET";

        String sql = "SELECT * FROM  COURSE_SUBMIT_SET WHERE id = 1";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try (Connection pcon = DbConn.getConnection(this)) {
            DatabaseMetaData dmd = pcon.getMetaData();
            try (ResultSet rs = dmd.getTables(null, null, tableName, null)) {
                try (Statement stmt = pcon.createStatement()) {
                    if (!rs.next()) {
                        stmt.executeUpdate("CREATE TABLE  COURSE_SUBMIT_SET" + "(id VARCHAR(30), text1 VARCHAR(30), text2 VARCHAR(30), text3 VARCHAR(30), text4 VARCHAR(30), text5 VARCHAR(30))");
                        //stmt.executeUpdate("alter table  COURSE_SUBMIT_SET add primary key(id, text1, text2, text3, text4, text5);");
                    }
                }
            }

            preparedStatement = pcon.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            CourseSubmitSet courseSubmitSet = null;
            if (resultSet.next()) {
                courseSubmitSet = new CourseSubmitSet(
                        resultSet.getString("text1"),
                        resultSet.getString("text2"),
                        resultSet.getString("text3"),
                        resultSet.getString("text4"),
                        resultSet.getString("text5")
                );
            }
            return courseSubmitSet;
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
    public void setCourseSubmitSet(CourseSubmitSet courseSubmitSet) throws SQLException {
        String sql = "UPDATE  COURSE_SUBMIT_SET SET text1 = ?, text2 = ?, text3 = ?, text4 = ?, text5 = ? WHERE id = 1";
        PreparedStatement preparedStatement = null;
        try (Connection pcon = DbConn.getConnection(this)) {
            preparedStatement = pcon.prepareStatement(sql);
            int i = 1;
            preparedStatement.setString(i++, courseSubmitSet.getText1());
            preparedStatement.setString(i++, courseSubmitSet.getText2());
            preparedStatement.setString(i++, courseSubmitSet.getText3());
            preparedStatement.setString(i++, courseSubmitSet.getText4());
            preparedStatement.setString(i++, courseSubmitSet.getText5());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null)
                preparedStatement.close();
        }
    }

    @Override
    public void insertCourseSubmitSet(CourseSubmitSet courseSubmitSet) throws SQLException {
        String sql = "INSERT INTO  COURSE_SUBMIT_SET(id, text1, text2, text3, text4, text5) VALUES (1, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = null;
        try (Connection pcon = DbConn.getConnection(this)) {
            preparedStatement = pcon.prepareStatement(sql);
            int i = 1;
            preparedStatement.setString(i++, courseSubmitSet.getText1());
            preparedStatement.setString(i++, courseSubmitSet.getText2());
            preparedStatement.setString(i++, courseSubmitSet.getText3());
            preparedStatement.setString(i++, courseSubmitSet.getText4());
            preparedStatement.setString(i++, courseSubmitSet.getText5());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null)
                preparedStatement.close();
        }
    }
}
