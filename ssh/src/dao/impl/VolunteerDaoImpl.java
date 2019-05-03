package dao.impl;

import dao.VolunteerDao;
import dbconn.DbConn;
import model.HasSchoolSchema;
import model.Volunteer;
import util.DbUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VolunteerDaoImpl extends BasePerformanceDao implements VolunteerDao {
    //TODO 頁數整合
    public final static Integer RECORD_PER_PAGE = 4;


    public VolunteerDaoImpl(HasSchoolSchema hasSchoolSchemaObject) {
        super(hasSchoolSchemaObject);
    }


    @Override
    public void insertVolunteer(Volunteer volunteer) throws SQLException {
        String sql = "INSERT INTO VOLUNTEER_RECORD (" +
                "RGNO, " +
                "CONTENT, SOURCE, " +
                "name, place, startTime, endTime, count, " +
                "DOCUMENT_FILE_ID, VIDEO_FILE_ID, EXTERNAL_LINK" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = null;
        try (Connection pcon = DbConn.getConnection(this)) {
            ps = pcon.prepareStatement(sql);
            int i = 0;
            ps.setInt(++i, volunteer.getRgno());
            ps.setString(++i, volunteer.getContent());
            ps.setString(++i, volunteer.getSource());

            ps.setString(++i, volunteer.getName());
            ps.setString(++i, volunteer.getPlace());
            ps.setString(++i, volunteer.getStartTime());
            ps.setString(++i, volunteer.getEndTime());
            ps.setString(++i, volunteer.getCount());

            i = setFilesAndReturnNewIndex(i, ps, volunteer);
            ps.setString(++i, volunteer.getExternalLink());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (ps != null)
                ps.close();
        }
    }

    @Override
    public void updateVolunteerByID(Volunteer volunteer, Integer rgno, Integer id) throws SQLException {
        String sql = "UPDATE VOLUNTEER_RECORD SET " +
                "NAME = ?, PLACE = ?, STARTTIME = ?,ENDTIME = ?," +
                "COUNT=?, " +
                "DOCUMENT_FILE_ID = ?,VIDEO_FILE_ID = ?,EXTERNAL_LINK= ?," +
                "SOURCE = ?, SUBMITTER = ?, CONTENT = ? ,CHECK = ? , STATUS = ?,SELECTEDYEAR = ?" +
                "  WHERE RGNO = ? AND id = ?";
        PreparedStatement ps = null;
        try (Connection pcon = DbConn.getConnection(this)) {
            ps = pcon.prepareStatement(sql);
            int i = 0;
            ps.setString(++i, volunteer.getName());
            ps.setString(++i, volunteer.getPlace());
            ps.setString(++i, volunteer.getStartTime());
            ps.setString(++i, volunteer.getEndTime());
            ps.setString(++i, volunteer.getCount());

            i = setCommonData(i, volunteer, ps);

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
    public List<Volunteer> getVolunteer(Integer rgno, String check, String status, String selectYear, Integer page) throws SQLException {
        String sql = "SELECT * FROM (\n" +
                "SELECT ROW_NUMBER() OVER (ORDER BY ID) AS RN,VOLUNTEER_RECORD.*\n" +
                "FROM VOLUNTEER_RECORD WHERE 1=1";
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
        List<Volunteer> volunteerList = new ArrayList<>();
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
                Volunteer volunteer = convertToInstance(resultSet);
                volunteerList.add(volunteer);
            }
            fillInExternalFields(volunteerList);
            return volunteerList;
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
        String sql = "SELECT COUNT(*) FROM VOLUNTEER_RECORD WHERE RGNO=? AND 1=1 ";
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
    public Volunteer getVolunteerByID(Integer rgno, Integer id) throws SQLException {
        String sql = "SELECT * FROM VOLUNTEER_RECORD WHERE ID = ?";
        if (rgno != null)
            sql += "AND RGNO=?";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Volunteer volunteer = null;
        try (Connection pcon = DbConn.getConnection(this)) {
            preparedStatement = pcon.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            if (rgno != null)
                preparedStatement.setInt(2, rgno);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                volunteer = convertToInstance(resultSet);
            }
            fillInExternalFields(Collections.singletonList(volunteer));
            return volunteer;
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
    public void deleteVolunteer(Integer rgno, Integer id) throws SQLException {
        Volunteer volunteer = getVolunteerByID(rgno, id);
        this.deleteOldUploadedFile(volunteer, null);
        String sql = "DELETE FROM VOLUNTEER_RECORD WHERE RGNO = ? AND ID = ?";
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
    public List<Volunteer> getExportStuVolunteerRecords(int year) {
        final List<Volunteer> list = new ArrayList<>();
        try {
            try (Connection pcon = DbConn.getConnection(this)) {
                try (PreparedStatement ps = pcon.prepareStatement("SELECT * " +
                        "FROM VOLUNTEER_RECORD " +
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

    private Volunteer convertToInstance(ResultSet resultSet) throws SQLException {
        Volunteer volunteer = new Volunteer(
                resultSet.getInt("id"),
                resultSet.getInt("rgno"),
                resultSet.getString("content"),
                resultSet.getString("source"),

                resultSet.getString("name"),
                resultSet.getString("place"),
                resultSet.getString("startTime"),
                resultSet.getString("endTime"),
                resultSet.getString("count"),

                DbUtils.getInteger(resultSet, "DOCUMENT_FILE_ID"),
                DbUtils.getInteger(resultSet, "VIDEO_FILE_ID"),
                resultSet.getString("EXTERNAL_LINK"),

                resultSet.getString("SUBMITTER"),
                resultSet.getString("SELECTEDYEAR"),
                resultSet.getInt("CHECK"),
                resultSet.getString("STATUS"),
                resultSet.getTimestamp("CREATED_AT")
        );
        volunteer.setSelectedYear(resultSet.getString("SelectedYear"));

        return volunteer;
    }
}
