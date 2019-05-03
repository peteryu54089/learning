package model;

import java.util.Date;

public class CourseRecordSubmitRecord extends RecordSubmitRecord {


    public CourseRecordSubmitRecord(int year,int sem, Date submittedAt, String idno, int cnt) {
        super(year, sem, submittedAt, idno, cnt);

    }

    public CourseRecordSubmitRecord(int year,int sem, Date submittedAt, int cnt) {
        super(year, sem, submittedAt, "", cnt);
    }
    public CourseRecordSubmitRecord(int year,int sem, int cnt) {
        super(year, sem, null, "", cnt);
    }
}
