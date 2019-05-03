package model;

import java.sql.Timestamp;

/**
 * Created by Ching Yun Yu on 2018/5/7.
 */
public class IndividualCounsel extends Counsel {
    private Integer rgno;

    public IndividualCounsel(int id, Timestamp startTime, Timestamp endTime, String title, String originalFilename, Integer fileId, String location, String description, String counselor, String submitter, Integer rgno, Timestamp createdAt) {
        super(id, startTime, endTime, title, originalFilename, fileId, location, description, counselor, submitter, createdAt);
        this.rgno = rgno;
    }

    public Integer getRgno() {
        return rgno;
    }

    public void setRgno(Integer rgno) {
        this.rgno = rgno;
    }
}
