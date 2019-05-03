package model;

public class LifePlan {
    private String reg_no;
    private String topic;
    private String description;
    private String file_path;

    public LifePlan(String reg_no, String topic, String description, String file_path) {
        this.reg_no = reg_no;
        this.topic = topic;
        this.description = description;
        this.file_path = file_path;
    }

    public String getReg_no() {
        return reg_no;
    }

    public void setReg_no(String reg_no) {
        this.reg_no = reg_no;
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

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }
}
