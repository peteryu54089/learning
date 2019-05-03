package model;

import util.DateUtils;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by David on 2017/8/3.
 */
public class Volunteer extends PerformanceBaseModel {
    private String name;
    private String place;
    private String startTime;
    private String endTime;
    private String count;

    public Volunteer(
            int rgno,
            String content, String source,
            String name, String place,
            String startTime, String endTime,
            String count,
            Integer documentFileId, Integer videoFileId, String externalLink
    ) {
        super(rgno, content, source, documentFileId, videoFileId, externalLink);

        this.name = name;
        this.place = place;
        this.startTime = startTime;
        this.endTime = endTime;
        this.count = count;
    }

    public Volunteer(
            Integer id, int rgno,
            String content, String source,
            String name, String place,
            String startTime, String endTime,
            String count,
            Integer documentFileId, Integer videoFileId, String externalLink,
            String submitter, String selectedYear, Integer check, String status,
            Date createdAt
    ) {
        super(id, rgno, content, source, documentFileId, videoFileId, externalLink, submitter, selectedYear, check, status, createdAt);

        this.name = name;
        this.place = place;
        this.startTime = startTime;
        this.endTime = endTime;
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
