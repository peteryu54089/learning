package dao;

import model.Account;
import model.role.CadreSubmitter;

import java.sql.SQLException;

public interface CadreSubmitterDao extends RoleDao {
    CadreSubmitter getCadreSubmitterByRegNumber(String register_number) throws SQLException;
}
