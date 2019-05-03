package dao;

import model.Volunteer;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by David on 2017/8/3.
 */
public interface VolunteerDao {
    void insertVolunteer(Volunteer volunteer) throws SQLException;

    void updateVolunteerByID(Volunteer volunteer, Integer rgno, Integer id) throws SQLException;

    List<Volunteer> getVolunteer(Integer rgno, String check, String status, String selectYear, Integer page) throws SQLException;

    Integer getPageNumber(Integer rgno) throws SQLException;

    Volunteer getVolunteerByID(Integer rgno, Integer id) throws SQLException;

    void deleteVolunteer(Integer rgno, Integer id) throws SQLException;

    List<Volunteer> getExportStuVolunteerRecords(int year);

}
