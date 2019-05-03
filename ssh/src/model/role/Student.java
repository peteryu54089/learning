package model.role;

/**
 * Created by David on 2017/7/23.
 */
public class Student extends Role {
    private String term_year; //哪個年度的資料
    private String term_semester; //哪個學期的資料
    private String classCode; //班號
    private String className; //班級名稱
    private String class_no;//座號
    private String stu_Cname;//學生名稱
    private String stu_Ename;//英文名稱
    private String idno;
    private String email;
    private String mobile_telno;
    private String staff_code;//導師號碼
    private Integer rgno;//RGNO

    private String birth;
    private String avatar;
    private String socialAccount;
    private String bio;
    private String nickname;
    private String interest;
    private String altMail;

    private boolean courseRecordUnlock;
    private boolean performanceUnlock;

    public Student(String register_number, String year, String semester,
                   String classCode, String className, String class_no,
                   String stu_Cname, String stu_Ename, String idno,
                   String birth, String email, String mobile_telno,
                   String staff_code, Integer rgno,
                   String avatar, String socialAccount,
                   String bio, String nickname,
                   String interest, String altMail) {
        super(register_number);
        this.term_year = year;
        this.term_semester = semester;
        this.classCode = classCode;
        this.className = className;
        this.class_no = class_no;
        this.birth = birth;
        this.stu_Cname = stu_Cname;
        this.stu_Ename = stu_Ename;
        this.idno = idno;
        this.email = email;
        this.mobile_telno = mobile_telno;
        this.staff_code = staff_code;
        this.rgno = rgno;
        this.avatar = avatar;
        this.socialAccount = socialAccount;
        this.bio = bio;
        this.nickname = nickname;
        this.interest = interest;
        this.altMail = altMail;
    }

    public String getTerm_year() {
        return term_year;
    }

    public void setTerm_year(String term_year) {
        this.term_year = term_year;
    }

    public String getTerm_semester() {
        return term_semester;
    }

    public void setTerm_semester(String term_semester) {
        this.term_semester = term_semester;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClass_no() {
        return class_no;
    }

    public void setClass_no(String class_no) {
        this.class_no = class_no;
    }

    public String getStu_Cname() {
        return stu_Cname;
    }

    public void setStu_Cname(String stu_Cname) {
        this.stu_Cname = stu_Cname;
    }

    public String getStu_Ename() {
        return stu_Ename;
    }

    public void setStu_Ename(String stu_Ename) {
        this.stu_Ename = stu_Ename;
    }

    public String getIdno() {
        return idno;
    }

    public void setIdno(String idno) {
        this.idno = idno;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile_telno() {
        return mobile_telno;
    }

    public void setMobile_telno(String mobile_telno) {
        this.mobile_telno = mobile_telno;
    }

    public String getStaff_code() {
        return staff_code;
    }

    public void setStaff_code(String staff_code) {
        this.staff_code = staff_code;
    }



    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getSocialAccount() {
        return socialAccount;
    }

    public void setSocialAccount(String socialAccount) {
        this.socialAccount = socialAccount;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getAltMail() {
        return altMail;
    }

    public void setAltMail(String altMail) {
        this.altMail = altMail;
    }

    public boolean isCourseRecordUnlock() {
        return courseRecordUnlock;
    }

    public void setCourseRecordUnlock(boolean courseRecordUnlock) {
        this.courseRecordUnlock = courseRecordUnlock;
    }


    public boolean isPerformanceUnlock() {
        return performanceUnlock;
    }

    public void setPerformanceUnlock(boolean performanceUnlock) {
        this.performanceUnlock = performanceUnlock;
    }

    public Integer getRgno() {
        return rgno;
    }

    public void setRgno(Integer rgno) {
        this.rgno = rgno;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    @Override
    public String getMenuBar() {
        return "menuBar/studentBar.jsp";
    }
}
