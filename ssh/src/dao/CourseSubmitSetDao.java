package dao;

import model.CourseSubmitSet;

import java.sql.SQLException;

/**
 * Created by Ching-Yun Yu on 2018/5/31.
 */
public interface CourseSubmitSetDao {
    CourseSubmitSet getCourseSubmitSet() throws SQLException;

    void setCourseSubmitSet(CourseSubmitSet courseSubmitSet) throws SQLException;

    void insertCourseSubmitSet(CourseSubmitSet courseSubmitSet) throws SQLException;
}
