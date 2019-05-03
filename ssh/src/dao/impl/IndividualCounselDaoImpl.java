package dao.impl;

import dao.IndividualCounselDao;
import dbconn.DbConn;
import model.HasSchoolSchema;
import model.IndividualCounsel;
import util.LogUtility;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ching Yun Yu on 2018/5/5.
 */
public class IndividualCounselDaoImpl extends BaseCounselDaoImpl implements IndividualCounselDao {


    public IndividualCounselDaoImpl(HasSchoolSchema hasSchoolSchemaObject) {
        super(hasSchoolSchemaObject);
        // Default Constructor
    }

    @Override
    public List<IndividualCounsel> getIndividualCounselByTitleOrDate(java.util.Date startTime, java.util.Date endTime, String title, Integer rgNo) throws SQLException {
        String sql = "SELECT C.*, F.FILE_NAME\n" +
                "FROM INDIVIDUAL_COUNSEL C LEFT JOIN UPLOAD_FILES F\n" +
                "ON C.FILE_ID = F.ID\n" +
                "WHERE C.TITLE LIKE ?\n";
        if (startTime != null) sql += "  AND DATE(START_TIME) >= ?\n";
        if (endTime != null) sql += "  AND DATE(END_TIME) <= ?";
        if (rgNo != null) sql += "  AND REGISTER_NUMBER = ?";

        List<IndividualCounsel> individualCounselList = new ArrayList<>();
        try (Connection pcon = DbConn.getConnection(this); PreparedStatement preparedStatement = pcon.prepareStatement(sql)) {
            int i = 0;
            preparedStatement.setString(++i, "%" + title + "%");
            if (startTime != null) preparedStatement.setDate(++i, new java.sql.Date(startTime.getTime()));
            if (endTime != null) preparedStatement.setDate(++i, new java.sql.Date(endTime.getTime()));
            if (rgNo != null) preparedStatement.setInt(++i, rgNo);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    individualCounselList.add(mappingFromTable(resultSet));
                }
            }
        } catch (SQLException e) {
            LogUtility.errorLog("Select from individual counsel get error", e);
            throw e;
        }
        return individualCounselList;
    }

    @Override
    public IndividualCounsel getIndividualCounselById(int id) throws SQLException {
        String sql = "SELECT C.*,F.FILE_NAME\n" +
                "FROM INDIVIDUAL_COUNSEL C\n" +
                "       LEFT JOIN UPLOAD_FILES F ON C.FILE_ID = F.ID\n" +
                "WHERE C.ID = ?";
        List<IndividualCounsel> individualCounselList = new ArrayList<>();

        try (Connection connection = DbConn.getConnection(this); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            int i = 0;
            preparedStatement.setInt(++i, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    IndividualCounsel individualCounsel = mappingFromTable(resultSet);
                    individualCounselList.add(individualCounsel);
                }
            }
        } catch (SQLException e) {
            LogUtility.errorLog("Select from individual counsel get error", e);
            throw e;
        }
        if (individualCounselList.isEmpty()) return null;
        else return individualCounselList.get(0);
    }

    @Override
    public void insertIndividualCounsel(IndividualCounsel individualCounsel) throws SQLException {
        String sql =
                "INSERT INTO INDIVIDUAL_COUNSEL (START_TIME, END_TIME, TITLE, FILE_ID,\n" +
                        "                                LOCATION, DESCRIPTION, COUNSELOR, SUBMITTER,\n" +
                        "                                RGNO)\n" +
                        "  VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DbConn.getConnection(this); PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            setCounselPreparedStatement(preparedStatement, individualCounsel);
            preparedStatement.execute();
        } catch (SQLException e) {
            LogUtility.errorLog("Select from individual counsel get error", e);
            throw e;
        }
    }

    @Override
    public void updateIndividualCounsel(IndividualCounsel individualCounsel) throws SQLException {
        String sql =
                "UPDATE INDIVIDUAL_COUNSEL\n" +
                        "SET START_TIME        = ?,\n" +
                        "    END_TIME          = ?,\n" +
                        "    TITLE             = ?,\n" +
                        "    FILE_ID           = ?,\n" +
                        "    LOCATION          = ?,\n" +
                        "    DESCRIPTION       = ?,\n" +
                        "    COUNSELOR         = ?,\n" +
                        "    SUBMITTER         = ?,\n" +
                        "    REGISTER_NUMBER   = ?\n" +
                        "WHERE ID = ?";
        try (Connection connection = DbConn.getConnection(this); PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setCounselPreparedStatement(preparedStatement, individualCounsel);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Update individual counsel failed, no rows affected.");
            }
        } catch (SQLException e) {
            LogUtility.errorLog("Update individual counsel from group counsel get error", e);
            throw e;
        }
    }

    @Override
    public void deleteIndividualCounsel(int id) throws SQLException {
        String sql =
                "DELETE\n" +
                        "FROM INDIVIDUAL_COUNSEL\n" +
                        "WHERE ID = ?";

        try (Connection pcon = DbConn.getConnection(this); PreparedStatement preparedStatement = pcon.prepareStatement(sql)) {
            int i = 0;
            preparedStatement.setInt(++i, id);

            preparedStatement.execute();
        } catch (SQLException e) {
            LogUtility.errorLog("Delete individual counsel get error", e);
            throw e;
        }
    }

    private IndividualCounsel mappingFromTable(ResultSet resultSet) throws SQLException {
        return new IndividualCounsel(
                resultSet.getInt("ID"),
                resultSet.getTimestamp("START_TIME"),
                resultSet.getTimestamp("END_TIME"),
                resultSet.getString("TITLE"),
                resultSet.getString("FILE_NAME"),
                resultSet.getInt("FILE_ID"),
                resultSet.getString("LOCATION"),
                resultSet.getString("DESCRIPTION"),
                resultSet.getString("COUNSELOR"),
                resultSet.getString("SUBMITTER"),
                resultSet.getInt("RGNO"),
                resultSet.getTimestamp("CREATED_AT")
        );
    }
}
