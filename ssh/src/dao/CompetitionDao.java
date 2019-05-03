package dao;

import model.Competition;

import java.sql.SQLException;
import java.util.List;

public interface CompetitionDao {
    void insertCompetition(Competition competition) throws SQLException;

    void updateCompetitionByID(Competition competition, Integer rgno, Integer id) throws SQLException;

    List<Competition> getCompetition(Integer rgno, String check, String status, String selectYear, Integer page) throws SQLException;

    Integer getPageNumber(Integer rgno) throws SQLException;

    Competition getCompetitionByID(Integer rgno, Integer id) throws SQLException;

    void deleteCompetition(Integer rgno, Integer id) throws SQLException;

    List<Competition> getExportStuCompetitionRecords(int year);
}
