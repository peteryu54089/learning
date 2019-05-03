package dao;

import model.CourseRecord;

import java.sql.SQLException;
import java.util.List;

public interface CourseRecordDao {

    List<CourseRecord> getCourseRecord(Integer rgno, String term_year, String term_sem, String course_num, String course_cname, String status, String stu_name, boolean tutor, String check, Integer page) throws SQLException;

    Integer getPageNumber(Integer rgno, String term_year, String term_sem, String course_num, String course_cname, String status, String stu_name, boolean tutor) throws SQLException;

    Integer uploadCourseRecord(Integer rgno, Integer term_year, Integer term_sem, String course_num, String course_cname, String content) throws SQLException;

    CourseRecord getCourseRecordByID(Integer rgno, String ID) throws SQLException;

    void updateCourseRecordByID(Integer rgno, String ID, CourseRecord courseRecord) throws SQLException;

    void deleteCourseRecord(Integer rgno, String ID) throws SQLException;

    List<CourseRecord> getCourseRecord(Integer rgno, String term_year, String term_sem, List<String> course_num, String course_cname, String status, String stu_name, boolean tutor, String check, Integer page) throws SQLException;
}

