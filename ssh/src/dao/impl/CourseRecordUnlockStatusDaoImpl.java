package dao.impl;

import dao.CourseRecordUnlockStatusDao;
import dbconn.DbConn;
import model.HasSchoolSchema;

import java.sql.*;
import java.util.Date;

public class CourseRecordUnlockStatusDaoImpl extends BaseDao implements CourseRecordUnlockStatusDao {
    private int allowUnlockInterval = 5;

    public CourseRecordUnlockStatusDaoImpl(HasSchoolSchema hasSchoolSchemaObject) {
        super(hasSchoolSchemaObject);
    }

    @Override
    public boolean isInUnlockStatus(String activeYear, String sem, Integer rgno) {
        try (Connection pcon = DbConn.getConnection(this)) {
            try (PreparedStatement ps = pcon.prepareStatement("" +
                    "SELECT ACTIVE_YEAR FROM COURSE_RECORD_UNLOCK_STATUS " +
                    "WHERE ACTIVE_YEAR = ? AND ACTIVE_SEM = ?  AND RGNO = ? AND CURRENT_TIMESTAMP BETWEEN START_TIME AND END_TIME")) {
                int i = 0;
                ps.setString(++i, activeYear);
                ps.setString(++i, sem);
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
    public void grantUnlock(String activeYear, String sem, Integer rgno) {
        revokeUnlock(activeYear, sem, rgno);
        try (Connection pcon = DbConn.getConnection(this)) {
            try (PreparedStatement ps = pcon.prepareStatement("" +
                    "DELETE FROM COURSE_RECORD_UNLOCK_STATUS WHERE ACTIVE_YEAR = ? AND ACTIVE_SEM = ? AND RGNO = ?")) {
                int i = 0;
                ps.setString(++i, activeYear);
                ps.setString(++i, sem);
                ps.setInt(++i, rgno);
                ps.executeUpdate();
            }

            try (PreparedStatement ps = pcon.prepareStatement("" +
                    "INSERT INTO COURSE_RECORD_UNLOCK_STATUS (ACTIVE_YEAR,ACTIVE_SEM, RGNO, START_TIME, END_TIME) " +
                    "VALUES (?, ?, ?, ?, ?)")) {
                int i = 0;

                Date now = new Date();
                ps.setString(++i, activeYear);
                ps.setString(++i, sem);
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
    public void revokeUnlock(String activeYear, String sem, Integer rgno) {
        try (Connection pcon = DbConn.getConnection(this)) {
            try (PreparedStatement ps = pcon.prepareStatement("DELETE FROM " +
                    "COURSE_RECORD_UNLOCK_STATUS " +
                    "WHERE ACTIVE_YEAR = ? AND ACTIVE_SEM = ? AND RGNO = ? AND CURRENT_TIMESTAMP BETWEEN START_TIME AND END_TIME")) {
                int i = 0;

                ps.setString(++i, activeYear);
                ps.setString(++i, sem);
                ps.setInt(++i, rgno);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
