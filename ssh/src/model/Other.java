package model;

import java.util.Date;

public class Other extends PerformanceBaseModel {
    private String name;
    private String unit;
    private String startTime;
    private String endTime;
    private String count;
    private String type;
    // ST: 自主學習 WS: 作品成果 OA: 其他活動


    public Other(
            int rgno,
            String content, String source,
            String name, String unit,
            String startTime, String endTime,
            String count, String type,
            Integer documentFileId, Integer videoFileId, String externalLink
    ) {
        super(rgno, content, source, documentFileId, videoFileId, externalLink);
        this.name = name;
        this.unit = unit;
        this.startTime = startTime;
        this.endTime = endTime;
        this.count = count;
        this.type = type;
    }

    public Other(
            Integer id, int rgno,
            String content, String source,
            String name, String unit,
            String startTime, String endTime,
            String count, String type,
            Integer documentFileId, Integer videoFileId, String externalLink,
            String submitter, String selectedYear, Integer check, String status,
            Date createdAt
    ) {
        super(id, rgno, content, source, documentFileId, videoFileId, externalLink, submitter, selectedYear, check, status, createdAt);

        this.name = name;
        this.unit = unit;
        this.startTime = startTime;
        this.endTime = endTime;
        this.count = count;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
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

    public String getType() {
        return type;
    }


    public void setType(String type) {
        this.type = type;
    }

    public static class Type {
        public static final String SELF_TAUGHT = "ST";
        public static final String WROK_RESULT = "WR";
        public static final String OTHER_ACTIVITY = "OA";

        private Type() {
        }
    }

    public String getTypeC() {
        String other_type = "";
        switch (type) {
            case "ST":
                other_type = "自主學習";
                break;
            case "WR":
                other_type = "作品成果";
                break;
            case "OA":
                other_type = "其他活動";
                break;
        }
        return other_type;
    }
}
