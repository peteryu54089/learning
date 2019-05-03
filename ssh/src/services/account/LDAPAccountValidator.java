package services.account;

import dao.AccountDao;
import dao.impl.AccountDaoImpl;
import model.Account;
import model.MisSystemData;
import util.MisSystemUtil;

import java.util.Properties;

public class LDAPAccountValidator implements AccountValidator {
    @Override
    public Account verify(Properties props) {
        final String schSchema = getSchema(props);
        AccountDao accountDao = new AccountDaoImpl(() -> schSchema);

        try {
            MisSystemData misSystemData = MisSystemUtil.getStuAffairsSbjYS(schSchema);    // 學務系統學年期
            Integer uid = Integer.valueOf(props.getProperty("uid"));
            Account account = accountDao.getAccountByUID(uid, misSystemData.getSbj_year(), misSystemData.getSbj_sem());
            // 檢查帳號是否存在
            if (account == null || !account.isExist()) {
                return null;
            }

            account.setSchoolSchema(schSchema);

            return account;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getSchema(Properties props) {
        String sid = props.getProperty("sid");
        final String preSchema = "LRN";
        return preSchema + sid;
    }
}
