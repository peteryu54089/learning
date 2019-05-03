package dao;

import model.Account;
import model.Authority;
import model.ModelList;

import java.sql.SQLException;
import java.util.List;

public interface AccountDao {

    Account getAccountByRegNumber(String regNumber) throws SQLException;

    Account getAccountByIdNumber(String idNumber) throws SQLException;

    List<Account> getAccountByRoleIndex(Authority.RoleIndex roleIndex) throws SQLException;

    List<Account> getAccountByFuzzy(String hint) throws SQLException;

    ModelList<Account> getStaffByNameOrStaffCode(String nameOrStaffCode, int page);

    List<Account> getAccountByFuzzy(String term_year, String term_semester,
                                    String hint) throws SQLException;

    boolean updateToDB(Account account);

    boolean updateRole(Account account);

    int getItemsPerPage();

	Account getAccountByUID(int pUid, int pSbjYear, int pSbjSem) throws SQLException;
}
