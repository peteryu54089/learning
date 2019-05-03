package dao;

import model.Other;

import java.sql.SQLException;
import java.util.List;

public interface OtherDao {
    void insertOther(Other other) throws SQLException;

    void updateOtherByID(Other other, Integer rgno, Integer id) throws SQLException;

    List<Other> getOther(Integer rgno, String check, String status, String selectYear, Integer page, String type) throws SQLException;

    Integer getPageNumber(Integer rgno) throws SQLException;

    Other getOtherByID(Integer rgno, Integer id) throws SQLException;

    void deleteOther(Integer rgno, Integer id) throws SQLException;


    List<Other> getExportStuVolunteerRecords(int year, String type);
}
