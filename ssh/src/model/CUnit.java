package model;

public class CUnit {

    private Integer sbj_year; //開課年
    private Integer sbj_semester; //開課學期
    private Integer grade; // 年級
    private String cls_code; //班級號碼
    private String staff_code; //導師號碼
    private String cunit_name; //班級名稱

    public CUnit() {
    }

    public CUnit(Integer year, Integer semester, String cls_code, String staff_code, String cunit_name) {
        this.sbj_year = year;
        this.sbj_semester = semester;
        this.cls_code = cls_code;
        this.staff_code = staff_code;
        this.cunit_name = cunit_name;
    }

    public Integer getSbj_year() {
        return sbj_year;
    }

    public void setSbj_year(Integer sbj_year) {
        this.sbj_year = sbj_year;
    }

    public Integer getSbj_semester() {
        return sbj_semester;
    }

    public void setSbj_semester(Integer sbj_semester) {
        this.sbj_semester = sbj_semester;
    }

    public String getCls_code() {
        return cls_code;
    }

    public void setCls_code(String cls_code) {
        this.cls_code = cls_code;
    }

    public String getStaff_code() {
        return staff_code;
    }

    public void setStaff_code(String staff_code) {
        this.staff_code = staff_code;
    }

    public String getCunit_name() {
        return cunit_name;
    }

    public void setCunit_name(String cunit_name) {
        this.cunit_name = cunit_name;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }
}
