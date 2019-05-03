package dao.impl;


import dao.ResumeDao;
import dbconn.DbConn;
import model.HasSchoolSchema;
import model.Resume;
import util.StringUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ResumeDaoImpl extends BaseDao implements ResumeDao {
    //TODO 頁數整合
    public final static Integer RECORD_PER_PAGE = 2;

    public ResumeDaoImpl(HasSchoolSchema hasSchoolSchemaObject) {
        super(hasSchoolSchemaObject);
    }

    @Override
    public void uploadResume(String reg_no, String topic, String description, String filename, Integer year, Integer sem, String original_filename) throws SQLException {

        String sql = "INSERT INTO RESUME (REGISTER_NUMBER, TOPIC, DESCRIPTION,FILE_PATH,TERM_YEAR,TERM_SEMESTER,ORIGINAL_FILENAME) VALUES (?, ?, ?, ?,?,?,?)";
        PreparedStatement preparedStatement = null;
        try (Connection pcon = DbConn.getConnection(this)) {
            preparedStatement = pcon.prepareStatement(sql);
            preparedStatement.setString(1, reg_no);
            preparedStatement.setString(2, topic);
            preparedStatement.setString(3, description);
            preparedStatement.setString(4, filename);
            preparedStatement.setInt(5, year);
            preparedStatement.setInt(6, sem);
            preparedStatement.setString(7, original_filename);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null)
                preparedStatement.close();
        }
    }

    @Override
    public void deleteResume(String reg_no, int id) throws SQLException {
        String sql = "UPDATE Resume SET DELETED_AT = ? WHERE REGISTER_NUMBER = ? AND ID = ?";
        PreparedStatement preparedStatement = null;
        try (Connection pcon = DbConn.getConnection(this)) {
            preparedStatement = pcon.prepareStatement(sql);
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            preparedStatement.setTimestamp(1, timestamp);
            preparedStatement.setString(2, reg_no);
            preparedStatement.setInt(3, id);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null)
                preparedStatement.close();
        }
    }

    @Override
    public Integer getPageNumber(String reg_no) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Resume WHERE REGISTER_NUMBER=? AND DELETED_AT IS NULL";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int pagenumber = 1;
        try (Connection pcon = DbConn.getConnection(this)) {
            preparedStatement = pcon.prepareStatement(sql);
            preparedStatement.setString(1, reg_no);
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
    public List<Resume> getResume(String reg_no) throws SQLException {
        String sql = "SELECT * FROM Resume WHERE REGISTER_NUMBER=? AND DELETED_AT IS NULL";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Resume> resumeList = new ArrayList<>();
        try (Connection pcon = DbConn.getConnection(this)) {
            preparedStatement = pcon.prepareStatement(sql);
            preparedStatement.setString(1, reg_no);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Resume resume = convertResultToInstance(resultSet);
                resumeList.add(resume);
            }
            return resumeList;
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
    public List<Resume> getResume(String reg_no, Integer page) throws SQLException {
        String sql = "SELECT * FROM (\n" +
                "SELECT ROW_NUMBER() OVER (ORDER BY ID) AS RN,Resume.*\n" +
                "FROM Resume WHERE REGISTER_NUMBER=? AND DELETED_AT IS NULL)\n" +
                "WHERE RN BETWEEN ? AND ?\n";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Resume> resumetList = new ArrayList<>();
        try (Connection pcon = DbConn.getConnection(this)) {
            preparedStatement = pcon.prepareStatement(sql);
            preparedStatement.setString(1, reg_no);
            preparedStatement.setInt(2, ((page - 1) * RECORD_PER_PAGE) + 1);
            preparedStatement.setInt(3, ((page) * RECORD_PER_PAGE));
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Resume resume = convertResultToInstance(resultSet);
                resumetList.add(resume);
            }
            return resumetList;
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
    public String GetOriginalFileName(String reg_no, String filename) throws SQLException {
        String sql = "SELECT * FROM RESUME WHERE REGISTER_NUMBER=? AND FILE_PATH = ?";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try (Connection pcon = DbConn.getConnection(this)) {
            preparedStatement = pcon.prepareStatement(sql);
            preparedStatement.setString(1, reg_no);
            preparedStatement.setString(2, filename);
            resultSet = preparedStatement.executeQuery();
            String originalname = "";
            if (resultSet.next()) {
                originalname = resultSet.getString("original_filename");
            }
            return originalname;
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
    public Resume findResume(int id, String regNo) throws SQLException {
        String sql = "SELECT * FROM RESUME WHERE ID = ? ";

        if (!StringUtils.isNullOrEmpty(regNo)) {
            sql += " AND REGISTER_NUMBER = ?";
        }

        try (Connection pcon = DbConn.getConnection(this)) {
            try (PreparedStatement ps = pcon.prepareStatement(sql)) {
                int i = 0;
                ps.setInt(++i, id);
                if (!StringUtils.isNullOrEmpty(regNo))
                    ps.setString(++i, regNo);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return convertResultToInstance(rs);
                    }
                }
            }
        }

        return null;
    }

    private Resume convertResultToInstance(ResultSet resultSet) throws SQLException {
        return new Resume(
                resultSet.getInt("ID"),
                resultSet.getString("register_number"),
                resultSet.getString("topic"),
                resultSet.getString("description"),
                resultSet.getString("file_path"),
                resultSet.getTimestamp("created_at"),
                resultSet.getInt("term_year"),
                resultSet.getInt("term_semester"),
                resultSet.getString("original_filename")
        );
    }
}
