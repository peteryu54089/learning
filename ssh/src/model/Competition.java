package model;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by David on 2017/7/25.
 */
public class Competition extends PerformanceBaseModel {
    private String name;
    private String item;
    private String field;
    private String level;
    private String award;
    private String time;
    private String type;

    public Competition(
            int rgno,
            String content, String source,
            String name, String item, String level,
            String award, String time, String type, String field,
            Integer documentFileId, Integer videoFileId, String externalLink
    ) {
        super(rgno, content, source, documentFileId, videoFileId, externalLink);

        this.name = name;
        this.item = item;
        this.level = level;
        this.award = award;
        this.time = time;
        this.type = type;
        this.field = field;
    }

    public Competition(
            Integer id, int rgno,
            String content, String source,
            String name, String item, String level,
            String award, String time, String type, String field,
            Integer documentFileId, Integer videoFileId, String externalLink,
            String submitter, String selectedYear, Integer check, String status,
            Date createdAt
    ) {
        super(id, rgno, content, source, documentFileId, videoFileId, externalLink, submitter, selectedYear, check, status, createdAt);

        this.name = name;
        this.item = item;
        this.level = level;
        this.award = award;
        this.time = time;
        this.type = type;
        this.field = field;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getLevel() {
        return level;
    }

    public String getLevelC() {
        String[] StatusC = {"", "校級", "縣市級", "全國", "國際"};
        return StatusC[Integer.parseInt(level)];
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getAward() {
        return award;
    }

    public void setAward(String award) {
        this.award = award;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public String getTypeC() {
        String[] TypeC = {"", "個人", "團體"};
        return TypeC[Integer.parseInt(type)];
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}
