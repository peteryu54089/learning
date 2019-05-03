package dao.impl;

import dao.GroupCounselDao;
import dbconn.DbConn;
import model.GroupCounsel;
import model.GroupCounselMember;
import model.HasSchoolSchema;
import util.LogUtility;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Ching Yun Yu on 2018/5/5.
 */
public class GroupCounselDaoImpl extends BaseCounselDaoImpl implements GroupCounselDao {
    public GroupCounselDaoImpl(HasSchoolSchema hasSchoolSchemaObject) {
        super(hasSchoolSchemaObject);
    }

    @Override
    public List<GroupCounsel> getGroupCounselByTitleOrDate(Date startTime, Date endTime, String title) throws SQLException {
        String sql = "SELECT c.*, F.FILE_NAME\n" +
                "FROM GROUP_COUNSEL C LEFT JOIN UPLOAD_FILES F\n" +
                "ON C.FILE_ID = F.ID\n" +
                "WHERE  C.TITLE LIKE ?\n";
        if (startTime != null) sql += "  AND DATE(START_TIME) >= ?\n";
        if (endTime != null) sql += "  AND DATE(END_TIME) <= ?";


        List<GroupCounsel> groupCounselList = new ArrayList<>();
        try (Connection pcon = DbConn.getConnection(this); PreparedStatement preparedStatement = pcon.prepareStatement(sql)) {
            int i = 0;
            preparedStatement.setString(++i, "%" + title + "%");
            if (startTime != null) preparedStatement.setDate(++i, new java.sql.Date(startTime.getTime()));
            if (endTime != null) preparedStatement.setDate(++i, new java.sql.Date(endTime.getTime()));
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    groupCounselList.add(mappingFromTable(resultSet));
                }
            }
        } catch (SQLException e) {
            LogUtility.errorLog("Select from group counsel get error", e);
            throw e;
        }
        for (GroupCounsel item : groupCounselList) {
            item.setGroupCounselMember(getGroupCounselMemberListById(item.getId()));
        }
        return groupCounselList;
    }

    @Override
    public void insertGroupCounsel(GroupCounsel groupCounsel) throws SQLException {
        String sql =
                "INSERT INTO GROUP_COUNSEL(START_TIME, END_TIME, TITLE, FILE_ID , LOCATION, DESCRIPTION,\n" +
                        "                          COUNSELOR, SUBMITTER)\n" +
                        "  VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        int id;
        try (Connection pcon = DbConn.getConnection(this);
             PreparedStatement preparedStatement = pcon.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setCounselPreparedStatement(preparedStatement, groupCounsel);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    id = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            LogUtility.errorLog("Insert from group counsel get error", e);
            throw e;
        }
        for (GroupCounselMember groupCounselMember : groupCounsel.getGroupCounselMember()) {
            groupCounselMember.setGroupCounselId(id);
            insertGroupCounselMember(groupCounselMember);
        }
    }

    @Override
    public void deleteGroupCounsel(int id) throws SQLException {
        String sql = "DELETE\n" +
                "FROM GROUP_COUNSEL\n" +
                "WHERE ID = ?";
        try (Connection pcon = DbConn.getConnection(this); PreparedStatement preparedStatement = pcon.prepareStatement(sql)) {
            int i = 0;
            preparedStatement.setInt(++i, id);
            preparedStatement.execute();
        } catch (SQLException e) {
            LogUtility.errorLog("Delete from group counsel get error", e);
            throw e;
        }
    }

    public List<GroupCounsel> getGroupCounselByTitleOrDateAndRegNo(Date startTime, Date endTime, String title, String regNo) throws SQLException {
        String sql = "\n" +
                "SELECT *\n" +
                "FROM (\n" +
                "  SELECT *\n" +
                "  FROM GROUP_COUNSEL_MEMBER AS A,\n" +
                "  GROUP_COUNSEL AS B\n" +
                "  WHERE A.GROUP_COUNSEL_ID = B.ID) AS C\n" +
                "WHERE C.RGNO = ? AND TITLE LIKE ?\n";
        if (startTime != null) sql += "  AND DATE(START_TIME) >= ?\n";
        if (endTime != null) sql += "  AND DATE(END_TIME) <= ?";


        List<GroupCounsel> groupCounselList = new ArrayList<>();
        try (Connection pcon = DbConn.getConnection(this); PreparedStatement preparedStatement = pcon.prepareStatement(sql)) {
            int i = 0;
            preparedStatement.setString(++i, regNo);
            preparedStatement.setString(++i, "%" + title + "%");
            if (startTime != null) preparedStatement.setDate(++i, new java.sql.Date(startTime.getTime()));
            if (endTime != null) preparedStatement.setDate(++i, new java.sql.Date(endTime.getTime()));
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    groupCounselList.add(mappingFromTable(resultSet));
                }
            }
        } catch (SQLException e) {
            LogUtility.errorLog("Select from group counsel get error", e);
            throw e;
        }
        for (GroupCounsel item : groupCounselList) {
            item.setGroupCounselMember(getGroupCounselMemberListById(item.getId()));
        }
        return groupCounselList;
    }

    public GroupCounsel getGroupCounselById(int id) throws SQLException {
        String sql = "SELECT C.*,F.FILE_NAME\n" +
                "FROM GROUP_COUNSEL C\n" +
                "       LEFT JOIN UPLOAD_FILES F ON C.FILE_ID = F.ID\n" +
                "WHERE C.ID = ?";
        List<GroupCounsel> groupCounselList = new ArrayList<>();

        try (Connection pcon = DbConn.getConnection(this); PreparedStatement preparedStatement = pcon.prepareStatement(sql)) {
            int i = 0;
            preparedStatement.setInt(++i, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    GroupCounsel groupCounsel = mappingFromTable(resultSet);
                    groupCounselList.add(groupCounsel);
                }
            }
        } catch (SQLException e) {
            LogUtility.errorLog("Select from group counsel get error", e);
            throw e;
        }
        for (GroupCounsel item : groupCounselList) {
            item.setGroupCounselMember(getGroupCounselMemberListById(item.getId()));
        }
        if (groupCounselList.isEmpty()) return null;
        else return groupCounselList.get(0);
    }

    public void updateGroupCounsel(GroupCounsel groupCounsel) throws SQLException {
        String sql = "UPDATE GROUP_COUNSEL\n" +
                "SET START_TIME=?,\n" +
                "    END_TIME=?,\n" +
                "    TITLE=?,\n" +
                "    FILE_ID=?,\n" +
                "    LOCATION=?,\n" +
                "    DESCRIPTION=?,\n" +
                "    COUNSELOR=?,\n" +
                "    SUBMITTER=?\n" +
                "WHERE ID = ?";
        try (Connection connection = DbConn.getConnection(this); PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setCounselPreparedStatement(preparedStatement, groupCounsel);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Update group counsel failed, no rows affected.");
            }
        } catch (SQLException e) {
            LogUtility.errorLog("Update group counsel from group counsel get error", e);
            throw e;
        }
        deleteGroupCounselMember(groupCounsel.getId());

        for (GroupCounselMember groupCounselMember : groupCounsel.getGroupCounselMember()) {
            groupCounselMember.setGroupCounselId(groupCounsel.getId());
            insertGroupCounselMember(groupCounselMember);
        }
    }

    private void insertGroupCounselMember(GroupCounselMember groupCounselMember) throws SQLException {
        String sql = "INSERT INTO GROUP_COUNSEL_MEMBER(GROUP_COUNSEL_ID, RGNO)\n" +
                "VALUES (?, ?)";
        try (Connection pcon = DbConn.getConnection(this); PreparedStatement preparedStatement = pcon.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            int i = 0;
            preparedStatement.setInt(++i, groupCounselMember.getGroupCounselId());
            preparedStatement.setInt(++i, groupCounselMember.getRegisterNumber());
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating group counsel member failed, no rows affected.");
            }
        } catch (SQLException e) {
            LogUtility.errorLog("Insert member from group counsel get error", e);
            throw e;
        }
    }

    private void deleteGroupCounselMember(int groupCounselId) throws SQLException {
        String sql = "DELETE\n" +
                "FROM GROUP_COUNSEL_MEMBER\n" +
                "WHERE GROUP_COUNSEL_ID = ?";
        try (Connection connection = DbConn.getConnection(this); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            int i = 0;
            preparedStatement.setInt(++i, groupCounselId);
            preparedStatement.execute();
        } catch (SQLException e) {
            LogUtility.errorLog("Delete group counsel member from group counsel member get error", e);
            throw e;
        }
    }

    private List<GroupCounselMember> getGroupCounselMemberListById(int id) throws SQLException {
        String sql = "SELECT *\n" +
                "FROM GROUP_COUNSEL_MEMBER\n" +
                "WHERE GROUP_COUNSEL_ID = ?";
        List<GroupCounselMember> groupCounselMemberList = new ArrayList<>();
        try (Connection pcon = DbConn.getConnection(this); PreparedStatement preparedStatement = pcon.prepareStatement(sql)) {
            int i = 0;
            preparedStatement.setInt(++i, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    GroupCounselMember groupCounselMember =
                            new GroupCounselMember(
                                    resultSet.getInt("GROUP_COUNSEL_ID"),
                                    resultSet.getInt("RGNO"));

                    groupCounselMemberList.add(groupCounselMember);
                }

                return groupCounselMemberList;
            }
        } catch (SQLException e) {
            LogUtility.errorLog("Select member from group counsel get error", e);
            throw e;
        }
    }

    private GroupCounsel mappingFromTable(ResultSet resultSet) throws SQLException {
        return new GroupCounsel(
                resultSet.getInt("ID"),
                resultSet.getTimestamp("START_TIME"),
                resultSet.getTimestamp("END_TIME"),
                resultSet.getString("TITLE"),
                resultSet.getString("FILE_NAME"),
                resultSet.getInt("FILE_ID"),
                resultSet.getString("LOCATION"),
                resultSet.getString("DESCRIPTION"),
                null,
                resultSet.getString("COUNSELOR"),
                resultSet.getString("SUBMITTER"),
                resultSet.getTimestamp("CREATED_AT")
        );
    }
}
