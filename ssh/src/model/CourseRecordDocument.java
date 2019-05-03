package model;

import java.sql.Timestamp;

public class CourseRecordDocument {
    private Integer crid;
    private Integer upload_file_id;
    private String document;
    private String original_filename;
    private Timestamp deleted_at;
    private UploadFile uploadFile;
    private String dlLink;

    public CourseRecordDocument(Integer crid,Integer upload_file_id, String document, String original_filename, Timestamp deleted_at) {
        this.crid = crid;
        this.upload_file_id = upload_file_id;
        this.document = document;
        this.original_filename = original_filename;
        this.deleted_at = deleted_at;
    }


    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getOriginal_filename() {
        return original_filename;
    }

    public void setOriginal_filename(String original_filename) {
        this.original_filename = original_filename;
    }

    public Timestamp getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(Timestamp deleted_at) {
        this.deleted_at = deleted_at;
    }

    public Integer getCrid() {
        return crid;
    }

    public void setCrid(Integer crid) {
        this.crid = crid;
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

    public String getDlLink() {
        return dlLink;
    }

    public void setDlLink(String dlLink) {
        this.dlLink = dlLink;
    }
}
