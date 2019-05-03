package dao.impl;

import dao.ManagerDao;
import dbconn.DbConn;
import model.Account;
import model.Authority;
import model.HasSchoolSchema;
import model.role.Manager;
import model.role.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by David on 2018/1/29.
 */
public class ManagerDaoImpl extends BaseDao implements ManagerDao {

    public ManagerDaoImpl(HasSchoolSchema hasSchoolSchemaObject) {
        super(hasSchoolSchemaObject);
    }

    public Manager getManagerByAccount(Account account) throws SQLException {
        if (account == null) {
            throw new IllegalArgumentException("傳入Account為null");
        }

        Manager manager = getManagerByRegNumber(account.getRegNumber());

        return manager;
    }


    @Override
    public Manager getManagerByRegNumber(String register_number) throws SQLException {
        String sql = "SELECT " +
                "REGISTER_NUMBER, " +
                "REGISTER_NUMBER STAFF_CODE " +
                "FROM ACCOUNT WHERE " +
                "SOURCE != ? " +
                "AND BITAND(ROLE_CODE, ?) > 0 " +
                "AND REGISTER_NUMBER = ?";
        Manager manager = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try (Connection pcon = DbConn.getConnection(this)) {
            preparedStatement = pcon.prepareStatement(sql);
            int i = 0;
            preparedStatement.setString(++i, "STUDENT"); // not equals
            preparedStatement.setInt(++i, Authority.RoleIndex.MANAGER.value());
            preparedStatement.setString(++i, register_number);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                manager = new Manager(
                        resultSet.getString("REGISTER_NUMBER")
                );
            }
            // return manager;
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
        return manager;
    }

    @Override
    public Role getDetailByRegNumber(String regNumber, Authority.RoleIndex roleIndex) throws SQLException {
        return getManagerByRegNumber(regNumber);
    }
}
