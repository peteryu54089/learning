package dbconn;

import config.Config;
import model.HasSchoolSchema;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import util.LogUtility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DbConn {
    private static final Properties props = Config.getDbProperties();
    private static DataSource dataSource = null;

    // public final static String LEARNING_CACHE = "learning_cache";

    @Deprecated
    public static synchronized Connection getConnection() {
        return getConnection(
                props.getProperty("db.database")
        );
    }

    public static synchronized Connection getConnection(HasSchoolSchema dao) {
        return getConnection(dao.getSchoolSchema());
    }

    public static synchronized Connection getConnection(String dbName) {
        try {
            final String dbUserName = props.getProperty("db.user", "USER");
            final String dbPassword = props.getProperty("db.pass");
            if (dataSource == null) {
                initDataSource(dbUserName, dbPassword);
            }
            Connection connection = dataSource.getConnection();
            connection.setSchema(dbName);
            return connection;
        } catch (SQLException e) {
            LogUtility.errorLog("Get DB Connection Error.", e);
            e.printStackTrace();
            return null;
        }
    }

    private static synchronized void initDataSource(String userName, String password) throws SQLException {
        String url = props.getProperty("db.url");
        // "jdbc:db2://140.124.13.173:50000/" + dbName + "?useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC&useSSL=false";
        // "jdbc:db2://140.124.13.173:50000/NTUT";

        String driverClassName = props.getProperty("db.driver", "com.ibm.db2.jcc.DB2Driver");

        try {
            Class.forName(driverClassName);
        } catch (ClassNotFoundException ignored) {
        }

        PoolProperties p = new PoolProperties();
        p.setUrl(url);
        p.setDriverClassName(driverClassName);
        p.setUsername(userName);
        p.setPassword(password);
        p.setJmxEnabled(true);

        p.setTestWhileIdle(Boolean.parseBoolean(props.getProperty("db.testWhileIdle", "true")));
        p.setTestOnBorrow(Boolean.parseBoolean(props.getProperty("db.testOnBorrow", "false")));
        p.setTestOnReturn(Boolean.parseBoolean(props.getProperty("db.testOnReturn", "false")));

        p.setValidationQuery("select 1 from sysibm.sysdummy1");
        p.setValidationInterval(30000);
        p.setTimeBetweenEvictionRunsMillis(30000);
        p.setMaxActive(50);
        p.setInitialSize(5);
        p.setMaxWait(5000);
        p.setRemoveAbandonedTimeout(60);
        p.setMinEvictableIdleTimeMillis(30000);
        p.setMinIdle(2);
        p.setLogAbandoned(true);
        p.setRemoveAbandoned(true);
        p.setJdbcInterceptors("org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;" +
                "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");
        dataSource = new DataSource();
        dataSource.setPoolProperties(p);
    }
}
