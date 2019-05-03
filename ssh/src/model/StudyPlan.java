package model;

import util.DateUtils;

import java.sql.Timestamp;

public class StudyPlan {

    private Integer id;
    private Integer rgno;
    private String topic;
    private String description;
    private Integer main_file_id;
    private Integer sub_file_id;
    private Timestamp created_at;
    private Integer term_year;
    private Integer term_semester;
    private UploadFile main_uploadFile;
    private UploadFile sub_uploadFile;
    private String main_link;
    private String sub_link;

    public StudyPlan(){
    }

    public StudyPlan(Integer id, Integer rgno, String topic, String description, Integer main_file_id, Integer sub_file_id, Timestamp created_at, Integer term_year, Integer term_semester) {
        this.id = id;
        this.rgno = rgno;
        this.topic = topic;
        this.description = description;
        this.main_file_id = main_file_id;
        this.sub_file_id = sub_file_id;
        this.created_at = created_at;
        this.term_year = term_year;
        this.term_semester = term_semester;

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public Integer getTerm_year() {
        return term_year;
    }

    public void setTerm_year(Integer term_year) {
        this.term_year = term_year;
    }

    public Integer getTerm_semester() {
        return term_semester;
    }

    public void setTerm_semester(Integer term_semester) {
        this.term_semester = term_semester;
    }

    public String getCreatedDateString() {
        return DateUtils.formatDateTime(created_at);
    }

    public Integer getRgno() {
        return rgno;
    }

    public void setRgno(Integer rgno) {
        this.rgno = rgno;
    }

    public Integer getMain_file_id() {
        return main_file_id;
    }

    public void setMain_file_id(Integer main_file_id) {
        this.main_file_id = main_file_id;
    }

    public Integer getSub_file_id() {
        return sub_file_id;
    }

    public void setSub_file_id(Integer sub_file_id) {
        this.sub_file_id = sub_file_id;
    }

    public UploadFile getMain_uploadFile() {
        return main_uploadFile;
    }

    public void setMain_uploadFile(UploadFile main_uploadFile) {
        this.main_uploadFile = main_uploadFile;
    }

    public UploadFile getSub_uploadFile() {
        return sub_uploadFile;
    }

    public void setSub_uploadFile(UploadFile sub_uploadFile) {
        this.sub_uploadFile = sub_uploadFile;
    }

    public String getMain_link() {
        return main_link;
    }

    public void setMain_link(String main_link) {
        this.main_link = main_link;
    }

    public String getSub_link() {
        return sub_link;
    }

    public void setSub_link(String sub_link) {
        this.sub_link = sub_link;
    }
}


