package model;

public class CourseResult {

    private String course_no;
    private String year;
    private String sem;
    private String rgno;
    private String regular_score;
    private String p1_score;
    private String p2_score;
    private String final_score;
    private String sem_score;
    private String sem_star_sign;//是否通過
    private String makeup_score;
    private String makeup_star_sign;//是否通過
    private String course_Cname;
    private String staff_Cnames;

    public CourseResult() {
    }

    public CourseResult(String course_no, String year, String sem, String rgno, String regular_score, String p1_score, String p2_score, String final_score, String sem_score, String sem_star_sign, String makeup_score, String makeup_star_sign, String course_Cname, String staff_Cnames) {
        this.course_no = course_no;
        this.year = year;
        this.sem = sem;
        this.rgno = rgno;
        this.regular_score = regular_score;
        this.p1_score = p1_score;
        this.p2_score = p2_score;
        this.final_score = final_score;
        this.sem_score = sem_score;
        this.sem_star_sign = sem_star_sign;
        this.makeup_score = makeup_score;
        this.makeup_star_sign = makeup_star_sign;
        this.course_Cname = course_Cname;
        this.staff_Cnames = staff_Cnames;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getSem() {
        return sem;
    }

    public void setSem(String sem) {
        this.sem = sem;
    }

    public String getRgno() {
        return rgno;
    }

    public void setRgno(String rgno) {
        this.rgno = rgno;
    }

    public String getRegular_score() {
        return regular_score;
    }

    public void setRegular_score(String regular_score) {
        this.regular_score = regular_score;
    }

    public String getP1_score() {
        return p1_score;
    }

    public void setP1_score(String p1_score) {
        this.p1_score = p1_score;
    }

    public String getFinal_score() {
        return final_score;
    }

    public void setFinal_score(String final_score) {
        this.final_score = final_score;
    }

    public String getP2_score() {
        return p2_score;
    }

    public void setP2_score(String p2_score) {
        this.p2_score = p2_score;
    }

    public String getSem_score() {
        return sem_score;
    }

    public void setSem_score(String sem_score) {
        this.sem_score = sem_score;
    }

    public String getSem_star_sign() {
        return sem_star_sign;
    }

    public void setSem_star_sign(String sem_star_sign) {
        this.sem_star_sign = sem_star_sign;
    }

    public String getMakeup_score() {
        return makeup_score;
    }

    public void setMakeup_score(String makeup_score) {
        this.makeup_score = makeup_score;
    }

    public String getMakeup_star_sign() {
        return makeup_star_sign;
    }

    public void setMakeup_star_sign(String makeup_star_sign) {
        this.makeup_star_sign = makeup_star_sign;
    }

    public String getCourse_Cname() {
        return course_Cname;
    }

    public void setCourse_Cname(String course_Cname) {
        this.course_Cname = course_Cname;
    }

    public String getCourse_no() {
        return course_no;
    }

    public void setCourse_no(String course_no) {
        this.course_no = course_no;
    }

    public String getStaff_Cnames() {
        return staff_Cnames;
    }

    public void setStaff_Cnames(String staff_Cnames) {
        this.staff_Cnames = staff_Cnames;
    }
}

