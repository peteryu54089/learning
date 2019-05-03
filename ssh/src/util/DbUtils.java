package util;

import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DbUtils {
    public static String convertClobToString(Clob clob) {
        if (clob == null)
            return "";

        try {
            return clob.getSubString(1, (int) clob.length());
        } catch (SQLException e) {
            return "";
        }
    }

    public static Integer getInteger(ResultSet rs, String colName) throws SQLException {
        int nValue = rs.getInt(colName);
        return rs.wasNull() ? null : nValue;
    }
}
