package dao;

import model.Account;
import model.role.Manager;

import java.sql.SQLException;
import java.util.List;

public interface ManagerDao extends RoleDao {

    Manager getManagerByAccount(Account account) throws SQLException;

    Manager getManagerByRegNumber(String register_number) throws SQLException;
}
