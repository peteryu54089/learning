package dao.impl;

import dao.AccountDao;
import dbconn.DbConn;
import model.Account;
import model.Authority;
import model.HasSchoolSchema;
import model.ModelList;
import util.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccountDaoImpl extends BaseDao implements AccountDao {
    protected int itemsPerPage = 15;
    // private Account account;

    public AccountDaoImpl(HasSchoolSchema hasSchoolSchemaObject) {
        super(hasSchoolSchemaObject);
    }

    // public AccountDaoImpl(Account account) {
    //   this.account = account;
    // }

    @Override
    public Account getAccountByRegNumber(String regNumber) throws SQLException {
        String sql = "SELECT * FROM ACCOUNT WHERE REGISTER_NUMBER = ?";
        Account account = getAccountByOneParameter(sql, regNumber);
        return account;
    }

    @Override
    public Account getAccountByIdNumber(String idNumber) throws SQLException {
        String sql = "SELECT * FROM ACCOUNT WHERE ID_NUMBER = ?";
        Account account = getAccountByOneParameter(sql, idNumber);
        return account;
    }

    @Override
    public List<Account> getAccountByFuzzy(String hint) throws SQLException {
        List<Account> accountList = new ArrayList<>();
        String sql = "SELECT * FROM ACCOUNT " +
                "WHERE (REGISTER_NUMBER LIKE ? OR TERM_YEAR LIKE ? OR TERM_SEMESTER LIKE ? OR NAME LIKE ? OR ENAME LIKE ? OR ID_NUMBER LIKE ? OR EMAIL LIKE ? OR MOBILE_TELNO LIKE ? )";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try (Connection pcon = DbConn.getConnection(this)) {
            preparedStatement = pcon.prepareStatement(sql);
            // int i = 1;
            for (int i = 1; i <= 8; i++) {
                preparedStatement.setString(i++, "%" + hint + "%");
            }
            // preparedStatement.setString(i++, "%" + hint + "%");
            // preparedStatement.setString(i++, "%" + hint + "%");
            // preparedStatement.setString(i++, "%" + hint + "%");
            // preparedStatement.setString(i++, "%" + hint + "%");
            // preparedStatement.setString(i++, "%" + hint + "%");
            // preparedStatement.setString(i++, "%" + hint + "%");
            // preparedStatement.setString(i, "%" + hint + "%");
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Account account = convertRsToInstance(resultSet);
                accountList.add(account);
            }
            return accountList;
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
        return null;
    }

    @Override
    public ModelList<Account> getStaffByNameOrStaffCode(String nameOrStaffCode, int page) {
        page--;
        String sql = "" +
                "SELECT * FROM (" +
                "   SELECT " +
                "       ROW_NUMBER() OVER (ORDER BY REGISTER_NUMBER ASC) - 1 AS RN," +
                "       ACCOUNT.* " +
                "   FROM ACCOUNT WHERE SOURCE = ? AND (NAME LIKE ? OR REGISTER_NUMBER = ?)" +
                ") WHERE RN BETWEEN ? AND ?";
        String sqlCnt = "SELECT COUNT(*) FROM ACCOUNT WHERE SOURCE = ? AND (NAME LIKE ? OR REGISTER_NUMBER = ?)";
        int start = page * itemsPerPage;
        int end = start + itemsPerPage - 1;
        int total = 0;
        List<Account> list = new ArrayList<>(itemsPerPage);

        try (Connection pcon = DbConn.getConnection(this)) {
            try (
                    PreparedStatement ps = pcon.prepareStatement(sql);
                    PreparedStatement ps2 = pcon.prepareStatement(sqlCnt)
            ) {
                int i = 0;

                ps.setString(++i, "STAFF");
                ps.setString(++i, "%" + nameOrStaffCode + "%");
                ps.setString(++i, nameOrStaffCode);
                ps.setInt(++i, start);
                ps.setInt(++i, end);

                int j = 0;
                ps2.setString(++j, "STAFF");
                ps2.setString(++j, "%" + nameOrStaffCode + "%");
                ps2.setString(++j, nameOrStaffCode);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Account account = convertRsToInstance(rs);
                        account.setSeqNo(rs.getInt("RN") + 1);
                        list.add(account);
                    }
                }

                try (ResultSet rs = ps2.executeQuery()) {
                    if (rs.next()) {
                        total = rs.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ModelList<>(list, total);
    }

    @Override
    public List<Account> getAccountByRoleIndex(Authority.RoleIndex roleIndex) throws SQLException {
        List<Account> accountList = new ArrayList<>();
        String sql = "SELECT * FROM ACCOUNT WHERE BITAND(ROLE_CODE,?) = ?";
        try (Connection pcon = DbConn.getConnection(this); PreparedStatement preparedStatement = pcon.prepareStatement(sql)) {
            int i = 0;
            preparedStatement.setInt(++i, roleIndex.value());
            preparedStatement.setInt(++i, roleIndex.value());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Account account = convertRsToInstance(resultSet);
                    accountList.add(account);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return accountList;
    }

    @Override
    public List<Account> getAccountByFuzzy(String term_year, String term_semester, String hint)
            throws SQLException {
        List<Account> accountList = new ArrayList<>();
        String sql = "SELECT * FROM ACCOUNT WHERE TERM_YEAR LIKE ? AND TERM_SEMESTER LIKE ? AND (REGISTER_NUMBER LIKE ? OR TERM_YEAR LIKE ? OR TERM_SEMESTER LIKE ? OR NAME LIKE ? OR ENAME LIKE ? OR ID_NUMBER LIKE ? OR EMAIL LIKE ? OR MOBILE_TELNO LIKE ? )";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try (Connection pcon = DbConn.getConnection(this)) {
            preparedStatement = pcon.prepareStatement(sql);
            int i = 1;
            preparedStatement.setString(i++, "%" + term_year + "%");
            preparedStatement.setString(i++, "%" + term_semester + "%");
            for (; i <= 8; i++) {
                preparedStatement.setString(i++, "%" + hint + "%");
            }
            // preparedStatement.setString(i++, "%" + hint + "%");
            // preparedStatement.setString(i++, "%" + hint + "%");
            // preparedStatement.setString(i++, "%" + hint + "%");
            // preparedStatement.setString(i++, "%" + hint + "%");
            // preparedStatement.setString(i++, "%" + hint + "%");
            // preparedStatement.setString(i++, "%" + hint + "%");
            // preparedStatement.setString(i, "%" + hint + "%");
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Account account = convertRsToInstance(resultSet);
                accountList.add(account);
            }
            // return accountList;
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
        return accountList;
    }

    @Override
    public boolean updateToDB(Account account) {
        String sql = null;
        List<String> data = new ArrayList<>();
        switch (account.getSource()) {
            case "STUDENT": {
                String idNo = account.getIdNumber();
                if (StringUtils.isNullOrEmpty(idNo))
                    throw new IllegalArgumentException("id number is null or empty");

                sql = "MERGE INTO EXTRA_STU_DATA AS T\n" +
                        "USING (VALUES\n" +
                        "         (?, ?, ?, ?)\n" +
                        ") AS merge (ID_NUMBER, EMAIL, MOBILE, AVATAR)\n" +
                        "ON T.ID_NUMBER = merge.ID_NUMBER\n" +
                        "WHEN MATCHED THEN\n" +
                        "  UPDATE\n" +
                        "  SET T.EMAIL  = merge.EMAIL,\n" +
                        "      T.MOBILE = merge.MOBILE,\n" +
                        "      T.AVATAR = merge.AVATAR\n" +
                        "WHEN NOT MATCHED THEN\n" +
                        "  INSERT (ID_NUMBER, EMAIL, MOBILE, AVATAR)\n" +
                        "  VALUES (merge.ID_NUMBER, merge.EMAIL, merge.MOBILE, merge.AVATAR)";
                data.add(account.getIdNumber());
                data.add(account.getEmail());
                data.add(account.getMobileTelNo());
                data.add(account.getAvatar());
            }
            break;
            case "STAFF": {
                sql = "MERGE INTO EXTRA_STAFF_DATA AS T\n" +
                        "USING (VALUES\n" +
                        "         (?, ?, ?, ?)\n" +
                        ") AS merge (STAFF_CODE, EMAIL, MOBILE, AVATAR)\n" +
                        "ON T.STAFF_CODE = merge.STAFF_CODE\n" +
                        "WHEN MATCHED THEN\n" +
                        "  UPDATE\n" +
                        "  SET T.EMAIL  = merge.EMAIL,\n" +
                        "      T.MOBILE = merge.MOBILE,\n" +
                        "      T.AVATAR = merge.AVATAR\n" +
                        "WHEN NOT MATCHED THEN\n" +
                        "  INSERT (STAFF_CODE, EMAIL, MOBILE, AVATAR)\n" +
                        "  VALUES (merge.STAFF_CODE, merge.EMAIL, merge.MOBILE, merge.AVATAR)";
                data.add(account.getRegNumber());
                data.add(account.getEmail());
                data.add(account.getMobileTelNo());
                data.add(account.getAvatar());
            }
            default:
                throw new UnsupportedOperationException();
        }

        if (sql != null) {
            try (Connection pcon = DbConn.getConnection(this)) {
                try (PreparedStatement ps = pcon.prepareStatement(sql)) {
                    for (int i = data.size() - 1; i >= 0; i++)
                        ps.setString(i + 1, data.get(i));

                    return ps.executeUpdate() >= 0;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    @Override
    public boolean updateRole(Account account) {
        String key;
        String sql = "MERGE INTO ROLE_EXTEND AS R\n" +
                "USING (VALUES\n" +
                "         (?, ?, ?)\n" +
                ") AS merge (SOURCE, KEY, ROLE_VALUE)\n" +
                "ON R.SOURCE = merge.SOURCE AND R.KEY = merge.KEY\n" +
                "WHEN MATCHED THEN\n" +
                "  UPDATE\n" +
                "  SET R.ROLE_VALUE = merge.ROLE_VALUE\n" +
                "WHEN NOT MATCHED THEN\n" +
                "  INSERT (SOURCE, KEY, ROLE_VALUE)\n" +
                "  VALUES (merge.SOURCE, merge.KEY, merge.ROLE_VALUE)";
        String source = account.getSource();

        switch (account.getSource()) {
            case "STUDENT":
                key = account.getIdNumber();
                break;
            case "STAFF":
                key = account.getRegNumber();
                break;
            case "EXTRA":
                key = account.getRegNumber();
                break;
            default:
                throw new UnsupportedOperationException("Role not support");
        }

        try (Connection pcon = DbConn.getConnection(this)) {
            try (PreparedStatement ps = pcon.prepareStatement(sql)) {
                int i = 0;
                ps.setString(++i, source);
                ps.setString(++i, key);
                ps.setInt(++i, account.getAuthority().getRoleCode());
                return ps.executeUpdate() >= 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public int getItemsPerPage() {
        return itemsPerPage;
    }

    private Account getAccountByOneParameter(String sql, String parameter) throws SQLException {
        Account account = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try (Connection pcon = DbConn.getConnection(this)) {
            preparedStatement = pcon.prepareStatement(sql);
            preparedStatement.setString(1, parameter);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                account = convertRsToInstance(resultSet);
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
        return account;
    }

    private Account convertRsToInstance(final ResultSet resultSet) throws SQLException {
        return new Account(
                this,
                resultSet.getString("REGISTER_NUMBER"),
                resultSet.getInt("TERM_YEAR"),
                resultSet.getInt("TERM_SEMESTER"),
                resultSet.getString("PASSWORD"),
                resultSet.getString("NAME"),
                resultSet.getString("ENAME"),
                resultSet.getString("ID_NUMBER"),
                resultSet.getString("EMAIL"),
                resultSet.getString("MOBILE_TELNO"),
                resultSet.getDate("BIRTHDAY"),
                resultSet.getString("SOURCE"),
                resultSet.getInt("ROLE_CODE"),
                resultSet.getString("AVATAR")
        );
    }

    // public Account getAccount() {
    //   return account;
    // }
    //
    // public void setAccount(Account account) {
    //   this.account = account;
    // }

    @Override
    public Account getAccountByUID(int pUid, int pSbjYear, int pSbjSem) throws SQLException {
        Account account = null;

        String sql = "SELECT * FROM ACCOUNT " +
                "WHERE pk = ? AND term_year = ? AND term_semester = ? ";

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try (Connection pcon = DbConn.getConnection(this)) {
            preparedStatement = pcon.prepareStatement(sql);

            preparedStatement.setInt(1, pUid);
            preparedStatement.setInt(2, pSbjYear);
            preparedStatement.setInt(3, pSbjSem);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                account = convertRsToInstance(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (resultSet != null) {
                resultSet.close();
            }
        }
        return account;
    }
}
