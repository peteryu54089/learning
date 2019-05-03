package dao;

import model.Course;

import java.sql.SQLException;
import java.util.List;

public interface SelectCourseDao {

    List<Course> getSelectCourse(Integer rgno, String term_year, String term_sem, String name, Integer page) throws SQLException;

    Integer getPageNumber(Integer rgno, String term_year, String term_sem, String name) throws SQLException;

    Course getSelectCourseByCourseNo(String course_no) throws SQLException;
}
