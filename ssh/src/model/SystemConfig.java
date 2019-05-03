package model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SystemConfig {
    private static final String ALLOW_EXT_DELIMITER = ", ";
    private static final String DEFAULT_UPLOAD_TYPE = String.join(ALLOW_EXT_DELIMITER,
            Arrays.asList(".pdf", ".doc", ".docx", ".odf", ".png", ".jpg"));
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final SchoolInfo schoolInfo;
    private final AutoBio autoBio;
    private final StudyPlan studyPlan;
    private final LifePlan lifePlan;
    private final CounselingRecord counselingRecord;
    private final CourseStudyRecord courseStudyRecord;
    private final OtherDocument otherDocument;
    private final Performance performance;
    private final AbsenceUpdateInfo absenceUpdateInfo;
    private final SyncStatus syncStatus;

    public SystemConfig(Map<String, KeyPair> dataSet) throws ParseException {
        autoBio = new AutoBio(
                dataSet.get("AUTO_BIO_MAX_SIZE").getValue(2 * 1024 * 1024),
                dataSet.get("AUTO_BIO_TYPES").getValue(DEFAULT_UPLOAD_TYPE)
        );

        studyPlan = new StudyPlan(
                dataSet.get("STUDY_PLAN_MAX_SIZE").getValue(2 * 1024 * 1024),
                dataSet.get("STUDY_PLAN_TYPES").getValue(DEFAULT_UPLOAD_TYPE)
        );

        lifePlan = new LifePlan(
                dataSet.get("LIFE_PLAN_MAX_SIZE").getValue(2 * 1024 * 1024),
                dataSet.get("LIFE_PLAN_TYPES").getValue(DEFAULT_UPLOAD_TYPE)
        );

        counselingRecord = new CounselingRecord(
                dataSet.get("COUNSELING_RECORD_MAX_SIZE").getValue(2 * 1024 * 1024),
                dataSet.get("COUNSELING_RECORD_TYPES").getValue(DEFAULT_UPLOAD_TYPE)
        );

        schoolInfo = new SchoolInfo(
                dataSet.get("SCHOOL_ID").getValue(""),
                dataSet.get("SCHOOL_BANNER_PATH").getValue(""),
                dataSet.get("SCHOOL_LOGO_PATH").getValue(""),
                dataSet.get("SCHOOL_NAME").getValue(""),
                dataSet.get("SCHOOL_PHONE").getValue(""),
                dataSet.get("SCHOOL_ADDRESS").getValue(""),
                dataSet.get("SCHOOL_WEBSITE").getValue("")
        );

        courseStudyRecord = new CourseStudyRecord(
                dataSet.get("COURSE_STUDY_RECORD_MAX_AMOUNT_PER_RECORD").<Integer>getValue(1),
                dataSet.get("COURSE_STUDY_RECORD_MAX_SUBMIT_AMOUNT").<Integer>getValue(1),
                dataSet.get("COURSE_STUDY_RECORD_MAX_SIZE").<Integer>getValue(2 * 1024 * 1024),

                dataSet.get("COURSE_STUDY_RECORD_ACTIVE_TERM_YEAR").<Integer>getValue(107),
                dataSet.get("COURSE_STUDY_RECORD_ACTIVE_TERM_SEM").<Integer>getValue(1),

                dataSet.get("COURSE_STUDY_RECORD_STUDENT_START_DATETIME").getValue(new Date(100, 1, 1)),
                dataSet.get("COURSE_STUDY_RECORD_STUDENT_END_DATETIME").getValue(new Date(200, 1, 1)),

                dataSet.get("COURSE_STUDY_RECORD_TEACHER_START_DATETIME").getValue(new Date(100, 1, 1)),
                dataSet.get("COURSE_STUDY_RECORD_TEACHER_END_DATETIME").getValue(new Date(200, 1, 1)),
                dataSet.get("COURSE_STUDY_RECORD_TYPE").getValue(DEFAULT_UPLOAD_TYPE),

                dataSet.get("COURSE_STUDY_RECORD_MAIL_INTERVAL").<Integer>getValue(1)
        );

        otherDocument = new OtherDocument(
                dataSet.get("OTHER_DOCUMENT_MAX_AMOUNT").getValue(1),
                dataSet.get("OTHER_DOCUMENT_MAX_SIZE").getValue(2 * 1024 * 1024),
                dataSet.get("OTHER_DOCUMENT_TYPE").getValue(DEFAULT_UPLOAD_TYPE)
        );

        performance = new Performance(
                dataSet.get("PERFORMANCE_SUBMIT_YEAR").getValue(107),

                dataSet.get("PERFORMANCE_STUDENT_START_DATETIME").getValue(new Date(100, 1, 1)),
                dataSet.get("PERFORMANCE_STUDENT_END_DATETIME").getValue(new Date(200, 1, 1)),

                dataSet.get("PERFORMANCE_MAX_SUBMIT_AMOUNT").getValue(10),

                dataSet.get("PERFORMANCE_DOCUMENT_MAX_SIZE").getValue(2 * 1024 * 1024),
                dataSet.get("PERFORMANCE_DOCUMENT_TYPE").getValue(DEFAULT_UPLOAD_TYPE),

                dataSet.get("PERFORMANCE_VIDEO_MAX_SIZE").getValue(2 * 1024 * 1024),
                dataSet.get("PERFORMANCE_VIDEO_TYPE").getValue(DEFAULT_UPLOAD_TYPE)
        );

        absenceUpdateInfo = new AbsenceUpdateInfo(
                dataSet.get("ABSENCE_UPDATE_INTERVAL").getValue(1),
                dataSet.get("ABSENCE_UPDATE_DATETIME").getValue(new Date(1, 1, 1))
        );

        syncStatus = new SyncStatus(
                dataSet.get("LAST_SYNCED_STU_CLUB_ID").getValue(0),
                dataSet.get("LAST_SYNCED_STU_CLUB_TIME").getValue(new Date(1, 1, 1))
        );
    }

    public SchoolInfo getSchoolInfo() {
        return schoolInfo;
    }

    public CourseStudyRecord getCourseStudyRecord() {
        return courseStudyRecord;
    }

    public OtherDocument getOtherDocument() {
        return otherDocument;
    }

    public Performance getPerformance() {
        return performance;
    }

    public AutoBio getAutoBio() {
        return autoBio;
    }

    public StudyPlan getStudyPlan() {
        return studyPlan;
    }

    public LifePlan getLifePlan() {
        return lifePlan;
    }

    public CounselingRecord getCounselingRecord() {
        return counselingRecord;
    }

    public AbsenceUpdateInfo getAbsenceUpdateInfo() {
        return absenceUpdateInfo;
    }

    public SyncStatus getSyncStatus() {
        return syncStatus;
    }

    public static class KeyPair {
        private String key;
        private String dataType;
        private String value;

        public KeyPair(String key, String value) {
            this(key, "STRING", value);
        }

        public KeyPair(String key, Boolean value) {
            this(key, "BOOLEAN", Boolean.toString(value));
        }

        public KeyPair(String key, Date value) {
            this(key, "DATETIME", SIMPLE_DATE_FORMAT.format(value));
        }

        public KeyPair(String key, Integer value) {
            this(key, "INTEGER", Integer.toString(value));
        }

        public KeyPair(String key, String dataType, String value) {
            this.key = key;
            this.dataType = dataType;
            this.value = value;
        }

        public <T> T getValue(T defaultValue) {
            return convertDataType(value, dataType, defaultValue);
        }

        public String getValue() {
            return this.value;
        }

        public <T> void setValue(T value) {
            if (value instanceof Boolean) {
                this.dataType = "BOOLEAN";
                this.value = Boolean.toString((Boolean) value);
            } else if (value instanceof Date) {
                this.dataType = "DATETIME";
                this.value = SIMPLE_DATE_FORMAT.format((Date) value);
            } else {
                this.dataType = "STRING";
                this.value = value.toString();
            }
        }

        public String getKey() {
            return key;
        }

        public String getDataType() {
            return dataType;
        }
    }

    public static class SchoolInfo {
        private String id;
        private String bannerPath;
        private String logoPath;
        private String name;
        private String phone;
        private String address;
        private String website;

        public SchoolInfo(String id, String bannerPath, String logoPath, String name, String phone, String address, String website) {
            this.id = id;
            this.bannerPath = bannerPath;
            this.logoPath = logoPath;
            this.name = name;
            this.phone = phone;
            this.address = address;
            this.website = website;
        }

        public String getBannerPath() {
            return bannerPath;
        }

        public void setBannerPath(String bannerPath) {
            this.bannerPath = bannerPath;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getWebsite() {
            return website;
        }

        public void setWebsite(String website) {
            this.website = website;
        }

        public String getLogoPath() {
            return logoPath;
        }

        public void setLogoPath(String logoPath) {
            this.logoPath = logoPath;
        }

        public String getId() {
            return id;
        }
        
        public void setId(String id) {
        	this.id = id;
        }
    }

    public class CourseStudyRecord extends CommonDocumentType {
        private Integer mailInterval;
        private Integer maxAmountPerRecord;
        private Integer maxSubmitAmount;

        private Integer activeYear;
        private Integer activeSem;

        private Date studentStartDateTime;
        private Date studentEndDateTime;
        private Date teacherStartDateTime;
        private Date teacherEndDateTime;

        public CourseStudyRecord(
                Integer maxAmountPerRecord,
                Integer maxSubmitAmount,
                Integer maxSize,
                Integer activeYear, Integer activeSem,
                Date studentStartDateTime, Date studentEndDateTime,
                Date teacherStartDateTime, Date teacherEndDateTime,
                String allowTypes,
                Integer mailInterval
        ) throws ParseException {
            super(maxSize, allowTypes);
            if (mailInterval < 1) mailInterval = 1;

            this.mailInterval = mailInterval;
            this.maxAmountPerRecord = maxAmountPerRecord;
            this.maxSubmitAmount = maxSubmitAmount;

            this.activeYear = activeYear;
            this.activeSem = activeSem;

            this.studentStartDateTime = studentStartDateTime;
            this.studentEndDateTime = studentEndDateTime;

            this.teacherStartDateTime = teacherStartDateTime;
            this.teacherEndDateTime = teacherEndDateTime;
        }

        public Integer getMailInterval() {
            return mailInterval;
        }

        public void setMailInterval(Integer mailInterval) {
            this.mailInterval = mailInterval;
        }

        public Date getStudentStartDateTime() {
            return studentStartDateTime;
        }

        public void setStudentStartDateTime(Date studentStartDateTime) {
            this.studentStartDateTime = studentStartDateTime;
        }

        public Date getStudentEndDateTime() {
            return studentEndDateTime;
        }

        public void setStudentEndDateTime(Date studentEndDateTime) {
            this.studentEndDateTime = studentEndDateTime;
        }

        public Date getTeacherStartDateTime() {
            return teacherStartDateTime;
        }

        public void setTeacherStartDateTime(Date teacherStartDateTime) {
            this.teacherStartDateTime = teacherStartDateTime;
        }

        public Date getTeacherEndDateTime() {
            return teacherEndDateTime;
        }

        public void setTeacherEndDateTime(Date teacherEndDateTime) {
            this.teacherEndDateTime = teacherEndDateTime;
        }

        public Integer getMaxSubmitAmount() {
            return maxSubmitAmount;
        }

        public void setMaxSubmitAmount(Integer maxSubmitAmount) {
            this.maxSubmitAmount = maxSubmitAmount;
        }

        public Integer getMaxAmountPerRecord() {
            return maxAmountPerRecord;
        }

        public void setMaxAmountPerRecord(Integer maxAmountPerRecord) {
            this.maxAmountPerRecord = maxAmountPerRecord;
        }

        public Integer getActiveYear() {
            return activeYear;
        }

        public void setActiveYear(Integer activeYear) {
            this.activeYear = activeYear;
        }

        public Integer getActiveSem() {
            return activeSem;
        }

        public void setActiveSem(Integer activeSem) {
            this.activeSem = activeSem;
        }
    }

    public class OtherDocument extends CommonDocumentType {
        private Integer maxAmount;

        public OtherDocument(Integer maxAmount, Integer maxSize, String allowTypes) throws ParseException {
            super(maxSize, allowTypes);
            this.maxAmount = maxAmount;
        }

        public Integer getMaxAmount() {
            return maxAmount;
        }

        public void setMaxAmount(Integer maxAmount) {
            this.maxAmount = maxAmount;
        }
    }

    public class Performance {
        private Integer submitYear;
        private Date studentStartDateTime;
        private Date studentEndDateTime;

        private Integer maxSubmitAmount;

        private Integer maxDocSize;
        private Set<String> allowDocTypes;

        private Integer maxVideoSize;
        private Set<String> allowVideoTypes;

        public Performance(
                Integer submitYear,
                Date studentStartDateTime, Date studentEndDateTime,
                Integer maxSubmitAmount,
                Integer maxDocSize, String allowDocTypes,
                Integer maxVideoSize, String allowVideoTypes
        ) throws ParseException {
            this.submitYear = submitYear;
            this.studentStartDateTime = studentStartDateTime;
            this.studentEndDateTime = studentEndDateTime;
            this.maxSubmitAmount = maxSubmitAmount;
            this.maxDocSize = maxDocSize;
            this.allowDocTypes = parseExtTypes(allowDocTypes);
            this.maxVideoSize = maxVideoSize;
            this.allowVideoTypes = parseExtTypes(allowVideoTypes);
        }

        public Date getStudentStartDateTime() {
            return studentStartDateTime;
        }

        public void setStudentStartDateTime(Date studentStartDateTime) {
            this.studentStartDateTime = studentStartDateTime;
        }

        public Date getStudentEndDateTime() {
            return studentEndDateTime;
        }

        public void setStudentEndDateTime(Date studentEndDateTime) {
            this.studentEndDateTime = studentEndDateTime;
        }

        public Integer getMaxSubmitAmount() {
            return maxSubmitAmount;
        }

        public void setMaxSubmitAmount(Integer maxSubmitAmount) {
            this.maxSubmitAmount = maxSubmitAmount;
        }

        public Integer getMaxDocSize() {
            return maxDocSize;
        }

        public void setMaxDocSize(Integer maxDocSize) {
            this.maxDocSize = maxDocSize;
        }

        public String getAllowDocTypesString() {
            return String.join(ALLOW_EXT_DELIMITER, allowDocTypes);
        }

        public Set<String> getAllowDocTypes() {
            return allowDocTypes;
        }

        public void setAllowDocTypes(String allowDocTypes) throws ParseException {
            this.allowDocTypes = parseExtTypes(allowDocTypes);
        }

        public Integer getMaxVideoSize() {
            return maxVideoSize;
        }

        public void setMaxVideoSize(Integer maxVideoSize) {
            this.maxVideoSize = maxVideoSize;
        }

        public Set<String> getAllowVideoTypes() {
            return allowVideoTypes;
        }

        public String getAllowVideoTypesString() {
            return String.join(ALLOW_EXT_DELIMITER, allowVideoTypes);
        }

        public void setAllowVideoTypes(String allowVideoTypes) throws ParseException {
            this.allowVideoTypes = parseExtTypes(allowVideoTypes);
        }

        public Integer getSubmitYear() {
            return submitYear;
        }

        public void setSubmitYear(Integer submitYear) {
            this.submitYear = submitYear;
        }
    }

    public class AutoBio extends CommonDocumentType {
        protected AutoBio(Integer maxSize, String allowTypes) throws ParseException {
            super(maxSize, allowTypes);
        }
    }

    public class StudyPlan extends CommonDocumentType {
        protected StudyPlan(Integer maxSize, String allowTypes) throws ParseException {
            super(maxSize, allowTypes);
        }
    }

    public class LifePlan extends CommonDocumentType {
        protected LifePlan(Integer maxSize, String allowTypes) throws ParseException {
            super(maxSize, allowTypes);
        }
    }

    public class CounselingRecord extends CommonDocumentType {
        protected CounselingRecord(Integer maxSize, String allowTypes) throws ParseException {
            super(maxSize, allowTypes);
        }
    }

    public class AbsenceUpdateInfo {
        private Integer updateInterval;
        private Date lastUpdateDate;

        public AbsenceUpdateInfo(Integer updateInterval, Date lastUpdateDate) {
            this.updateInterval = updateInterval;
            this.lastUpdateDate = lastUpdateDate;
        }

        public Integer getUpdateInterval() {
            return updateInterval;
        }

        public void setUpdateInterval(Integer updateInterval) {
            this.updateInterval = updateInterval;
        }

        public Date getLastUpdateDate() {
            return lastUpdateDate;
        }

        public void setLastUpdateDate(Date lastUpdateDate) {
            this.lastUpdateDate = lastUpdateDate;
        }
    }

    public class SyncStatus {
        private Integer lastSyncedStuClubId;
        private Date lastSyncedStuClubTime;

        public SyncStatus(
                Integer lastSyncedStuClubId,
                Date lastSyncedStuClubTime

        ){
            this.lastSyncedStuClubId = lastSyncedStuClubId;
            this.lastSyncedStuClubTime = lastSyncedStuClubTime;
        }

        public Integer getLastSyncedStuClubId() {
            return lastSyncedStuClubId;
        }

        public void setLastSyncedStuClubId(Integer lastSyncedStuClubId) {
            this.lastSyncedStuClubId = lastSyncedStuClubId;
        }

        public Date getLastSyncedStuClubTime() {
            return lastSyncedStuClubTime;
        }

        public void setLastSyncedStuClubTime(Date lastSyncedStuClubTime) {
            this.lastSyncedStuClubTime = lastSyncedStuClubTime;
        }
    }

    private static class CommonDocumentType {
        private Integer maxSize;
        private Set<String> allowTypes;

        protected CommonDocumentType(Integer maxSize, String allowTypes) throws ParseException {
            this.maxSize = maxSize;
            this.allowTypes = parseExtTypes(allowTypes);
        }

        public Integer getMaxSize() {
            return maxSize;
        }

        public void setMaxSize(Integer maxSize) {
            this.maxSize = maxSize;
        }

        public Set<String> getAllowTypes() {
            return allowTypes;
        }

        public String getAllowTypesString() {
            return String.join(ALLOW_EXT_DELIMITER, allowTypes);
        }

        public void setAllowTypes(String allowTypes) throws ParseException {
            this.allowTypes = parseExtTypes(allowTypes);
        }
    }

    private static Set<String> parseExtTypes(String str) throws ParseException {
        if (!Pattern.compile("^((\\.[a-z0-9]+, )*(\\.[a-z0-9]+)*)$").matcher(str).matches()) {
            throw new ParseException("str", 0);
        }
        return Collections.unmodifiableSet(
                Arrays.stream(str.split(ALLOW_EXT_DELIMITER))
                        .map(x -> x.toLowerCase().trim()).collect(Collectors.toSet())
        );
    }

    private static <T> T convertDataType(String str, String type, T defaultValue) {
        if (str == null)
            return defaultValue;
        try {
            switch (type.toUpperCase()) {
                case "INTEGER":
                    return (T) ((Integer) Integer.parseInt(str));

                case "DATETIME":
                    try {
                        return (T) SIMPLE_DATE_FORMAT.parse(str);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        return defaultValue;
                    }

                case "BOOLEAN":
                    return (T) (Boolean) Boolean.parseBoolean(str);

                default:
                    return (T) str;
            }
        } catch (Exception e) {
            return defaultValue;
        }
    }
}
