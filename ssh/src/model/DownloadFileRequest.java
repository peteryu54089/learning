package model;

import java.util.Date;
import java.util.UUID;

public class DownloadFileRequest {
    private String _uuid = UUID.randomUUID().toString();
    private Date createdAt;
    private String idNo;
    private String fsName;
    private String originalName;
    private Integer id;
    private String table;

    public DownloadFileRequest(String table, Integer id, String idNo, String fsName, String originalName) {
        this.table = table;
        this.id = id;
        this.idNo = idNo;
        this.fsName = fsName;
        this.originalName = originalName;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getFsName() {
        return fsName;
    }

    public void setFsName(String fsName) {
        this.fsName = fsName;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table.toUpperCase();
    }


    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o == this) return true;
        if (!(o instanceof DownloadFileRequest)) return false;

        DownloadFileRequest target = (DownloadFileRequest) o;
        return idNo.equals(target.idNo) &&
                fsName.equals(target.fsName) &&
                originalName.equals(target.originalName) &&
                id.equals(target.id) && table.equals(target.table)
                ;

    }
}
