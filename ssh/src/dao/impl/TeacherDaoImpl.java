package dao.impl;

import dao.TeacherDao;
import dbconn.DbConn;
import model.Authority;
import model.HasSchoolSchema;
import model.role.Role;
import model.role.Teacher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by David on 2018/1/29.
 */
public class TeacherDaoImpl extends BaseDao implements TeacherDao {
    public TeacherDaoImpl(HasSchoolSchema hasSchoolSchemaObject) {
        super(hasSchoolSchemaObject);
    }

    @Override
    public Role getDetailByRegNumber(String regNumber, Authority.RoleIndex roleIndex) throws SQLException {
        return getTeacherByRegNumber(regNumber);
    }

    @Override
    public Teacher getTeacherByRegNumber(String regNumber) throws SQLException {
        String sql = "SELECT " +
                "REGISTER_NUMBER, " +
                "REGISTER_NUMBER STAFF_CODE " +
                "FROM ACCOUNT WHERE " +
                "SOURCE != ? " +
                "AND BITAND(ROLE_CODE, ?) > 0 " +
                "AND REGISTER_NUMBER = ?";
        Teacher teacher = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try (Connection pcon = DbConn.getConnection(this)) {
            preparedStatement = pcon.prepareStatement(sql);
            int i = 0;
            preparedStatement.setString(++i, "STUDENT"); // not equals
            preparedStatement.setInt(++i, Authority.RoleIndex.TEACHER.value());
            preparedStatement.setString(++i, regNumber);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                teacher = new Teacher(
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
        return teacher;
    }
}
