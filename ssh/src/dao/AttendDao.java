package dao;

import model.ArCode;
import model.Attend;

import java.sql.SQLException;
import java.util.List;

public interface AttendDao {

    List<Attend> getAttend(Integer rgno, String term_year, String term_sem, String ar_code, Integer page) throws SQLException;

    Integer getPageNumber(Integer rgno, String term_year, String term_sem, String ar_code) throws SQLException;

    Integer getCount(Integer rgno, String term_year, String term_sem, String ar_code) throws SQLException;

    List<Attend> getCalenderByDate(Integer rgno, String start, String end) throws SQLException;

    List<ArCode> getArCodeList() throws SQLException;
}
