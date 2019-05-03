package services.account;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import config.Config;
import model.Account;
import model.MisSystemData;

import java.util.Properties;

public interface AccountValidator {
    @Nullable
    Account verify(Properties props);

    @NotNull
    String getSchema(Properties props);

    static AccountValidator getInstance() {
        if (Config.getLoginMethod() == Config.LoginMethod.DEV) {
            return new DevAccountValidator();
        }
        return new LDAPAccountValidator();
    }
}
