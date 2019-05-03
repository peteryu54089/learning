package model;

import java.util.Date;

/**
 * Created by David on 2017/7/31.
 */
public class License extends PerformanceBaseModel {
    private String code;
    private String note;
    private String point;
    private String result;
    private String time;
    private String licensenumber;
    private String group;

    public License(
            int rgno,
            String content, String source,
            String code, String note,
            String point, String result, String time,
            String licensenumber, String group,
            Integer documentFileId, Integer videoFileId, String externalLink
    ) {
        super(rgno, content, source, documentFileId, videoFileId, externalLink);
        this.code = code;
        this.note = note;
        this.point = point;
        this.result = result;
        this.time = time;
        this.licensenumber = licensenumber;
        this.group = group;
    }

    public License(
            Integer id, int rgno,
            String content, String source,
            String code, String note,
            String point, String result, String time,
            String licensenumber, String group,
            Integer documentFileId, Integer videoFileId, String externalLink,
            String submitter, String selectedYear, Integer check, String status,
            Date createdAt
    ) {
        super(id, rgno, content, source, documentFileId, videoFileId, externalLink, submitter, selectedYear, check, status, createdAt);

        this.code = code;
        this.note = note;
        this.point = point;
        this.result = result;
        this.time = time;
        this.licensenumber = licensenumber;
        this.group = group;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLicensenumber() {
        return licensenumber;
    }

    public void setLicensenumber(String licensenumber) {
        this.licensenumber = licensenumber;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
