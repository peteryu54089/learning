package legacy.jdbc.db2;

import com.ibm.db2.jcc.am.io;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;

public class Db2forJccPreparedStatementWrapper implements PreparedStatement {
    private final com.ibm.db2.jcc.am.io p;

    Db2forJccPreparedStatementWrapper(io p) {
        this.p = p;
    }

    @Override
    public ResultSet executeQuery() throws SQLException {
        return p.executeQuery();
    }

    @Override
    public int executeUpdate() throws SQLException {
        return p.executeUpdate();
    }

    @Override
    public void setNull(int parameterIndex, int sqlType) throws SQLException {
        p.setNull(parameterIndex, sqlType);
    }

    @Override
    public void setBoolean(int parameterIndex, boolean x) throws SQLException {
        p.setBoolean(parameterIndex, x);
    }

    @Override
    public void setByte(int parameterIndex, byte x) throws SQLException {
        p.setByte(parameterIndex, x);
    }

    @Override
    public void setShort(int parameterIndex, short x) throws SQLException {
        p.setShort(parameterIndex, x);
    }

    @Override
    public void setInt(int parameterIndex, int x) throws SQLException {
        p.setInt(parameterIndex, x);
    }

    @Override
    public void setLong(int parameterIndex, long x) throws SQLException {
        p.setLong(parameterIndex, x);
    }

    @Override
    public void setFloat(int parameterIndex, float x) throws SQLException {
        p.setFloat(parameterIndex, x);
    }

    @Override
    public void setDouble(int parameterIndex, double x) throws SQLException {
        p.setDouble(parameterIndex, x);
    }

    @Override
    public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
        p.setBigDecimal(parameterIndex, x);
    }

    @Override
    public void setString(int parameterIndex, String x) throws SQLException {
        p.setString(parameterIndex, x);
    }

    @Override
    public void setBytes(int parameterIndex, byte[] x) throws SQLException {
        p.setBytes(parameterIndex, x);
    }

    @Override
    public void setDate(int parameterIndex, Date x) throws SQLException {
        p.setDate(parameterIndex, x);
    }

    @Override
    public void setTime(int parameterIndex, Time x) throws SQLException {
        p.setTime(parameterIndex, x);
    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
        p.setTimestamp(parameterIndex, x);
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {
        p.setAsciiStream(parameterIndex, x);
    }

    @Override
    public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {
        p.setUnicodeStream(parameterIndex, x, length);
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {
        p.setBinaryStream(parameterIndex, x, length);
    }

    @Override
    public void clearParameters() throws SQLException {
        p.clearParameters();
    }

    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
        p.setObject(parameterIndex, x, targetSqlType);
    }

    @Override
    public void setObject(int parameterIndex, Object x) throws SQLException {
        p.setObject(parameterIndex, x);
    }

    @Override
    public boolean execute() throws SQLException {
        return p.execute();
    }

    @Override
    public void addBatch() throws SQLException {
        p.addBatch();
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {
        p.setCharacterStream(parameterIndex, reader, length);
    }

    @Override
    public void setRef(int parameterIndex, Ref x) throws SQLException {
        p.setRef(parameterIndex, x);
    }

    @Override
    public void setBlob(int parameterIndex, Blob x) throws SQLException {
        p.setBlob(parameterIndex, x);
    }

    @Override
    public void setClob(int parameterIndex, Clob x) throws SQLException {
        p.setClob(parameterIndex, x);
    }

    @Override
    public void setArray(int parameterIndex, Array x) throws SQLException {
        p.setArray(parameterIndex, x);
    }

    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        return p.getMetaData();
    }

    @Override
    public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {
        p.setDate(parameterIndex, x, cal);
    }

    @Override
    public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
        p.setTime(parameterIndex, x, cal);
    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
        p.setTimestamp(parameterIndex, x, cal);
    }

    @Override
    public void setNull(int parameterIndex, int sqlType, String typeName) throws SQLException {
        p.setNull(parameterIndex, sqlType, typeName);
    }

    @Override
    public void setURL(int parameterIndex, URL x) throws SQLException {
        p.setURL(parameterIndex, x);
    }

    @Override
    public ParameterMetaData getParameterMetaData() throws SQLException {
        return p.getParameterMetaData();
    }

    @Override
    public void setRowId(int parameterIndex, RowId x) throws SQLException {
        p.setRowId(parameterIndex, x);
    }

    @Override
    public void setNString(int parameterIndex, String value) throws SQLException {
        p.setNString(parameterIndex, value);
    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException {
        p.setNCharacterStream(parameterIndex, value, length);
    }

    @Override
    public void setNClob(int parameterIndex, NClob value) throws SQLException {
        p.setNClob(parameterIndex, value);
    }

    @Override
    public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
        p.setClob(parameterIndex, reader, length);
    }

    @Override
    public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {
        p.setBlob(parameterIndex, inputStream, length);
    }

    @Override
    public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
        p.setNClob(parameterIndex, reader, length);
    }

    @Override
    public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
        p.setSQLXML(parameterIndex, xmlObject);
    }

    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength) throws SQLException {
        p.setObject(parameterIndex, x, targetSqlType, scaleOrLength);
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
        p.setAsciiStream(parameterIndex, x, length);
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
        p.setBinaryStream(parameterIndex, x, length);
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
        p.setCharacterStream(parameterIndex, reader, length);
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
        p.setAsciiStream(parameterIndex, x);
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
        p.setBinaryStream(parameterIndex, x);
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
        p.setCharacterStream(parameterIndex, reader);
    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
        p.setNCharacterStream(parameterIndex, value);
    }

    @Override
    public void setClob(int parameterIndex, Reader reader) throws SQLException {
        p.setClob(parameterIndex, reader);
    }

    @Override
    public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
        p.setBlob(parameterIndex, inputStream);
    }

    @Override
    public void setNClob(int parameterIndex, Reader reader) throws SQLException {
        p.setNClob(parameterIndex, reader);
    }

    @Override
    public ResultSet executeQuery(String sql) throws SQLException {
        return p.executeQuery(sql);
    }

    @Override
    public int executeUpdate(String sql) throws SQLException {
        return p.executeUpdate(sql);
    }

    @Override
    public void close() throws SQLException {
        p.close();
    }

    @Override
    public int getMaxFieldSize() throws SQLException {
        return p.getMaxFieldSize();
    }

    @Override
    public void setMaxFieldSize(int max) throws SQLException {
        p.setMaxFieldSize(max);
    }

    @Override
    public int getMaxRows() throws SQLException {
        return p.getMaxRows();
    }

    @Override
    public void setMaxRows(int max) throws SQLException {
        p.setMaxRows(max);
    }

    @Override
    public void setEscapeProcessing(boolean enable) throws SQLException {
        p.setEscapeProcessing(enable);
    }

    @Override
    public int getQueryTimeout() throws SQLException {
        return p.getQueryTimeout();
    }

    @Override
    public void setQueryTimeout(int seconds) throws SQLException {
        p.setQueryTimeout(seconds);
    }

    @Override
    public void cancel() throws SQLException {
        p.cancel();
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return p.getWarnings();
    }

    @Override
    public void clearWarnings() throws SQLException {
        p.clearWarnings();
    }

    @Override
    public void setCursorName(String name) throws SQLException {
        p.setCursorName(name);
    }

    @Override
    public boolean execute(String sql) throws SQLException {
        return p.execute(sql);
    }

    @Override
    public ResultSet getResultSet() throws SQLException {
        return p.getResultSet();
    }

    @Override
    public int getUpdateCount() throws SQLException {
        return p.getUpdateCount();
    }

    @Override
    public boolean getMoreResults() throws SQLException {
        return p.getMoreResults();
    }

    @Override
    public void setFetchDirection(int direction) throws SQLException {
        p.setFetchDirection(direction);
    }

    @Override
    public int getFetchDirection() throws SQLException {
        return p.getFetchDirection();
    }

    @Override
    public void setFetchSize(int rows) throws SQLException {
        p.setFetchSize(rows);
    }

    @Override
    public int getFetchSize() throws SQLException {
        return p.getFetchSize();
    }

    @Override
    public int getResultSetConcurrency() throws SQLException {
        return p.getResultSetConcurrency();
    }

    @Override
    public int getResultSetType() throws SQLException {
        return p.getResultSetType();
    }

    @Override
    public void addBatch(String sql) throws SQLException {
        p.addBatch(sql);
    }

    @Override
    public void clearBatch() throws SQLException {
        p.clearBatch();
    }

    @Override
    public int[] executeBatch() throws SQLException {
        return p.executeBatch();
    }

    @Override
    public Connection getConnection() throws SQLException {
        return p.getConnection();
    }

    @Override
    public boolean getMoreResults(int current) throws SQLException {
        return p.getMoreResults();
    }

    @Override
    public ResultSet getGeneratedKeys() throws SQLException {
        return p.getGeneratedKeys();
    }

    @Override
    public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
        return p.executeUpdate(sql, autoGeneratedKeys);
    }

    @Override
    public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
        throw new NotImplementedException();
    }

    @Override
    public int executeUpdate(String sql, String[] columnNames) throws SQLException {
        throw new NotImplementedException();
    }

    @Override
    public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
        return p.execute(sql, autoGeneratedKeys);
    }

    @Override
    public boolean execute(String sql, int[] columnIndexes) throws SQLException {
        throw new NotImplementedException();
    }

    @Override
    public boolean execute(String sql, String[] columnNames) throws SQLException {
        throw new NotImplementedException();
    }

    @Override
    public int getResultSetHoldability() throws SQLException {
        return p.getResultSetHoldability();
    }

    @Override
    public boolean isClosed() throws SQLException {
        return p.getConnection().isClosed();
    }

    @Override
    public void setPoolable(boolean poolable) throws SQLException {
        p.setPoolable(poolable);
    }

    @Override
    public boolean isPoolable() throws SQLException {
        return p.isPoolable();
    }

    @Override
    public void closeOnCompletion() throws SQLException {
        p.closeOnCompletion();
    }

    @Override
    public boolean isCloseOnCompletion() throws SQLException {
        return p.isCloseOnCompletion();
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
