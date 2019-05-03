package dao.impl;

import dao.PerformanceUnlockStatusDao;
import dbconn.DbConn;
import model.HasSchoolSchema;

import java.sql.*;
import java.util.Date;

public class PerformanceUnlockStatusDaoImpl extends BaseDao implements PerformanceUnlockStatusDao {
    private int allowUnlockInterval = 5;

    public PerformanceUnlockStatusDaoImpl(HasSchoolSchema hasSchoolSchemaObject) {
        super(hasSchoolSchemaObject);
    }

    @Override
    public boolean isInUnlockStatus(String activeYear, Integer rgno) {
        try (Connection pcon = DbConn.getConnection(this)) {
            try (PreparedStatement ps = pcon.prepareStatement("" +
                    "SELECT ACTIVE_YEAR FROM PERFORMANCE_UNLOCK_STATUS " +
                    "WHERE ACTIVE_YEAR = ? AND RGNO = ? AND CURRENT_TIMESTAMP BETWEEN START_TIME AND END_TIME")) {
                int i = 0;
                ps.setString(++i, activeYear);
                ps.setInt(++i, rgno);

                try (ResultSet resultSet = ps.executeQuery()) {
                    if (resultSet.next()) {
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void grantUnlock(String activeYear, Integer rgno) {
        try (Connection pcon = DbConn.getConnection(this)) {
            try(PreparedStatement ps = pcon.prepareStatement("" +
                    "DELETE FROM PERFORMANCE_UNLOCK_STATUS WHERE ACTIVE_YEAR = ? AND RGNO = ?")){
                int i = 0;
                ps.setString(++i, activeYear);
                ps.setInt(++i, rgno);
                ps.executeUpdate();
            }

            try (PreparedStatement ps = pcon.prepareStatement("" +
                    "INSERT INTO PERFORMANCE_UNLOCK_STATUS (ACTIVE_YEAR, RGNO, START_TIME, END_TIME) " +
                    "VALUES (?, ?, ?, ?)")) {
                int i = 0;

                Date now = new Date();
                ps.setString(++i, activeYear);
                ps.setInt(++i, rgno);
                ps.setTimestamp(++i, new Timestamp(now.getTime()));
                ps.setTimestamp(++i, new Timestamp(now.getTime() + 86400 * 1000 * allowUnlockInterval));
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void revokeUnlock(String activeYear, Integer rgno) {
        try (Connection pcon = DbConn.getConnection(this)) {
            try (PreparedStatement ps = pcon.prepareStatement("DELETE FROM " +
                    "PERFORMANCE_UNLOCK_STATUS " +
                    "WHERE ACTIVE_YEAR = ? AND RGNO = ? AND CURRENT_TIMESTAMP BETWEEN START_TIME AND END_TIME")) {
                int i = 0;

                ps.setString(++i, activeYear);
                ps.setInt(++i, rgno);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
