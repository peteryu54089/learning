package dao.impl;

import dao.TutorDao;
import dbconn.DbConn;
import model.Authority;
import model.HasSchoolSchema;
import model.role.Role;
import model.role.Tutor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TutorDaoImpl extends BaseDao implements TutorDao {

    public TutorDaoImpl(HasSchoolSchema hasSchoolSchemaObject) {
        super(hasSchoolSchemaObject);
    }

    @Override
    public Tutor getTutorByRegNumber(String regNumber) throws SQLException {
        String sql = "SELECT " +
                "REGISTER_NUMBER, " +
                "REGISTER_NUMBER STAFF_CODE " +
                "FROM ACCOUNT WHERE " +
                "SOURCE != ? " +
                "AND BITAND(ROLE_CODE, ?) > 0 " +
                "AND REGISTER_NUMBER = ?";
        Tutor tutor = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try (Connection pcon = DbConn.getConnection(this)) {
            preparedStatement = pcon.prepareStatement(sql);
            int i = 0;
            preparedStatement.setString(++i, "STUDENT"); // not equals
            preparedStatement.setInt(++i, Authority.RoleIndex.TUTOR.value());
            preparedStatement.setString(++i, regNumber);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                tutor = new Tutor(
                        resultSet.getString("register_number"),
                        resultSet.getString("staff_code")
                );
            }
            // return tutor;
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
        return tutor;
    }

    @Override
    public Role getDetailByRegNumber(String regNumber, Authority.RoleIndex roleIndex) throws SQLException {
        return getTutorByRegNumber(regNumber);
    }
}
