package dao;

import model.Document;

import java.sql.SQLException;
import java.util.List;

public interface DocumentDao {
    void uploadDocument(Integer rgno, String topic, String description, Integer year, Integer sem,Integer upload_file_id) throws SQLException;

    void deleteDocument(Integer rgno, int id) throws SQLException;

    Integer getPageNumber(Integer rgno) throws SQLException;

    List<Document> getDocument(Integer rgno) throws SQLException;

    List<Document> getDocument(Integer rgno, Integer page) throws SQLException;

    Document findDocument(int id, Integer rgno) throws SQLException;
}
