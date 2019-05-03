package config;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class Config {
    private static Properties _dbProps = null;
    private static Properties _appProps = null;
    private static Properties _mailProps = null;

    private Config() {
    }

    public enum LoginMethod {
        DEV, LDAP
    }

    public static LoginMethod getLoginMethod() {
        return LoginMethod.valueOf(getAppProperties().getProperty("app.login_method", "dev").toUpperCase());
    }

    public static Properties getDbProperties() {
        if (_dbProps != null) return _dbProps;
        _dbProps = getPropsFromFile("db.properties");
        return _dbProps;
    }

    public static Properties getMailProperties() {
        if (_mailProps != null) return _mailProps;
        _mailProps = getPropsFromFile("mail.properties");
        return _mailProps;
    }


    public static Properties getAppProperties() {
        if (_appProps != null) return _appProps;
        _appProps = getPropsFromFile("app.properties");
        return _appProps;
    }

    private static Properties getPropsFromFile(String name) {
        Properties p = new Properties();
        try (InputStream is = Config.class.getResourceAsStream(name)) {
            try (InputStreamReader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
                p.load(reader);

                return p;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void clearPropsCache() {
        _dbProps = null;
        _appProps = null;
        _mailProps = null;
    }

    public static final String STUDENT_UPLOADED_FILE_DIR = "stu_learning_files";
    public static final  String COUNSEL_UPLOADED_FILE_DIR="counsel_record_files";
    public static final  String STU_PHTOT_PATH="stu_photo";
}
