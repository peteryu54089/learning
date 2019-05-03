package model;

/**
 * Created by David on 2017/10/20.
 */
public class SystemSet {
    private String sStartTime;
    private String sEndTime;
    private String tStartTime;
    private String tEndTime;
    private int tInterval;
    private int size;
    private String uploadType;
    private int termYear;
    private int termSemester;

    public SystemSet(
            String sStartTime, String sEndTime,
            String tStartTime, String tEndTime,
            int tInterval, int size,
            String uploadType,
            int termYear, int termSemester
    ) {
        this.sStartTime = sStartTime;
        this.sEndTime = sEndTime;
        this.tStartTime = tStartTime;
        this.tEndTime = tEndTime;
        this.tInterval = tInterval;
        this.size = size;
        this.uploadType = uploadType;
        this.termYear = termYear;
        this.termSemester = termSemester;
    }

    @Override
    public String toString() {
        return "{\"sStartTime\": \"" + sStartTime + ", \"sEndTime\":\"" + sEndTime + "\", \"tStartTime\":" + tStartTime + "\", \"tEndTime\", \"" + tEndTime + "\", \"tInterval\":" + tInterval + "\", \"size\":" + size + "\"}";
    }
}
