package dao.impl;

import dao.AttendSubmitterDao;
import dbconn.DbConn;
import model.Account;
import model.Authority;
import model.HasSchoolSchema;
import model.role.AttendSubmitter;
import model.role.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AttendSubmitterDaoImpl extends BaseDao implements AttendSubmitterDao {

    public AttendSubmitterDaoImpl(HasSchoolSchema hasSchoolSchemaObject) {
        super(hasSchoolSchemaObject);
    }

    @Override
    public Role getDetailByRegNumber(String regNumber, Authority.RoleIndex roleIndex) throws SQLException {
        String sql = "SELECT " +
                "REGISTER_NUMBER, " +
                "REGISTER_NUMBER STAFF_CODE " +
                "FROM ACCOUNT WHERE " +
                "SOURCE != ? " +
                "AND BITAND(ROLE_CODE, ?) > 0 " +
                "AND REGISTER_NUMBER = ?";

        try (Connection pcon = DbConn.getConnection(this)) {
            try (PreparedStatement preparedStatement = pcon.prepareStatement(sql)) {
                int i = 0;
                preparedStatement.setString(++i, "STUDENT"); // not equals
                preparedStatement.setInt(++i, Authority.RoleIndex.ATTEND_SUBMITTER.value());
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

    @Override
    public AttendSubmitter getAttendSubmitterByAccount(Account account) throws SQLException {
        if (account == null) {
            throw new IllegalArgumentException("傳入Account為null");
        }
        return getAttendSubmitterByRegNumber(account.getRegNumber());
    }

    @Override
    public AttendSubmitter getAttendSubmitterByRegNumber(String register_number) {
        return getAttendSubmitterByRegNumber(register_number);
    }

    private AttendSubmitter createInstance(ResultSet resultSet) throws SQLException {
        return new AttendSubmitter(
                resultSet.getString("REGISTER_NUMBER"),
                resultSet.getString("STAFF_CODE")
        );
    }
}
