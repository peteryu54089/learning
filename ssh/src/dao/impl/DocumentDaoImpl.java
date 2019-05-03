package dao.impl;

import dao.DocumentDao;
import dao.UploadFileDao;
import dbconn.DbConn;
import model.Document;
import model.HasSchoolSchema;
import model.UploadFile;
import util.StringUtils;

import java.sql.*;
import java.util.*;

public class DocumentDaoImpl extends BaseDao implements DocumentDao {
    //TODO 頁數整合
    public final static Integer RECORD_PER_PAGE = 2;

    public DocumentDaoImpl(HasSchoolSchema hasSchoolSchemaObject) {
        super(hasSchoolSchemaObject);
    }

    @Override
    public void uploadDocument(Integer rgno, String topic, String description, Integer year, Integer sem,Integer upload_file_id) throws SQLException {

        String sql = "INSERT INTO DOCUMENT (RGNO, TOPIC, DESCRIPTION,TERM_YEAR,TERM_SEMESTER,UPLOAD_FILE_ID) VALUES (?, ?, ?, ?,?,?)";
        PreparedStatement preparedStatement = null;
        try (Connection pcon = DbConn.getConnection(this)) {
            preparedStatement = pcon.prepareStatement(sql);
            preparedStatement.setInt(1, rgno);
            preparedStatement.setString(2, topic);
            preparedStatement.setString(3, description);
            preparedStatement.setInt(4, year);
            preparedStatement.setInt(5, sem);
            preparedStatement.setInt(6, upload_file_id);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null)
                preparedStatement.close();
        }
    }

    @Override
    public void deleteDocument(Integer rgno, int id) throws SQLException {
        String sql = "DELETE FROM DOCUMENT WHERE RGNO = ? AND ID = ?";
        PreparedStatement preparedStatement = null;
        try (Connection pcon = DbConn.getConnection(this)) {
            preparedStatement = pcon.prepareStatement(sql);
            preparedStatement.setInt(1, rgno);
            preparedStatement.setInt(2, id);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null)
                preparedStatement.close();
        }
    }

    @Override
    public Integer getPageNumber(Integer rgno) throws SQLException {
        String sql = "SELECT COUNT(*) FROM DOCUMENT WHERE RGNO=?";
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
    public List<Document> getDocument(Integer rgno) throws SQLException {
        String sql = "SELECT * FROM DOCUMENT WHERE RGNO=?";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Document> documentList = new ArrayList<>();
        try (Connection pcon = DbConn.getConnection(this)) {
            preparedStatement = pcon.prepareStatement(sql);
            preparedStatement.setInt(1, rgno);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Document documment = convertToInstance(resultSet);
                documentList.add(documment);
            }
            setUploadFiles(documentList);
            return documentList;
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
    public List<Document> getDocument(Integer rgno, Integer page) throws SQLException {
        String sql = "SELECT * FROM (\n" +
                "SELECT ROW_NUMBER() OVER (ORDER BY ID) AS RN,DOCUMENT.*\n" +
                "FROM DOCUMENT WHERE RGNO=?)\n" +
                "WHERE RN BETWEEN ? AND ?\n";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Document> documentList = new ArrayList<>();
        try (Connection pcon = DbConn.getConnection(this)) {
            preparedStatement = pcon.prepareStatement(sql);
            preparedStatement.setInt(1, rgno);
            preparedStatement.setInt(2, ((page - 1) * RECORD_PER_PAGE) + 1);
            preparedStatement.setInt(3, ((page) * RECORD_PER_PAGE));
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Document documment = convertToInstance(resultSet);
                documentList.add(documment);
            }
            setUploadFiles(documentList);
            return documentList;
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
    public Document findDocument(int id, Integer rgno) throws SQLException {
        String sql = "SELECT * FROM DOCUMENT WHERE ID = ? ";

        if (rgno != null) {
            sql += " AND RGNO = ?";
        }

        try (Connection pcon = DbConn.getConnection(this)) {
            try (PreparedStatement ps = pcon.prepareStatement(sql)) {
                int i = 0;
                ps.setInt(++i, id);
                if (rgno!=null)
                    ps.setInt(++i, rgno);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        Document d = convertToInstance(rs);
                        setUploadFiles(Collections.singletonList(d));
                        return d;
                    }
                }
            }
        }

        return null;
    }

    private Document convertToInstance(ResultSet resultSet) throws SQLException {
        return new Document(
                resultSet.getInt("id"),
                resultSet.getInt("RGNO"),
                resultSet.getString("topic"),
                resultSet.getString("description"),
                resultSet.getInt("upload_file_id"),
                resultSet.getTimestamp("created_at"),
                resultSet.getInt("term_year"),
                resultSet.getInt("term_semester")
        );
    }


    private void setUploadFiles(List<Document> list) {
        UploadFileDao uploadFileDao = new UploadFileDaoImpl(this);
        Map<Integer, UploadFile> map =  new HashMap<>();
        List<Integer> ids = new ArrayList<>();
        for (Document crd : list) {
            ids.add(crd.getUpload_file_id());
        }
        for (UploadFile file : uploadFileDao.getByIds(ids)) {
            map.put(file.getId(),file);
        }
        for(Document crd : list)
        {
            crd.setUploadFile(map.get(crd.getUpload_file_id()));
        }
    }
}
