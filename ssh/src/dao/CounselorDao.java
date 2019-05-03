package dao;

import model.role.Counselor;

import java.sql.SQLException;

/**
 * Created by Ching Yun Yu on 2018/4/27.
 */
public interface CounselorDao extends RoleDao {

    Counselor getCounselorByRegNumber(String regNumber) throws SQLException;

    // Counselor getCounselorByStaffCode(String staff_code) throws SQLException;
    //
    // Counselor getCounselorByCourse(Course course) throws SQLException;
    //
    // List<Counselor> getCounselorByFuzzy(String hint) throws SQLException;
    //
    // void deleteCounselorByStaff_code(String staff_code) throws SQLException;
}
