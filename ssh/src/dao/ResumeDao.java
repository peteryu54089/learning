package dao;

import model.Resume;

import java.sql.SQLException;
import java.util.List;

public interface ResumeDao {
    void uploadResume(String reg_no, String topic, String description, String filename, Integer year, Integer sem, String original_filename) throws SQLException;

    void deleteResume(String reg_no, int id) throws SQLException;

    Integer getPageNumber(String reg_no) throws SQLException;

    List<Resume> getResume(String reg_no) throws SQLException;

    List<Resume> getResume(String reg_no, Integer page) throws SQLException;

    String GetOriginalFileName(String reg_no, String filename) throws SQLException;

    Resume findResume(int id, String regNo) throws SQLException;
}
