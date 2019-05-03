package dao;

import model.Account;
import model.role.AttendSubmitter;

import java.sql.SQLException;

public interface AttendSubmitterDao extends RoleDao {
    AttendSubmitter getAttendSubmitterByAccount(Account account) throws SQLException;

    AttendSubmitter getAttendSubmitterByRegNumber(String register_number) throws SQLException;
}
