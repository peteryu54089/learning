package dao.impl;

import dao.WorkTeamDao;
import dbconn.DbConn;
import model.Account;
import model.Authority;
import model.HasSchoolSchema;
import model.role.Role;
import model.role.WorkTeam;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WorkTeamDaoImpl extends BaseDao implements WorkTeamDao {

    public WorkTeamDaoImpl(HasSchoolSchema hasSchoolSchemaObject) {
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
                preparedStatement.setInt(++i, Authority.RoleIndex.WORK_TEAM.value());
                preparedStatement.setString(++i, regNumber);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    return createInstance(resultSet);
                } else {
                    throw new NullPointerException("USER NOT FOUND");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public WorkTeam getWorkTeamByAccount(Account account) throws SQLException {
        if (account == null) {
            throw new IllegalArgumentException("傳入Account為null");
        }
        return getWorkTeamByRegNumber(account.getRegNumber());
    }

    @Override
    public WorkTeam getWorkTeamByRegNumber(String register_number) {
        return getWorkTeamByRegNumber(register_number);
    }

    private WorkTeam createInstance(ResultSet resultSet) throws SQLException {
        return new WorkTeam(
                resultSet.getString("REGISTER_NUMBER"),
                resultSet.getString("STAFF_CODE")
        );
    }
}
