package model;

/**
 * Created by Ching Yun Yu on 2018/5/7.
 */
public class SubmitPerformance {
    private String term_year;
    private String term_semester;
    private String unit;
    private String register_number;
    private String name;
    private String cadre;
    private String competition;
    private String license;
    private String volunteer;
    private String other;
    private String course;

    public SubmitPerformance() {
    }

    public SubmitPerformance(String term_year, String term_semester, String unit, String register_number, String name, String cadre, String competition, String license, String volunteer, String other, String course) {
        this.term_year = term_year;
        this.term_semester = term_semester;
        this.unit = unit;
        this.register_number = register_number;
        this.name = name;
        this.cadre = cadre;
        this.competition = competition;
        this.license = license;
        this.volunteer = volunteer;
        this.other = other;
        this.course = course;
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

    public String getRegister_number() {
        return register_number;
    }

    public void setRegister_number(String register_number) {
        this.register_number = register_number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCadre() {
        return cadre;
    }

    public void setCadre(String cadre) {
        this.cadre = cadre;
    }

    public String getCompetition() {
        return competition;
    }

    public void setCompetition(String competition) {
        this.competition = competition;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getVolunteer() {
        return volunteer;
    }

    public void setVolunteer(String volunteer) {
        this.volunteer = volunteer;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }
}
