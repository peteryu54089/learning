package dao.impl;

import dao.PerformanceSubmitSetDao;
import dbconn.DbConn;
import model.HasSchoolSchema;
import model.PerformanceSubmitSet;

import java.sql.*;

/**
 * Created by Ching-Yun Yu on 2018/5/31.
 */
public class PerformanceSubmitSetDaoImpl extends BaseDao implements PerformanceSubmitSetDao {

    public PerformanceSubmitSetDaoImpl(HasSchoolSchema hasSchoolSchemaObject) {
        super(hasSchoolSchemaObject);
    }

    @Override
    public PerformanceSubmitSet getPerformanceSubmitSet() throws SQLException {
        String tableName = "COURSE_SUBMIT_SET";

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try (Connection pcon = DbConn.getConnection(this)) {
            DatabaseMetaData dmd = pcon.getMetaData();
            ResultSet rs = dmd.getTables(null, null, tableName, null);
            Statement stmt = pcon.createStatement();
            if (!rs.next()) {
                stmt.executeUpdate("CREATE TABLE COURSE_SUBMIT_SET" + "(id VARCHAR(30), text1 VARCHAR(30), text2 VARCHAR(30), text3 VARCHAR(30), text4 VARCHAR(30), text5 VARCHAR(30))");
                //stmt.executeUpdate("alter table COURSE_SUBMIT_SET add primary key(id, text1, text2, text3, text4, text5);");
            }
            stmt.close();

            String sql = "SELECT * FROM COURSE_SUBMIT_SET WHERE id = 2";

            preparedStatement = pcon.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            PerformanceSubmitSet performanceSubmitSet = null;
            if (resultSet.next()) {
                performanceSubmitSet = new PerformanceSubmitSet(
                        resultSet.getString("text1"),
                        resultSet.getString("text2"),
                        resultSet.getString("text3"),
                        resultSet.getString("text4"),
                        resultSet.getString("text5")
                );
            }
            return performanceSubmitSet;
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
    public void setPerformanceSubmitSet(PerformanceSubmitSet performanceSubmitSet) throws SQLException {
        String sql = "UPDATE COURSE_SUBMIT_SET SET text1 = ?, text2 = ?, text3 = ?, text4 = ?, text5 = ? WHERE id = 2";
        PreparedStatement preparedStatement = null;
        try (Connection pcon = DbConn.getConnection(this)) {
            preparedStatement = pcon.prepareStatement(sql);
            int i = 1;
            preparedStatement.setString(i++, performanceSubmitSet.getText1());
            preparedStatement.setString(i++, performanceSubmitSet.getText2());
            preparedStatement.setString(i++, performanceSubmitSet.getText3());
            preparedStatement.setString(i++, performanceSubmitSet.getText4());
            preparedStatement.setString(i++, performanceSubmitSet.getText5());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null)
                preparedStatement.close();
        }
    }

    @Override
    public void insertPerformanceSubmitSet(PerformanceSubmitSet performanceSubmitSet) throws SQLException {
        String sql = "INSERT INTO COURSE_SUBMIT_SET(id, text1, text2, text3, text4, text5) VALUES (2, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = null;
        try (Connection pcon = DbConn.getConnection(this)) {
            preparedStatement = pcon.prepareStatement(sql);
            int i = 1;
            preparedStatement.setString(i++, performanceSubmitSet.getText1());
            preparedStatement.setString(i++, performanceSubmitSet.getText2());
            preparedStatement.setString(i++, performanceSubmitSet.getText3());
            preparedStatement.setString(i++, performanceSubmitSet.getText4());
            preparedStatement.setString(i++, performanceSubmitSet.getText5());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null)
                preparedStatement.close();
        }
    }
}
