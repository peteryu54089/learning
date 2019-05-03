package dao;


import model.Account;
import model.role.Teacher;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by David on 2017/8/6. Changed Architecture by Jeffrey on 2018/5/31.
 */
public interface TeacherDao extends RoleDao {

    Teacher getTeacherByRegNumber(String regNumber) throws SQLException;

}
