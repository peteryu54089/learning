package dao;


import model.role.Student;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Victor on 2017/7/10.
 */
public interface StudentDao extends RoleDao {
    Student getStudentByIdNumber(String idno) throws SQLException;

    Student getStudentByRegNumber(String termYear, String termSem, String reg_no) throws SQLException;

    Integer getRgnoFromRegNo(String termYear, String termSem, String reg_no) throws SQLException;

    Student getStudentByRegNumber(String reg_no) throws SQLException;

    Student getStudentByRgno(Integer rgno);

    List<Student> getStudentByRgno(List<Integer> rgno);

    List<Student> getStudentList(String register_number, String year, String semester, String classCode, String stu_Cname, String idno, String staff_code, Integer page) throws SQLException;

    List<Student> getStudentListByClass(String year, String semester, String classCode) throws SQLException;

    List<Student> getStudentListByGrade(String year, String semester,String grade) throws SQLException;

    Integer getPageNumber(String register_number, String year, String semester, String classCode, String stu_Cname, String idno, String staff_code, Integer page) throws SQLException;

    Integer getTotalAmount(String register_number, String year, String semester, String classCode, String stu_Cname, String idno, String staff_code) throws SQLException;

    boolean updateStudent(Student student);
}
