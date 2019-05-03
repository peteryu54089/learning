package legacy.jdbc.db2;

import com.ibm.db2.jcc.t4.b;

import java.sql.*;
import java.util.Properties;
import java.util.logging.Logger;

public class Db2forJccDriverWrapper implements Driver {
    private static final String PREFIX = "jdbc:db2_legacy_wrapper";
    private static final String PREFIX_REPLACEMENT = "jdbc:db2";

    static {
        try {
            DriverManager.registerDriver(new Db2forJccDriverWrapper());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private com.ibm.db2.jcc.DB2Driver _driverWrapper = new com.ibm.db2.jcc.DB2Driver();

    @Override
    public Connection connect(String url, Properties info) throws SQLException {
        url = replaceUrlPrefix(url);
        return new Db2forJccConnectionWrapper((b) _driverWrapper.connect(url, info));
    }

    @Override
    public boolean acceptsURL(String url) throws SQLException {
        if (url.startsWith(PREFIX)) {
            url = replaceUrlPrefix(url);
            return _driverWrapper.acceptsURL(url);
        }
        return false;
    }

    @Override
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
        url = replaceUrlPrefix(url);
        return _driverWrapper.getPropertyInfo(url, info);
    }

    @Override
    public int getMajorVersion() {
        return _driverWrapper.getMajorVersion();
    }

    @Override
    public int getMinorVersion() {
        return _driverWrapper.getMinorVersion();
    }

    @Override
    public boolean jdbcCompliant() {
        return false;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new SQLFeatureNotSupportedException();
    }

    private String replaceUrlPrefix(String url) {
        if (url.startsWith(PREFIX)) {
            return PREFIX_REPLACEMENT + url.substring(PREFIX.length());
        }

        return url;
    }
}
