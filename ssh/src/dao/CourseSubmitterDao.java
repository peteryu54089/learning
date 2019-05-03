package dao;

import model.role.CourseSubmitter;

import java.sql.SQLException;

/**
 * Created by Ching Yun Yu on 2018/4/27.
 */
public interface CourseSubmitterDao extends RoleDao {
    CourseSubmitter getCourseSubmitterByRegNumber(String regNumber) throws SQLException;
}
