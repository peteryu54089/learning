package dao.impl;

import dao.AnnouncementDao;
import dbconn.DbConn;
import model.Announcement;
import model.HasSchoolSchema;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AnnouncementDaoImpl extends BaseDao implements AnnouncementDao {
    public AnnouncementDaoImpl(HasSchoolSchema hasSchoolSchemaObject) {
        super(hasSchoolSchemaObject);
    }

    @Override
    public List<Announcement> listAvailableAnnouncements() {
        ArrayList<Announcement> ret = new ArrayList<>();
        String sql = "SELECT\n" +
                "    *\n" +
                "FROM\n" +
                "    ANNOUNCEMENT\n" +
                "WHERE\n" +
                "    PUBLISH_START<=CURRENT TIMESTAMP\n" +
                "AND PUBLISH_END >= CURRENT TIMESTAMP\n" +
                "ORDER BY PUBLISH_START DESC";

        try (Connection pcon = DbConn.getConnection(this)) {
            try (PreparedStatement ps = pcon.prepareStatement(sql)) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        ret.add(getAnnouncement(rs));
                    }

                    return ret;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return ret;
        }

    }

    @Override
    public int getItemCount() {
        String sql = "SELECT\n" +
                "    COUNT(*)\n" +
                "FROM\n" +
                "    ANNOUNCEMENT";

        try (Connection pcon = DbConn.getConnection(this)) {
            try (PreparedStatement ps = pcon.prepareStatement(sql)) {
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public List<Announcement> getAnnouncements(int page) {
        page--;
        List<Announcement> list = new ArrayList<>();
        String sql = "SELECT * FROM (\n" +
                "    SELECT \n" +
                "        ROW_NUMBER() OVER (ORDER BY ID DESC) - 1 AS RN,\n" +
                "        ANN.* FROM ANNOUNCEMENT ANN\n" +
                ") WHERE RN BETWEEN ? AND ?";
        int start = page * ITEM_PER_PAGE;
        int end = start + ITEM_PER_PAGE - 1;
        try (Connection pcon = DbConn.getConnection(this)) {
            try (PreparedStatement ps = pcon.prepareStatement(sql)) {
                ps.setInt(1, start);
                ps.setInt(2, end);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        list.add(getAnnouncement(rs));
                    }

                    return list;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public int insertAnnouncement(Announcement announcement) {
        String sql = "INSERT INTO ANNOUNCEMENT\n" +
                " (CONTENT, PUBLISH_START, PUBLISH_END)\n" +
                " VALUES (?, ?, ?)";

        try (Connection pcon = DbConn.getConnection(this)) {
            try (PreparedStatement ps = pcon.prepareStatement(sql)) {
                Clob clob = pcon.createClob();
                clob.setString(1, announcement.getContent());

                int i = 0;
                ps.setClob(++i, clob);
                ps.setTimestamp(++i, announcement.getPublishStart());
                ps.setTimestamp(++i, announcement.getPublishEnd());

                return ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int updateAnnouncement(Announcement announcement) {
        String sql = "UPDATE ANNOUNCEMENT SET\n" +
                " CONTENT = ?, PUBLISH_START = ?, PUBLISH_END = ?\n" +
                "WHERE ID = ?";

        try (Connection pcon = DbConn.getConnection(this)) {
            try (PreparedStatement ps = pcon.prepareStatement(sql)) {
                Clob clob = pcon.createClob();
                clob.setString(1, announcement.getContent());

                int i = 0;
                ps.setClob(++i, clob);
                ps.setTimestamp(++i, announcement.getPublishStart());
                ps.setTimestamp(++i, announcement.getPublishEnd());
                ps.setInt(++i, announcement.getId());

                return ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public boolean deleteAnnouncement(int id) {
        String sql = "DELETE FROM ANNOUNCEMENT " +
                "WHERE ID = ?";
        try (Connection pcon = DbConn.getConnection(this)) {
            try (PreparedStatement ps = pcon.prepareStatement(sql)) {
                ps.setInt(1, id);

                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static Announcement getAnnouncement(final ResultSet rs) throws SQLException {
        return new Announcement(
                rs.getInt("ID"),
                rs.getClob("CONTENT"),
                rs.getTimestamp("PUBLISH_START"),
                rs.getTimestamp("PUBLISH_END"),
                rs.getTimestamp("CREATED_AT")
        );
    }
}
