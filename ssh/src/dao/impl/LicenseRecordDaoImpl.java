package dao.impl;

import dao.LicenseRecordDao;
import dbconn.DbConn;
import model.HasSchoolSchema;
import model.License;
import util.DbUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LicenseRecordDaoImpl extends BasePerformanceDao implements LicenseRecordDao {
    //TODO 頁數整合
    public final static Integer RECORD_PER_PAGE = 4;

    public LicenseRecordDaoImpl(HasSchoolSchema hasSchoolSchemaObject) {
        super(hasSchoolSchemaObject);
    }

    @Override
    public void insertLicense(License license) throws SQLException {
        String sql = "INSERT INTO LICENSE_RECORD (" +
                "RGNO, " +
                "CONTENT, SOURCE, " +
                "code, note, point, " +
                "result, time,licensenumber,group," +
                "DOCUMENT_FILE_ID, VIDEO_FILE_ID, EXTERNAL_LINK" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection pcon = DbConn.getConnection(this);
             PreparedStatement ps = pcon.prepareStatement(sql)) {
            int i = 0;
            ps.setInt(++i, license.getRgno());
            ps.setString(++i, license.getContent());
            ps.setString(++i, license.getSource());

            ps.setString(++i, license.getCode());
            ps.setString(++i, license.getNote());
            ps.setString(++i, license.getPoint());
            ps.setString(++i, license.getResult());
            ps.setString(++i, license.getTime());
            ps.setString(++i, license.getLicensenumber());
            ps.setString(++i, license.getGroup());

            i = setFilesAndReturnNewIndex(i, ps, license);
            ps.setString(++i, license.getExternalLink());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateLicenseByID(License license, Integer rgno, Integer id) throws SQLException {
        String sql =  "UPDATE LICENSE_RECORD SET " +
                "CODE = ?, NOTE = ?, POINT = ?,RESULT = ?," +
                "TIME =?,LICENSENUMBER = ?,GROUP = ?," +
                "DOCUMENT_FILE_ID = ?,VIDEO_FILE_ID = ?,EXTERNAL_LINK= ?," +
                "SOURCE = ?, SUBMITTER = ?, CONTENT = ? ,CHECK = ? , STATUS = ?,SELECTEDYEAR = ?" +
                "  WHERE RGNO = ? AND id = ?";
        PreparedStatement ps = null;
        try (Connection pcon = DbConn.getConnection(this)) {
            ps = pcon.prepareStatement(sql);
            int i = 0;
            ps.setString(++i, license.getCode());
            ps.setString(++i, license.getNote());
            ps.setString(++i, license.getPoint());
            ps.setString(++i, license.getResult());
            ps.setString(++i, license.getTime());
            ps.setString(++i, license.getLicensenumber());
            ps.setString(++i, license.getGroup());

            i = setCommonData(i, license, ps);

            ps.setInt(++i, rgno);
            ps.setInt(++i, id);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (ps != null)
                ps.close();
        }
    }

    @Override
    public List<License> getLicense(Integer rgno, String check, String status, String selectYear, Integer page) throws SQLException {
        String sql = "SELECT * FROM (\n" +
                "SELECT ROW_NUMBER() OVER (ORDER BY ID) AS RN,LICENSE_RECORD.*\n" +
                "FROM LICENSE_RECORD WHERE 1=1 ";
        if (rgno != null)
            sql += " AND RGNO=?";
        if (status != null)
            sql += " AND status = ?";
        if (check != null)
            sql += " AND check = ?";
        if (selectYear != null)
            sql += " AND SELECTEDYEAR = ?";
        sql += ")";
        if (page != null)
            sql += "WHERE RN BETWEEN ? AND ?\n";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<License> LicenseList = new ArrayList<>();
        try (Connection pcon = DbConn.getConnection(this)) {
            preparedStatement = pcon.prepareStatement(sql);
            Integer i = 1;
            if (rgno != null)
                preparedStatement.setInt(i++, rgno);
            if (status != null)
                preparedStatement.setString(i++, status);
            if (check != null)
                preparedStatement.setString(i++, check);
            if (selectYear != null)
                preparedStatement.setString(i++, selectYear);
            if (page != null) {
                preparedStatement.setInt(i++, ((page - 1) * RECORD_PER_PAGE) + 1);
                preparedStatement.setInt(i++, ((page) * RECORD_PER_PAGE));
            }
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                License license = convertToInstance(resultSet);
                LicenseList.add(license);
            }
            fillInExternalFields(LicenseList);
            return LicenseList;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null)
                preparedStatement.close();
            if (resultSet != null)
                resultSet.close();
        }
        return null;
    }

    @Override
    public Integer getPageNumber(Integer rgno) throws SQLException {
        String sql = "SELECT COUNT(*) FROM LICENSE_RECORD WHERE RGNO=? ";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int pagenumber = 1;
        try (Connection pcon = DbConn.getConnection(this)) {
            preparedStatement = pcon.prepareStatement(sql);
            preparedStatement.setInt(1, rgno);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                pagenumber = resultSet.getInt(1);
                pagenumber = ((pagenumber - 1) / RECORD_PER_PAGE) + 1;
            }
            return pagenumber;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null)
                preparedStatement.close();
            if (resultSet != null)
                resultSet.close();
        }
        return 1;
    }

    @Override
    public License getLicenseByID(Integer rgno, Integer id) throws SQLException {
        String sql = "SELECT * FROM LICENSE_RECORD WHERE ID = ?";
        if (rgno != null)
            sql += "AND RGNO=?";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        License license = null;
        try (Connection pcon = DbConn.getConnection(this)) {
            preparedStatement = pcon.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            if (rgno != null)
                preparedStatement.setInt(2, rgno);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                license = convertToInstance(resultSet);
            }
            fillInExternalFields(Collections.singletonList(license));
            return license;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null)
                preparedStatement.close();
            if (resultSet != null)
                resultSet.close();
        }
        return null;
    }

    @Override
    public void deleteLicense(Integer rgno, Integer id) throws SQLException {
        License license = getLicenseByID(rgno, id);
        this.deleteOldUploadedFile(license, null);
        String sql = "DELETE FROM LICENSE_RECORD WHERE RGNO = ? AND ID = ?";
        try (Connection pcon = DbConn.getConnection(this);
             PreparedStatement ps = pcon.prepareStatement(sql)) {
            int i = 0;
            ps.setInt(++i, rgno);
            ps.setInt(++i, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<License> getExportStuLicenseRecords(int year) {
        final List<License> list = new ArrayList<>();
        try {
            try (Connection pcon = DbConn.getConnection(this)) {
                try (PreparedStatement ps = pcon.prepareStatement("SELECT * " +
                        "FROM LICENSE_RECORD " +
                        "WHERE SELECTEDYEAR = ? AND STATUS = ? " +
                        "ORDER BY SELECTEDYEAR, RGNO, ID")) {
                    int i = 0;
                    // ps.setString(++i, regNo);
                    ps.setString(++i, String.format("%3d", year));
                    ps.setString(++i, "2"); // 已勾選
                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            list.add(convertToInstance(rs));
                        }
                    }
                    fillInExternalFields(list);
                }
            }
        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
        }

        return list;
    }

    private License convertToInstance(ResultSet resultSet) throws SQLException {
        License license = new License(
                resultSet.getInt("id"),
                resultSet.getInt("rgno"),
                resultSet.getString("content"),
                resultSet.getString("source"),

                resultSet.getString("code"),
                resultSet.getString("note"),
                resultSet.getString("point"),
                resultSet.getString("result"),
                resultSet.getString("time"),
                resultSet.getString("licensenumber"),
                resultSet.getString("group"),

                DbUtils.getInteger(resultSet, "DOCUMENT_FILE_ID"),
                DbUtils.getInteger(resultSet, "VIDEO_FILE_ID"),
                resultSet.getString("EXTERNAL_LINK"),

                resultSet.getString("SUBMITTER"),
                resultSet.getString("SELECTEDYEAR"),
                resultSet.getInt("CHECK"),
                resultSet.getString("STATUS"),
                resultSet.getTimestamp("CREATED_AT")
        );
        return license;
    }
}
