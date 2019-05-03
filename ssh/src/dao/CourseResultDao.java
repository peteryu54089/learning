package dao;

import model.CourseResult;

import java.sql.SQLException;
import java.util.List;

public interface CourseResultDao {

    List<CourseResult> getCourseResult(Integer rgno, String term_year, String term_sem, String name, Integer page) throws SQLException;

    Integer getPageNumber(Integer rgno, String term_year, String term_sem, String name) throws SQLException;
}
