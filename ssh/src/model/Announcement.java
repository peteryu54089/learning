package model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import util.DbUtils;

import java.sql.Clob;
import java.sql.Timestamp;
import java.util.Date;

public class Announcement {
    private int id;
    private String content;
    private Timestamp publishStart;
    private Timestamp publishEnd;
    private Timestamp createdAt;

    public Announcement(String content, Date publishStart, Date publishEnd, Timestamp createdAt) {
        this(-1, content, publishStart, publishEnd, createdAt);
    }

    public Announcement(int id, String content, Date publishStart, Date publishEnd, Timestamp createdAt) {
        this.id = id;
        this.content = content;
        this.publishStart = new Timestamp(publishStart.getTime());
        this.publishEnd = new Timestamp(publishEnd.getTime());
        this.createdAt = createdAt;
    }

    public Announcement(int id, Clob content, Timestamp publishStart, Timestamp publishEnd, Timestamp createdAt) {
        this(id, DbUtils.convertClobToString(content), publishStart, publishEnd, createdAt);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setContent(Clob content) {
        this.content = DbUtils.convertClobToString(content);
    }

    public Timestamp getPublishStart() {
        return publishStart;
    }

    public void setPublishStart(Timestamp publishStart) {
        this.publishStart = publishStart;
    }

    public Timestamp getPublishEnd() {
        return publishEnd;
    }

    public void setPublishEnd(Timestamp publishEnd) {
        this.publishEnd = publishEnd;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public static Gson getGson() {
        return new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();
    }
}
