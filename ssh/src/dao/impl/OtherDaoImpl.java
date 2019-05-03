package dao.impl;

import dao.OtherDao;
import dbconn.DbConn;
import model.HasSchoolSchema;
import model.Other;
import util.DbUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OtherDaoImpl extends BasePerformanceDao implements OtherDao {

    //TODO 頁數整合
    public final static Integer RECORD_PER_PAGE = 4;


    public OtherDaoImpl(HasSchoolSchema hasSchoolSchemaObject) {
        super(hasSchoolSchemaObject);
    }

    @Override
    public void insertOther(Other other) throws SQLException {
        String sql = "INSERT INTO OTHER_RECORD (" +
                "RGNO, " +
                "CONTENT, SOURCE, " +
                "NAME, UNIT, STARTTIME, ENDTIME, COUNT, TYPE, " +
                "DOCUMENT_FILE_ID, VIDEO_FILE_ID, EXTERNAL_LINK) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement ps = null;
        try (Connection pcon = DbConn.getConnection(this)) {
            ps = pcon.prepareStatement(sql);
            int i = 0;
            ps.setInt(++i, other.getRgno());
            ps.setString(++i, other.getContent());
            ps.setString(++i, other.getSource());
            
            ps.setString(++i, other.getName());
            ps.setString(++i, other.getUnit());
            ps.setString(++i, other.getStartTime());
            ps.setString(++i, other.getEndTime());
            ps.setString(++i, other.getCount());
            ps.setString(++i, other.getType());

            i = setFilesAndReturnNewIndex(i, ps, other);
            ps.setString(++i, other.getExternalLink());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (ps != null)
                ps.close();
        }
    }

    @Override
    public void updateOtherByID(Other other, Integer rgno, Integer id) throws SQLException {
        String sql = "UPDATE OTHER_RECORD SET " +
                " NAME = ?, UNIT = ?, STARTTIME = ?, ENDTIME = ?, COUNT = ?, TYPE = ?, " +
                "DOCUMENT_FILE_ID = ?,VIDEO_FILE_ID = ?,EXTERNAL_LINK= ?," +
                "SOURCE = ?, SUBMITTER = ?, CONTENT = ? ,CHECK = ? , STATUS = ?,SELECTEDYEAR = ? " +
                "WHERE RGNO = ? AND id = ?";

        try (Connection pcon = DbConn.getConnection(this);
             PreparedStatement ps =pcon.prepareStatement(sql)) {
            int i = 0;
            ps.setString(++i, other.getName());
            ps.setString(++i, other.getUnit());
            ps.setString(++i, other.getStartTime());
            ps.setString(++i, other.getEndTime());
            ps.setString(++i, other.getCount());
            ps.setString(++i, other.getType());

            i = setCommonData(i, other, ps);

            ps.setInt(++i, rgno);
            ps.setInt(++i, id);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Other> getOther(Integer rgno, String check, String status, String selectYear, Integer page, String type) throws SQLException {
        String sql = "SELECT * FROM (\n" +
                "SELECT ROW_NUMBER() OVER (ORDER BY ID) AS RN,OTHER_RECORD.*\n" +
                "FROM OTHER_RECORD WHERE 1=1 ";
        if (type != null)
            sql += " AND TYPE=?";
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
        List<Other> otherList = new ArrayList<>();
        try (Connection pcon = DbConn.getConnection(this)) {
            preparedStatement = pcon.prepareStatement(sql);
            Integer i = 1;
            if (type != null)
                preparedStatement.setString(i++, type);
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
                Other other = convertToInstance(resultSet);
                otherList.add(other);
            }
            fillInExternalFields(otherList);
            return otherList;
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
        String sql = "SELECT COUNT(*) FROM OTHER_RECORD WHERE RGNO=? ";
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
    public Other getOtherByID(Integer rgno, Integer id) throws SQLException {
        String sql = "SELECT * FROM OTHER_RECORD WHERE ID = ?";
        if (rgno != null)
            sql += "AND RGNO=?";
        ResultSet resultSet = null;
        Other other = null;
        try (Connection pcon = DbConn.getConnection(this);
             PreparedStatement ps = pcon.prepareStatement(sql)) {
            ps.setInt(1, id);
            if (rgno != null)
                ps.setInt(2, rgno);
            resultSet = ps.executeQuery();
            if (resultSet.next()) {
                other = convertToInstance(resultSet);
            }
            fillInExternalFields(Collections.singletonList(other));
            return other;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void deleteOther(Integer rgno, Integer id) throws SQLException {
        Other other = getOtherByID(rgno, id);
        this.deleteOldUploadedFile(other, null);
        String sql = "DELETE FROM OTHER_RECORD WHERE RGNO = ? AND ID = ?";
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
    public List<Other> getExportStuVolunteerRecords(int year, String type) {
        final List<Other> list = new ArrayList<>();
        try {
            try (Connection pcon = DbConn.getConnection(this)) {
                try (PreparedStatement ps = pcon.prepareStatement("SELECT * " +
                        "FROM OTHER_RECORD " +
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

    private Other convertToInstance(ResultSet resultSet) throws SQLException {
        Other other = new Other(
                resultSet.getInt("id"),
                resultSet.getInt("rgno"),
                resultSet.getString("content"),
                resultSet.getString("source"),

                resultSet.getString("name"),
                resultSet.getString("unit"),
                resultSet.getString("startTime"),
                resultSet.getString("endTime"),
                resultSet.getString("count"),
                resultSet.getString("type"),

                DbUtils.getInteger(resultSet, "DOCUMENT_FILE_ID"),
                DbUtils.getInteger(resultSet, "VIDEO_FILE_ID"),
                resultSet.getString("EXTERNAL_LINK"),

                resultSet.getString("SUBMITTER"),
                resultSet.getString("SELECTEDYEAR"),
                resultSet.getInt("CHECK"),
                resultSet.getString("STATUS"),
                resultSet.getTimestamp("CREATED_AT")
        );

        return other;
    }
}
