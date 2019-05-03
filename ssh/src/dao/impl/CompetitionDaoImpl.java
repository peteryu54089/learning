package dao.impl;

import dao.CompetitionDao;
import dbconn.DbConn;
import model.Competition;
import model.HasSchoolSchema;
import util.DbUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CompetitionDaoImpl extends BasePerformanceDao implements CompetitionDao {
    //TODO 頁數整合
    public final static Integer RECORD_PER_PAGE = 4;


    public CompetitionDaoImpl(HasSchoolSchema hasSchoolSchemaObject) {
        super(hasSchoolSchemaObject);
    }


    @Override
    public void insertCompetition(Competition competition) throws SQLException {
        String sql = "INSERT INTO COMPETITION_RECORD(" +
                "RGNO, " +
                "CONTENT, SOURCE, " +
                "NAME, ITEM, LEVEL, " +
                "AWARD, TIME, TYPE, FIELD, " +
                "DOCUMENT_FILE_ID, VIDEO_FILE_ID, EXTERNAL_LINK" +
        ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = null;
        try (Connection pcon = DbConn.getConnection(this)) {
            ps = pcon.prepareStatement(sql);
            int i = 0;
            ps.setInt(++i, competition.getRgno());
            ps.setString(++i, competition.getContent());
            ps.setString(++i, competition.getSource());
            ps.setString(++i, competition.getName());
            ps.setString(++i, competition.getItem());
            ps.setString(++i, competition.getLevel());
            ps.setString(++i, competition.getAward());
            ps.setString(++i, competition.getTime());
            ps.setString(++i, competition.getType());
            ps.setString(++i, competition.getField());
            i = setFilesAndReturnNewIndex(i, ps, competition);
            ps.setString(++i, competition.getExternalLink());

            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (ps != null)
                ps.close();
        }
    }

    @Override
    public void updateCompetitionByID(Competition competition, Integer rgno, Integer id) throws SQLException {
        String sql = "UPDATE COMPETITION_RECORD SET " +
                "NAME = ?, ITEM = ?, LEVEL = ?,AWARD = ?," +
                "TIME=?,TYPE = ?,FIELD = ?," +
                "DOCUMENT_FILE_ID = ?,VIDEO_FILE_ID = ?,EXTERNAL_LINK= ?," +
                "SOURCE = ?, SUBMITTER = ?, CONTENT = ? ,CHECK = ? , STATUS = ?,SELECTEDYEAR = ?" +
                "  WHERE RGNO = ? AND id = ?";
        Competition oldModel = getCompetitionByID(rgno, id);
        deleteOldUploadedFile(oldModel, competition);
        try (Connection pcon = DbConn.getConnection(this); PreparedStatement ps = pcon.prepareStatement(sql)) {
            int i = 0;
            ps.setString(++i, competition.getName());
            ps.setString(++i, competition.getItem());
            ps.setString(++i, competition.getLevel());
            ps.setString(++i, competition.getAward());
            ps.setString(++i, competition.getTime());
            ps.setString(++i, competition.getType());
            ps.setString(++i, competition.getField());
            i = setCommonData(i, competition, ps);
            
            ps.setInt(++i, rgno);
            ps.setInt(++i, id);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Competition> getCompetition(Integer rgno, String check, String status, String selectYear, Integer page) throws SQLException {
        String sql = "SELECT * FROM (\n" +
                "SELECT ROW_NUMBER() OVER (ORDER BY ID) AS RN,COMPETITION_RECORD.*\n" +
                "FROM COMPETITION_RECORD WHERE 1=1 ";
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
        List<Competition> competitionList = new ArrayList<>();
        try (Connection pcon = DbConn.getConnection(this)) {
            preparedStatement = pcon.prepareStatement(sql);
            int i = 1;
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
                Competition competition = convertToInstance(resultSet);
                competitionList.add(competition);
            }

            fillInExternalFields(competitionList);
            return competitionList;
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
        String sql = "SELECT COUNT(*) FROM COMPETITION_RECORD WHERE RGNO=? AND 1=1 ";
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
    public Competition getCompetitionByID(Integer rgno, Integer id) throws SQLException {
        String sql = "SELECT * FROM COMPETITION_RECORD WHERE ID = ?";
        if (rgno != null)
            sql += "AND RGNO =?";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Competition competition = null;
        try (Connection pcon = DbConn.getConnection(this)) {
            preparedStatement = pcon.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            if (rgno != null)
                preparedStatement.setInt(2, rgno);

            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                competition = convertToInstance(resultSet);
            }

            fillInExternalFields(Collections.singletonList(competition));
            return competition;
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
    public void deleteCompetition(Integer rgno, Integer id) throws SQLException {
        Competition competition = getCompetitionByID(rgno, id);
        this.deleteOldUploadedFile(competition, null);
        String sql = "DELETE FROM COMPETITION_RECORD WHERE RGNO = ? AND ID = ?";
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
    public List<Competition> getExportStuCompetitionRecords(int year) {
        final List<Competition> list = new ArrayList<>();
        try {
            try (Connection pcon = DbConn.getConnection(this)) {
                try (PreparedStatement ps = pcon.prepareStatement("SELECT * " +
                        "FROM COMPETITION_RECORD " +
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

    private Competition convertToInstance(ResultSet resultSet) throws SQLException {
        Competition competition = new Competition(
                resultSet.getInt("id"),
                resultSet.getInt("rgno"),
                resultSet.getString("content"),
                resultSet.getString("source"),

                resultSet.getString("name"),
                resultSet.getString("item"),
                resultSet.getString("level"),
                resultSet.getString("award"),
                resultSet.getString("time"),
                resultSet.getString("type"),
                resultSet.getString("field"),

                DbUtils.getInteger(resultSet, "DOCUMENT_FILE_ID"),
                DbUtils.getInteger(resultSet, "VIDEO_FILE_ID"),
                resultSet.getString("EXTERNAL_LINK"),

                resultSet.getString("SUBMITTER"),
                resultSet.getString("SELECTEDYEAR"),
                resultSet.getInt("CHECK"),
                resultSet.getString("STATUS"),
                resultSet.getTimestamp("CREATED_AT")
        );

        return competition;
    }
}
