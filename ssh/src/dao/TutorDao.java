package dao;

import model.role.Tutor;

import java.sql.SQLException;

public interface TutorDao extends RoleDao {
    Tutor getTutorByRegNumber(String regNumber) throws SQLException;
}
