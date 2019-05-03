package model;

import dao.AccountDao;
import dao.impl.AccountDaoImpl;
import util.DateUtils;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class CourseRecord {
    public List<CourseRecordDocument> getCourseRecordDocumentList() {
        return courseRecordDocumentList;
    }

    public void setCourseRecordDocumentList(List<CourseRecordDocument> courseRecordDocumentList) {
        this.courseRecordDocumentList = courseRecordDocumentList;
    }

    public boolean getIsUnlockStatus() {
        return "5".equals(this.status) && unlock;
    }

    public void setIsUnlockStatus(boolean unlock) {
        this.unlock = "5".equals(this.status) && unlock;
    }

    public boolean isSubmittable() {
        return isSubmittable;
    }

    public void setSubmittable(boolean submittable) {
        isSubmittable = submittable;
    }

    public boolean isVerifyable() {
        return isVerifyable;
    }

    public void setVerifyable(boolean verifyable) {
        isVerifyable = verifyable;
    }

    public Integer getRg_no() {
        return rg_no;
    }

    public void setRg_no(Integer rg_no) {
        this.rg_no = rg_no;
    }

    public static class CourseStatus {
        public static final String 未送出 = "1";
        public static final String 未認證 = "2";
        public static final String 驗證成功 = "3";
        public static final String 驗證失敗 = "4";
        public static final String 已勾選 = "5";
    }

    private String stu_name;
    private String id;
    private String reg_no;
    private Integer rg_no;
    private String term_year;
    private String term_sem;
    private String course_num;
    private String course_cname;
    private String content;
    private String status;
    private String statusC;
    private Timestamp submitted_at;
    private Timestamp created_at;
    private Timestamp deleted_at;
    private Timestamp verify_at;
    private Course course;
    private String verify_message;
    private String verifier_staff_code;
    private String verify_name;
    private String check;//0未勾選1勾選
    private List<CourseRecordDocument> courseRecordDocumentList;
    private boolean unlock;
    private boolean isSubmittable;
    private boolean isVerifyable;

    public CourseRecord(String id, String reg_no,Integer rg_no, String term_year, String term_sem, String course_num, String course_cname, String content, String status, Timestamp submitted_at, Timestamp created_at, Timestamp deleted_at, String verify_name, Timestamp verify_at, String verify_message, String verifier_staff_code, String stu_name, String check) {
        this.id = id;
        this.reg_no = reg_no;
        this.rg_no = rg_no;
        this.term_year = term_year;
        this.term_sem = term_sem;
        this.course_num = course_num;
        this.course_cname = course_cname;
        this.content = content;
        this.status = status;
        this.submitted_at = submitted_at;
        this.created_at = created_at;
        this.deleted_at = deleted_at;
        this.verify_at = verify_at;
        this.verifier_staff_code = verifier_staff_code;
        this.verify_message = verify_message;
        this.stu_name = stu_name;
        this.check = check;
        this.statusC = getStatusC();
        this.verify_name = getVerify_name();
        //TODO 檢查是否解鎖
        if(status.equals("5")) {
            unlock = true;
        }
        else
            unlock = false;

    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReg_no() {
        return reg_no;
    }

    public void setReg_no(String reg_no) {
        this.reg_no = reg_no;
    }

    public String getTerm_year() {
        return term_year;
    }

    public void setTerm_year(String term_year) {
        this.term_year = term_year;
    }

    public String getTerm_sem() {
        return term_sem;
    }

    public void setTerm_sem(String term_sem) {
        this.term_sem = term_sem;
    }

    public String getCourse_num() {
        return course_num;
    }

    public void setCourse_num(String course_num) {
        this.course_num = course_num;
    }

    public String getCourse_cname() {
        return course_cname;
    }

    public void setCourse_cname(String course_cname) {
        this.course_cname = course_cname;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getSubmitted_at() {
        return submitted_at;
    }

    public void setSubmitted_at(Timestamp submitted_at) {
        this.submitted_at = submitted_at;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public Timestamp getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(Timestamp deleted_at) {
        this.deleted_at = deleted_at;
    }

    public String getStu_name() {
        return stu_name;
    }

    public void setStu_name(String stu_name) {
        this.stu_name = stu_name;
    }

    public Timestamp getVerify_at() {
        return verify_at;
    }

    public void setVerify_at(Timestamp verify_at) {
        this.verify_at = verify_at;
    }

    public String getVerify_message() {
        if (verify_message == null)
            return "--";
        return verify_message;
    }

    public void setVerify_message(String verify_message) {
        this.verify_message = verify_message;
    }

    public String getVerifier_staff_code() {
        return verifier_staff_code;
    }

    public void setVerifier_staff_code(String verifier_staff_code) {
        this.verifier_staff_code = verifier_staff_code;
    }


    public String getVerify_name() {
        String Cname = "--";
        if (verifier_staff_code == null) {
            return Cname;
        }

        return verify_name;
    }

    public void setVerify_name(String verify_name) {
        this.verify_name = verify_name;
    }

    public String getSubmittedDateString() {
        if (submitted_at == null)
            return "";
        return DateUtils.formatDateTime(submitted_at);
    }

    public String getVerifyDateString() {
        if (verify_at == null)
            return "";
        return DateUtils.formatDateTime(verify_at);
    }

    public boolean student_modifiable() {
        if (status.equals("1") || status.equals("4"))
            return true;
        else
            return false;
    }


    public String getStatusC() {
        String[] StatusC = {"", "未送出", "未認證", "驗證成功", "驗證失敗", "已勾選","已上傳全國"};
        return StatusC[Integer.parseInt(status)];
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }
}
