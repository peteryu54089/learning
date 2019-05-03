package services.account;

import config.Config;
import dao.AccountDao;
import dao.impl.AccountDaoImpl;
import model.Account;
import model.MisSystemData;

import java.sql.SQLException;
import java.util.Properties;

public class DevAccountValidator implements AccountValidator {
    @Override
    public Account verify(Properties props) {
        String userName = props.getProperty("username");
        String password = props.getProperty("password");

        final String testDb = Config.getDbProperties().getProperty("db.database", "LEARNING");
        AccountDao accountDao = new AccountDaoImpl(() -> testDb);
        try {
            Account account = accountDao.getAccountByRegNumber(userName);
            // 檢查帳號是否存在
            if (account == null || !account.isExist()) {
                return null;
            }

            account.setSchoolSchema(testDb);

            return account;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getSchema(Properties props) {
        return Config.getDbProperties().getProperty("db.database", "LEARNING");
    }
}
