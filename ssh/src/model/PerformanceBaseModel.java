package model;

import util.DateUtils;

import java.util.Date;

public abstract class PerformanceBaseModel implements PerformanceUnlockableModel {
    private String status;
    private Integer check;//0未勾選1勾選
    private Date created_at;
    protected Boolean isUnlockStatus = false;
    private String selectedYear;
    private String content;
    private String source;
    private String submitter;

    private Integer id;
    private Integer rgno;
    private Integer documentFileId;
    private Integer videoFileId;
    private String externalLink;
    private UploadFile documentFile;
    private UploadFile videoFile;
    private StuBasicInfo stuBasicInfo;

    protected PerformanceBaseModel(
            int rgno, String content,
            String source,
            Integer documentFileId, Integer videoFileId, String externalLink
    ) {
        this(null, rgno, content, source, documentFileId, videoFileId, externalLink, null, null, null, null, null);
    }

    protected PerformanceBaseModel(
            Integer id,
            int rgno, String content,
            String source,
            Integer documentFileId, Integer videoFileId, String externalLink,

            String submitter, String selectedYear, Integer check, String status,
            Date createdAt
    ) {
        this.id = id;
        this.source = source;
        this.rgno = rgno;
        this.content = content;
        this.documentFileId = documentFileId;
        this.videoFileId = videoFileId;
        this.externalLink = externalLink;
        this.submitter = submitter;
        this.selectedYear = selectedYear;
        this.check = check;
        this.status = status;
        this.created_at = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getRgno() {
        return this.rgno;
    }

    public void setRgno(int rgno) {
        this.rgno = rgno;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getCheck() {
        return check;
    }

    public void setCheck(Integer check) {
        this.check = check;
    }

    public String getSelectedYear() {
        return selectedYear;
    }

    public void setSelectedYear(String selectedYear) {
        this.selectedYear = selectedYear;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public String getCreatedDateString() {
        return DateUtils.formatDateTime(created_at);
    }

    public String getStatusC() {
        String[] StatusC = {"", "未提交", "已提交"};
        return StatusC[Integer.parseInt(status)];
    }

    public boolean student_modifiable() {
        if (status.equals("2"))
            return false;
        else
            return true;
    }

    public Boolean getIsUnlockStatus() {
        return "2".equals(status) && isUnlockStatus;
    }

    public void setUnlockStatus(Boolean unlockStatus) {
        isUnlockStatus = unlockStatus;
    }

    public Integer getDocumentFileId() {
        return this.documentFileId;
    }

    public void setDocumentFileId(Integer id) {
        this.documentFileId = id;
    }

    public Integer getVideoFileId() {
        return this.videoFileId;
    }

    public void setVideoFileId(Integer id) {
        this.videoFileId = id;
    }

    public String getExternalLink() {
        return this.externalLink;
    }

    public void setExternalLink(String link) {
        this.externalLink = link;
    }

    public UploadFile getDocumentFile() {
        return this.documentFile;
    }

    public void setDocumentFile(UploadFile documentFile) {
        this.documentFile = documentFile;
    }

    public UploadFile getVideoFile() {
        return this.videoFile;
    }

    public void setVideoFile(UploadFile videoFile) {
        this.videoFile = videoFile;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSubmitter() {
        return submitter;
    }

    public void setSubmitter(String submitter) {
        this.submitter = submitter;
    }

    public StuBasicInfo getStuBasicInfo() {
        return stuBasicInfo;
    }

    public void setStuBasicInfo(StuBasicInfo stuBasicInfo) {
        this.stuBasicInfo = stuBasicInfo;
    }

    public static class SourceType {
        public static final String STUDENT = "1";
        public static final String CADRE_SUBMITTER = "2";

        private SourceType() {

        }
    }

    public String getSourceC() {
        String[] SourceC = {"", "學生自建", "校方建立"};
        return SourceC[Integer.parseInt(source)];
    }

    public static class StuBasicInfo {
        public final String idNumber;
        public final String birth;

        public StuBasicInfo(String idNumber, String birth) {
            this.idNumber = idNumber;
            this.birth = birth;
        }
    }
}
