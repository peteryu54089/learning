package model;

import java.util.Date;

public class RecordSubmitRecord {
    private int year;
    private int sem;
    private Date submittedAt;
    private String idno;
    private int cnt;

    public RecordSubmitRecord(int year, int sem, Date submittedAt, String idno, int cnt) {
        this.year = year;
        this.sem = sem;
        this.submittedAt = submittedAt;
        this.idno = idno;
        this.cnt = cnt;
    }

    public RecordSubmitRecord(int year, int sem, Date submittedAt, int cnt) {
        this(year, sem, submittedAt, "", cnt);
    }

    public int getYear() {
        return year;
    }

    public int getSem() {
        return sem;
    }

    public Date getSubmittedAt() {
        return submittedAt;
    }

    public String getIdno() {
        return idno;
    }

    public int getCnt() {
        return cnt;
    }
}
