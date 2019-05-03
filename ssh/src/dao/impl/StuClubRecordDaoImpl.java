package dao.impl;

import dao.StuClubRecordDao;
import dao.SystemConfigDao;
import dbconn.DbConn;
import model.HasSchoolSchema;
import model.SystemConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class StuClubRecordDaoImpl extends BaseDao implements StuClubRecordDao {
    public StuClubRecordDaoImpl(HasSchoolSchema hasSchoolSchemaObject) {
        super(hasSchoolSchemaObject);
    }

    @Override
    public boolean syncClubRecordToCadre() {
        SystemConfigDao systemConfigDao = new SystemConfigDaoImpl(this);
        SystemConfig systemConfig = systemConfigDao.getSystemConfig();
        String sqlForCopy = "INSERT INTO CADRE_RECORD (" +
                "RGNO, " +
                "UNIT, STARTTIME, ENDTIME, " +
                "TERM, JOB, LEVEL, " +
                "DOCUMENT_FILE_ID, VIDEO_FILE_ID, EXTERNAL_LINK, SOURCE, " +
                "CONTENT) SELECT " +
                "RGNO, " +
                "UNIT, STARTTIME, ENDTIME, " +
                "TERM, JOB, LEVEL, " +
                "NULL, NULL, '', SOURCE, " +
                "CONTENT " +
                "FROM STU_CLUB_RECORD WHERE CR_NUM BETWEEN ? AND ?";

        String sqlForFetchSyncRange = "SELECT " +
                "MIN(CR_NUM) MIN_CR_NUM, MAX(CR_NUM) MAX_CR_NUM " +
                "FROM HS_STU_CLUB_RECORD WHERE CR_NUM > ?";

        try (Connection pcon = DbConn.getConnection(this)) {
            int minId = 0;
            int maxId = Integer.MAX_VALUE;

            try (PreparedStatement ps = pcon.prepareStatement(sqlForFetchSyncRange)) {
                int i = 0;
                ps.setInt(++i, systemConfig.getSyncStatus().getLastSyncedStuClubId());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        minId = rs.getInt("MIN_CR_NUM");
                        maxId = rs.getInt("MAX_CR_NUM");
                    }
                }
            }

            try (PreparedStatement ps = pcon.prepareStatement(sqlForCopy)) {
                int i = 0;
                ps.setInt(++i, minId);
                ps.setInt(++i, maxId);

                Integer cnt = ps.executeUpdate();
                cnt = cnt * 1;
            }

            systemConfig.getSyncStatus().setLastSyncedStuClubId(maxId);
            systemConfig.getSyncStatus().setLastSyncedStuClubTime(new Date());
            systemConfigDao.saveSystemConfig(systemConfig);
            return true;
        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
        }

        return false;
    }
}
