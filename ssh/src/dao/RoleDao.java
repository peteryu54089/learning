package dao;

import model.Authority;
import model.role.Role;

import java.sql.SQLException;

public interface RoleDao {
    Role getDetailByRegNumber(String regNumber, Authority.RoleIndex roleIndex) throws SQLException;
}
