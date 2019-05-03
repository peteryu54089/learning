package model;

import com.sun.istack.internal.Nullable;

import java.sql.Timestamp;
import java.util.Date;

public class UploadFile {
    private Integer id;
    private Integer rgno;
    private String staffCode;
    private String filePath;
    private String fileName;
    private java.sql.Timestamp createdAt;
    private int termYear;
    private int termSem;

    private String _link;

    public UploadFile(Integer id, Integer rgno, String staffCode, String filePath, String fileName, Timestamp createdAt, int termYear, int termSem) {
        this.id = id;
        this.rgno = rgno;
        this.staffCode = staffCode;
        this.filePath = filePath;
        this.fileName = fileName;
        this.createdAt = createdAt;
        this.termYear = termYear;
        this.termSem = termSem;
    }


    public UploadFile(Integer rgno, String filePath, String fileName, int termYear, int termSem) {
        this.termYear = termYear;
        this.termSem = termSem;
        this.id = null;
        this.rgno = rgno;
        this.staffCode = null;
        this.filePath = filePath;
        this.fileName = fileName;
    }

    public UploadFile(String staffCode, String filePath, String fileName, int termYear, int termSem) {
        this.termYear = termYear;
        this.termSem = termSem;
        this.id = null;
        this.rgno = null;
        this.staffCode = staffCode;
        this.filePath = filePath;
        this.fileName = fileName;
    }

    @Nullable
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    @Nullable
    public Integer getRgno() {
        return rgno;
    }

    public void setRgno(Integer rgno) {
        this.rgno = rgno;
    }

    @Nullable
    public String getStaffCode() {
        return staffCode;
    }

    public void setStaffCode(String staffCode) {
        this.staffCode = staffCode;
    }


    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }


    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }


    public java.sql.Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = new Timestamp(createdAt.getTime());
    }

    public int getTermYear() {
        return termYear;
    }

    public void setTermYear(int termYear) {
        this.termYear = termYear;
    }

    public int getTermSem() {
        return termSem;
    }

    public void setTermSem(int termSem) {
        this.termSem = termSem;
    }

    public String get_link() {
        return _link;
    }

    public void set_link(String _link) {
        this._link = _link;
    }
}
