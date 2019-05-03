package dao;

import model.Course;

import java.sql.SQLException;
import java.util.List;

public interface TeachingCourseDao {

    List<Course> getTeachingCourse(String staff_code, String term_year, String term_sem, String class_name, String course_name, Integer page) throws SQLException;

    Integer getPageNumber(String staff_code, String term_year, String term_sem, String class_name, String course_name) throws SQLException;

    //Course GetTeachingCourseByCourseNo(String course_no) throws SQLException;
}
