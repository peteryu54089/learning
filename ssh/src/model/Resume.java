package model;

import util.DateUtils;

import java.sql.Timestamp;

public class Resume {
    private int id;
    private String reg_no;
    private String topic;
    private String description;
    private String file_path;
    private Timestamp created_at;
    private Integer term_year;
    private Integer term_semester;
    private String original_filename;

    public Resume(int id, String reg_no, String topic, String description, String file_path, Timestamp created_at, Integer year, Integer sem, String original_filename) {
        this.id = id;
        this.reg_no = reg_no;
        this.topic = topic;
        this.description = description;
        this.file_path = file_path;
        this.created_at = created_at;
        this.term_year = year;
        this.term_semester = sem;
        this.original_filename = original_filename;
    }

    public void setReg_no(String reg_no) {
        this.reg_no = reg_no;
    }

    public String getReg_no() {
        return reg_no;
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

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public String getFile_path() {
        return file_path;
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

    public String getOriginal_filename() {
        return original_filename;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
