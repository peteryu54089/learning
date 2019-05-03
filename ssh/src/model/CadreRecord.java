package model;

public class CadreRecord {

    private String id;
    private String term_year;
    private String term_sem;
    private String booklet;

    public CadreRecord() {
    }

    public CadreRecord(String id, String term_year, String term_sem, String booklet) {
        this.id = id;
        this.term_year = term_year;
        this.term_sem = term_sem;
        this.booklet = booklet;
    }

    public String getId() {
        return id;
    }

    public String getTerm_year() {
        return term_year;
    }

    public String getTerm_sem() {
        return term_sem;
    }

    public String getBooklet() {
        return booklet;
    }

}
