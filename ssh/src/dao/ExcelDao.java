package dao;

import model.Account;
import model.Course;
import model.role.Teacher;

import java.sql.SQLException;

public interface ExcelDao {

    void insertToVcourse(Course course) throws SQLException;

    void insertToVcourseSel(Course course) throws SQLException;

    void insertToVcourseTea(Course course) throws SQLException;

    void insertToVstaff(Teacher teacher) throws SQLException;

    void insertToVstu(Account account) throws SQLException;
}
