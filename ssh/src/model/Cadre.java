package model;

import java.util.Date;

public class Cadre extends PerformanceBaseModel {
    private String unit;
    private String startTime;
    private String endTime;
    private String term;
    private String job;
    private String level;

    public Cadre(int rgno,
                 String content, String source,
                 String unit,
                 String startTime, String endTime,
                 String term,
                 String job, String level,
                 Integer documentFileId, Integer videoFileId, String externalLink
    ) {
        super(rgno, content, source, documentFileId, videoFileId, externalLink);
        this.unit = unit;
        this.startTime = startTime;
        this.endTime = endTime;
        this.term = term;
        this.job = job;
        this.level = level;
    }

    public Cadre(Integer id, int rgno,
                 String content, String source,
                 String unit,
                 String startTime, String endTime,
                 String term,
                 String job, String level,
                 Integer documentFileId, Integer videoFileId, String externalLink,
                 String submitter, String selectedYear, Integer check, String status,
                 Date createdAt
    ) {
        super(id, rgno,
                content, source,
                documentFileId, videoFileId, externalLink,
                submitter, selectedYear, check, status,
                createdAt);
        this.unit = unit;
        this.startTime = startTime;
        this.endTime = endTime;
        this.term = term;
        this.job = job;
        this.level = level;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String starttime) {
        this.startTime = starttime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endtime) {
        this.endTime = endtime;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getLevelC() {
        String[] StatusC = {"", "校級", "班級", "社團", "實習", "其他"};
        return StatusC[Integer.parseInt(level)];
    }

    @Override
    public boolean student_modifiable() {
        //TODO 檢查是否可修改
        if (!super.student_modifiable() || SourceType.CADRE_SUBMITTER.equals(getSource()))
            return false;
        else
            return true;
    }

    public boolean cadre_submitter_modifiable() {
        //TODO 檢查是否可修改
        if ("2".equals(getStatus()) || SourceType.STUDENT.equals(getSource()))
            return false;
        else {
//            if ("3".equals(getLevel())) {
//                return false;
//            }
            return true;
        }
    }
}
