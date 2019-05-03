package model;

/**
 * Created by Ching Yun Yu on 2018/5/7.
 */
public class SubmitCourse {
    private String term_year;
    private String term_semester;
    private String unit;
    private String number;
    private String register_number;
    private String ename;
    private String cname;
    private String pdf;

    public SubmitCourse() {
    }

    public SubmitCourse(String term_year, String term_semester, String unit, String number, String register_number, String ename, String cname, String pdf) {
        this.term_year = term_year;
        this.term_semester = term_semester;
        this.unit = unit;
        this.number = number;
        this.register_number = register_number;
        this.ename = ename;
        this.cname = cname;
        this.pdf = pdf;
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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getRegister_number() {
        return register_number;
    }

    public void setRegister_number(String register_number) {
        this.register_number = register_number;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getPdf() {
        return pdf;
    }

    public void setPdf(String pdf) {
        this.pdf = pdf;
    }
}
