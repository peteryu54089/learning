package dao.impl;

import dao.CadreSubmitterDao;
import dbconn.DbConn;
import model.Account;
import model.Authority;
import model.HasSchoolSchema;
import model.role.CadreSubmitter;
import model.role.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CadreSubmitterDaoImpl extends BaseDao implements CadreSubmitterDao {

    public CadreSubmitterDaoImpl(HasSchoolSchema hasSchoolSchemaObject) {
        super(hasSchoolSchemaObject);
    }

    @Override
    public CadreSubmitter getCadreSubmitterByRegNumber(String register_number) {
        String sql = "SELECT " +
                "REGISTER_NUMBER, " +
                "REGISTER_NUMBER STAFF_CODE " +
                "FROM ACCOUNT WHERE " +
                "SOURCE != ? " +
                "AND BITAND(ROLE_CODE, ?) > 0 " +
                "AND REGISTER_NUMBER = ?";

        CadreSubmitter cadreSubmitter = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try (Connection pcon = DbConn.getConnection(this)) {
            preparedStatement = pcon.prepareStatement(sql);
            int i = 0;
            preparedStatement.setString(++i, "STUDENT"); // not equals
            preparedStatement.setInt(++i, Authority.RoleIndex.CADRE_SUBMITTER.value());
            preparedStatement.setString(++i, register_number);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                cadreSubmitter = new CadreSubmitter(
                        resultSet.getString("register_number"),
                        resultSet.getString("staff_code")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cadreSubmitter;
    }

    private CadreSubmitter createInstance(ResultSet resultSet) throws SQLException {
        return new CadreSubmitter(
                resultSet.getString("REGISTER_NUMBER"),
                resultSet.getString("STAFF_CODE")
        );
    }

    @Override
    public Role getDetailByRegNumber(String regNumber, Authority.RoleIndex roleIndex) throws SQLException {
        return getCadreSubmitterByRegNumber(regNumber);
    }
}
