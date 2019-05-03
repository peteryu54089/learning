package legacy.jdbc.db2;

import com.ibm.db2.jcc.DB2ConnectionlessClob;
import com.ibm.db2.jcc.am.io;

import java.sql.*;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

public class Db2forJccConnectionWrapper implements Connection {
    private final com.ibm.db2.jcc.t4.b c;

    public Db2forJccConnectionWrapper(com.ibm.db2.jcc.t4.b connection) throws SQLException {
        c = connection;
    }

    @Override
    public Statement createStatement() throws SQLException {
        return c.createStatement();
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return new Db2forJccPreparedStatementWrapper((io) c.prepareStatement(sql));
    }

    @Override
    public CallableStatement prepareCall(String sql) throws SQLException {
        return c.prepareCall(sql);
    }

    @Override
    public String nativeSQL(String sql) throws SQLException {
        return c.nativeSQL(sql);
    }

    @Override
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        c.setAutoCommit(autoCommit);
    }

    @Override
    public boolean getAutoCommit() throws SQLException {
        return c.getAutoCommit();
    }

    @Override
    public void commit() throws SQLException {
        c.commit();
    }

    @Override
    public void rollback() throws SQLException {
        c.rollback();
    }

    @Override
    public void close() throws SQLException {
        c.close();
    }

    @Override
    public boolean isClosed() throws SQLException {
        return c.isClosed();
    }

    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        return c.getMetaData();
    }

    @Override
    public void setReadOnly(boolean readOnly) throws SQLException {
        c.setReadOnly(readOnly);
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        return c.isReadOnly();
    }

    @Override
    public void setCatalog(String catalog) throws SQLException {
        c.setCatalog(catalog);
    }

    @Override
    public String getCatalog() throws SQLException {
        return c.getCatalog();
    }

    @Override
    public void setTransactionIsolation(int level) throws SQLException {
        c.setTransactionIsolation(level);
    }

    @Override
    public int getTransactionIsolation() throws SQLException {
        return c.getTransactionIsolation();
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return c.getWarnings();
    }

    @Override
    public void clearWarnings() throws SQLException {
        c.clearWarnings();
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        return c.createStatement(resultSetType, resultSetConcurrency);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return new Db2forJccPreparedStatementWrapper((io) c.prepareStatement(sql, resultSetType, resultSetConcurrency));
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return c.prepareCall(sql, resultSetType, resultSetConcurrency);
    }

    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        return c.getTypeMap();
    }

    @Override
    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        c.setTypeMap(map);
    }

    @Override
    public void setHoldability(int holdability) throws SQLException {
        c.setHoldability(holdability);
    }

    @Override
    public int getHoldability() throws SQLException {
        return c.getHoldability();
    }

    @Override
    public Savepoint setSavepoint() throws SQLException {
        return c.setSavepoint();
    }

    @Override
    public Savepoint setSavepoint(String name) throws SQLException {
        return c.setSavepoint(name);
    }

    @Override
    public void rollback(Savepoint savepoint) throws SQLException {
        c.rollback(savepoint);
    }

    @Override
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        c.releaseSavepoint(savepoint);
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return c.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return new Db2forJccPreparedStatementWrapper((io) c.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability));
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        throw new NotImplementedException();
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        return new Db2forJccPreparedStatementWrapper((io) c.prepareStatement(sql, autoGeneratedKeys));
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        return new Db2forJccPreparedStatementWrapper((io) c.prepareStatement(sql, columnIndexes));
    }

    @Override
    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        return new Db2forJccPreparedStatementWrapper((io) c.prepareStatement(sql, columnNames));
    }

    @Override
    public Clob createClob() throws SQLException {
        return new DB2ConnectionlessClob("");
    }

    @Override
    public Blob createBlob() throws SQLException {
        throw new NotImplementedException();
    }

    @Override
    public NClob createNClob() throws SQLException {
        throw new NotImplementedException();
    }

    @Override
    public SQLXML createSQLXML() throws SQLException {
        throw new NotImplementedException();
    }

    @Override
    public boolean isValid(int timeout) throws SQLException {
        throw new NotImplementedException();
    }

    @Override
    public void setClientInfo(String name, String value) throws SQLClientInfoException {
    }

    @Override
    public void setClientInfo(Properties properties) throws SQLClientInfoException {
    }

    @Override
    public String getClientInfo(String name) throws SQLException {
        throw new NotImplementedException();
    }

    @Override
    public Properties getClientInfo() throws SQLException {
        throw new NotImplementedException();
    }

    @Override
    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        throw new NotImplementedException();
    }

    @Override
    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        throw new NotImplementedException();
    }

    @Override
    public void setSchema(String schema) throws SQLException {
        try (Statement statement = c.createStatement()) {
            statement.execute("SET SCHEMA \"" + schema + "\"");
        }
    }

    @Override
    public String getSchema() throws SQLException {
        try (Statement statement = c.createStatement()) {
            try (ResultSet rs = statement.executeQuery("select current_schema  \n" +
                    "from   sysibm.sysdummy1")) {
                if(rs.next()) {
                    return rs.getString(1);
                }
            }
        }
        return null;
    }

    @Override
    public void abort(Executor executor) throws SQLException {
    }

    @Override
    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
    }

    @Override
    public int getNetworkTimeout() throws SQLException {
        throw new NotImplementedException();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw new NotImplementedException();
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        throw new NotImplementedException();
    }
}
