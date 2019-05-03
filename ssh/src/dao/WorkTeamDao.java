package dao;

import model.Account;
import model.role.WorkTeam;

import java.sql.SQLException;

public interface WorkTeamDao extends RoleDao {

    WorkTeam getWorkTeamByAccount(Account account) throws SQLException;

    WorkTeam getWorkTeamByRegNumber(String register_number) throws SQLException;
}
