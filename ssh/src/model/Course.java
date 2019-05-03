package model;

import java.util.List;

public class Course {
    private String term_year;
    private String term_sem;
    private String class_Cname;
    private String course_Cname;
    private String course_no;
    private String names;//授課教師
    private List<CourseRecord> courseRecordList;
    private boolean isUploadable;

    public Course() {
    }

    public Course(String term_year, String term_sem, String class_Cname, String course_Cname, String course_no, String names) {
        this.term_year = term_year;
        this.term_sem = term_sem;
        this.class_Cname = class_Cname;
        this.course_Cname = course_Cname;
        this.course_no = course_no;
        this.names = names;
    }

    public String getTerm_year() {
        return term_year;
    }

    public String getTerm_sem() {
        return term_sem;
    }

    public String getCourse_no() {
        return course_no;
    }

    public void setCourse_no(String course_no) {
        this.course_no = course_no;
    }

    public List<CourseRecord> getCourseRecordList() {
        return courseRecordList;
    }

    public void setCourseRecordList(List<CourseRecord> courseRecordList) {
        this.courseRecordList = courseRecordList;
    }

    public String getCourse_Cname() {
        return course_Cname;
    }

    public void setCourse_Cname(String course_Cname) {
        this.course_Cname = course_Cname;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getClass_Cname() {
        return class_Cname;
    }

    public void setClass_Cname(String class_Cname) {
        this.class_Cname = class_Cname;
    }

    public boolean isUploadable() {
        return isUploadable;
    }

    public void setUploadable(boolean uploadable) {
        isUploadable = uploadable;
    }
}
