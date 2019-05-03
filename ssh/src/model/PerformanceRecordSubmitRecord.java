package model;

import java.util.Date;

public class PerformanceRecordSubmitRecord extends RecordSubmitRecord {
    private int cadSchool;
    private int cadSelf;
    private int com;
    private int lic;
    private int vol;
    private int oth;

    public PerformanceRecordSubmitRecord(int year, Date submittedAt, String idno, int cadSchool, int cadSelf, int com, int lic, int vol, int oth) {
        super(year, 0, submittedAt, idno, 0);
        this.cadSchool = cadSchool;
        this.cadSelf = cadSelf;
        this.com = com;
        this.lic = lic;
        this.vol = vol;
        this.oth = oth;
    }

    public PerformanceRecordSubmitRecord(int year, Date submittedAt, int cadSchool, int cadSelf, int com, int lic, int vol, int oth) {
        super(year, 0, submittedAt, "", 0);
        this.cadSchool = cadSchool;
        this.cadSelf = cadSelf;
        this.com = com;
        this.lic = lic;
        this.vol = vol;
        this.oth = oth;
    }

    public int getCnt() {
        throw new UnsupportedOperationException();
    }

    public int getCadSchool() {
        return cadSchool;
    }

    public int getCadSelf() {
        return cadSelf;
    }

    public int getCom() {
        return com;
    }

    public int getLic() {
        return lic;
    }

    public int getVol() {
        return vol;
    }

    public int getOth() {
        return oth;
    }
}
