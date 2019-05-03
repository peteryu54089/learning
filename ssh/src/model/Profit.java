package model;

public class Profit {
    private int id;
    private String idno;
    private int term_year;
    private String term_sem;
    private int course_num;
    private String course_cname;
    private String stuRegNo;
    private String stuName;

    public Profit(int id, String idno, int term_year, String term_sem, int course_num, String course_cname, String stuRegNo, String stuName) {
        this.id = id;
        this.idno = idno;
        this.term_year = term_year;
        this.term_sem = term_sem;
        this.course_num = course_num;
        this.course_cname = course_cname;
        this.stuRegNo = stuRegNo;
        this.stuName = stuName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdno() {
        return idno;
    }

    public void setIdno(String idno) {
        this.idno = idno;
    }

    public int getTerm_year() {
        return term_year;
    }

    public void setTerm_year(int term_year) {
        this.term_year = term_year;
    }

    public String getTerm_sem() {
        return term_sem;
    }

    public void setTerm_sem(String term_sem) {
        this.term_sem = term_sem;
    }

    public int getCourse_num() {
        return course_num;
    }

    public void setCourse_num(int course_num) {
        this.course_num = course_num;
    }

    public String getCourse_cname() {
        return course_cname;
    }

    public void setCourse_cname(String course_cname) {
        this.course_cname = course_cname;
    }

    public String getStuRegNo() {
        return stuRegNo;
    }

    public void setStuRegNo(String stuRegNo) {
        this.stuRegNo = stuRegNo;
    }

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }
}
