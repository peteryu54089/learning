package dao.impl;

import dao.PerformanceSubmitterDao;
import dbconn.DbConn;
import model.Authority;
import model.HasSchoolSchema;
import model.role.PerformanceSubmitter;
import model.role.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Ching Yun Yu on 2018/1/29.
 */
public class PerformanceSubmitterDaoImpl extends BaseDao implements PerformanceSubmitterDao {

    public PerformanceSubmitterDaoImpl(HasSchoolSchema hasSchoolSchemaObject) {
        super(hasSchoolSchemaObject);
    }

    @Override
    public Role getDetailByRegNumber(String regNumber, Authority.RoleIndex roleIndex) throws SQLException {
        return getPerformanceSubmitterByRegNumber(regNumber);
    }

    public PerformanceSubmitter getPerformanceSubmitterByRegNumber(String regNumber) throws SQLException {
        String sql = "SELECT " +
                "REGISTER_NUMBER, " +
                "REGISTER_NUMBER STAFF_CODE " +
                "FROM ACCOUNT WHERE " +
                "SOURCE != ? " +
                "AND BITAND(ROLE_CODE, ?) > 0 " +
                "AND REGISTER_NUMBER = ?";
        PerformanceSubmitter performanceSubmitter = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try (Connection pcon = DbConn.getConnection(this)) {
            preparedStatement = pcon.prepareStatement(sql);
            int i = 0;
            preparedStatement.setString(++i, "STUDENT"); // not equals
            preparedStatement.setInt(++i, Authority.RoleIndex.PERFORMANCE_SUBMITTER.value());
            preparedStatement.setString(++i, regNumber);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                performanceSubmitter = new PerformanceSubmitter(
                        resultSet.getString("register_number"),
                        resultSet.getString("staff_code")
                );
            }
            // return student;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (resultSet != null) {
                resultSet.close();
            }
        }
        return performanceSubmitter;
    }
}
