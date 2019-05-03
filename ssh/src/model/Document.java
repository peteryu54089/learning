package model;

import util.DateUtils;

import java.sql.Timestamp;

public class Document {
    private int id;
    private Integer rgno;
    private String topic;
    private String description;
    private Integer upload_file_id;
    private Timestamp created_at;
    private Integer term_year;
    private Integer term_semester;
    private UploadFile uploadFile;
    private String link;



    public Document(int id, Integer rgno, String topic,String description, Integer upload_file_id,  Timestamp created_at, Integer year, Integer sem) {
        this.id = id;
        this.rgno = rgno;
        this.topic = topic;
        this.description = description;
        this.upload_file_id = upload_file_id;
        this.created_at = created_at;
        this.term_year = year;
        this.term_semester = sem;
    }


    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTopic() {
        return topic;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public String getCreatedDateString() {
        return DateUtils.formatDateTime(created_at);
    }

    public Integer getTerm_year() {
        return term_year;
    }

    public Integer getTerm_semester() {
        return term_semester;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getRgno() {
        return rgno;
    }

    public void setRgno(Integer rgno) {
        this.rgno = rgno;
    }

    public Integer getUpload_file_id() {
        return upload_file_id;
    }

    public void setUpload_file_id(Integer upload_file_id) {
        this.upload_file_id = upload_file_id;
    }

    public UploadFile getUploadFile() {
        return uploadFile;
    }

    public void setUploadFile(UploadFile uploadFile) {
        this.uploadFile = uploadFile;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
