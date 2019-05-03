package dao;

import model.IndividualCounsel;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public interface IndividualCounselDao {

    List<IndividualCounsel> getIndividualCounselByTitleOrDate(java.util.Date startTime, java.util.Date endTime, String title, Integer rgNo) throws SQLException;

    IndividualCounsel getIndividualCounselById(int id) throws SQLException;

    void insertIndividualCounsel(IndividualCounsel individualCounsel) throws SQLException;

    void updateIndividualCounsel(IndividualCounsel individualCounsel) throws SQLException;

    void deleteIndividualCounsel(int id) throws SQLException;
}
