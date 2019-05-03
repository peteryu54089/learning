package dao.impl;

import dao.AdminDao;
import dao.AttendSubmitterDao;
import dbconn.DbConn;
import model.Account;
import model.Authority;
import model.HasSchoolSchema;
import model.role.Admin;
import model.role.AttendSubmitter;
import model.role.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminDaoImpl extends BaseDao implements AdminDao {

    public AdminDaoImpl(HasSchoolSchema hasSchoolSchemaObject) {
        super(hasSchoolSchemaObject);
    }

    @Override
    public Role getDetailByRegNumber(String regNumber, Authority.RoleIndex roleIndex) throws SQLException {
        String sql = "SELECT " +
                "REGISTER_NUMBER " +
                "FROM ACCOUNT WHERE " +
                "SOURCE = ? " +
                "AND BITAND(ROLE_CODE, ?) > 0 " +
                "AND REGISTER_NUMBER = ?";

        try (Connection pcon = DbConn.getConnection(this)) {
            try (PreparedStatement preparedStatement = pcon.prepareStatement(sql)) {
                int i = 0;
                preparedStatement.setString(++i, "EXTRA"); // not equals
                preparedStatement.setInt(++i, Authority.RoleIndex.ADMIN.value());
                preparedStatement.setString(++i, regNumber);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    return createInstance(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private Admin createInstance(ResultSet resultSet) throws SQLException {
        return new Admin(
                resultSet.getString("REGISTER_NUMBER")
        );
    }
}
