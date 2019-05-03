package util;

import model.SystemConfig;

import java.util.Date;

/**
 * Created by David on 2017/10/20.
 */

public class TimeCheck {
    public static boolean isAllowUploadCourseStudyRecord(SystemConfig systemConfig) {
        if (systemConfig == null) return false;

        final Date currentTime = new Date();
        SystemConfig.CourseStudyRecord courseStudyRecord = systemConfig.getCourseStudyRecord();
        return isDateBetweenRange(
                currentTime,
                courseStudyRecord.getStudentStartDateTime(),
                courseStudyRecord.getStudentEndDateTime()
        );
    }

    public static boolean isAllowVerifyCourseStudyRecord(SystemConfig systemConfig) {
        if (systemConfig == null) return false;

        final Date currentTime = new Date();
        SystemConfig.CourseStudyRecord courseStudyRecord = systemConfig.getCourseStudyRecord();

        return isDateBetweenRange(
                currentTime,
                courseStudyRecord.getTeacherStartDateTime(),
                courseStudyRecord.getTeacherEndDateTime()
        );
    }

    public static boolean isAllowUploadPerformance(SystemConfig systemConfig) {
        if (systemConfig == null) return false;

        final Date currentTime = new Date();
        SystemConfig.Performance performance = systemConfig.getPerformance();

        return isDateBetweenRange(
                currentTime,
                performance.getStudentStartDateTime(),
                performance.getStudentEndDateTime()
        );
    }

    public static boolean isDateBetweenRange(
            final Date target,
            final Date start,
            final Date end
    ) {
        return (target.equals(start) || target.after(start)) && (target.equals(end) || target.before(end));
    }
}
