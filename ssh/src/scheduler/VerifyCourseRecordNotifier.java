package scheduler;

import dao.SystemConfigDao;
import dao.impl.SystemConfigDaoImpl;
import dbconn.DbConn;
import model.HasSchoolSchema;
import model.SystemConfig;
import org.apache.commons.io.IOUtils;
import resources.Resource;
import util.LogUtility;
import util.Mail;
import util.StringUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class VerifyCourseRecordNotifier {
    public static void main(String[] args) throws IOException {
        String schema = args[0];
        HasSchoolSchema hasSchoolSchema = () -> schema;
        SystemConfigDao configDao = new SystemConfigDaoImpl(hasSchoolSchema);
        SystemConfig config = configDao.getSystemConfig();
        Date teaStartTime = config.getCourseStudyRecord().getTeacherStartDateTime();
        Date teaEndTime = config.getCourseStudyRecord().getTeacherEndDateTime();

        Date now = new Date();
        if (now.before(teaStartTime) || now.after(teaEndTime)) {
            LogUtility.infoLog("Not in time");
            return;
        } else {
            Calendar cal = Calendar.getInstance();
            int day = cal.get(Calendar.DAY_OF_YEAR);
            int r;
            if ((r = (day % config.getCourseStudyRecord().getMailInterval())) != 0) {
                LogUtility.infoLog("Interval Remaining: " + r);
                return;
            }
        }

        String mailTemplate = IOUtils.toString(Resource.getFile("verifyCourseRecordNotification.html"), StandardCharsets.UTF_8);
        String rowTemplate = StringUtils.extractPair(
                mailTemplate,
                "<!-- tr template start -->",
                "<!-- tr template end -->"
        );
        mailTemplate = mailTemplate.replace(rowTemplate, "");
        List<Mail> mails = new ArrayList<>();

        try (Connection con = DbConn.getConnection(hasSchoolSchema)) {
            String sql = "SELECT * " +
                    "FROM COURSE_UNVERIFIED_LIST " +
                    "WHERE RECORD_YEAR = ? AND RECORD_SEM = ? " +
                    "ORDER BY STAFF_CODE ASC ";
            Map<String, List<Info>> listMap = new HashMap<>();
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                int i = 0;
                ps.setInt(++i, config.getCourseStudyRecord().getActiveYear());
                ps.setInt(++i, config.getCourseStudyRecord().getActiveSem());


                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Info info = new Info(
                                rs.getInt("SBJ_YEAR"),
                                rs.getString("SBJ_SEM"),
                                rs.getString("CURR_CNAME"),
                                rs.getInt("COURSE_NUM"),
                                rs.getString("STAFF_CODE"),
                                rs.getString("NAME"),
                                rs.getString("EMAIL"),
                                rs.getInt("CNT")
                        );
                        List<Info> list = listMap.computeIfAbsent(info.staffCode, (x) -> new ArrayList<>());
                        list.add(info);
                    }
                }

                for (Map.Entry<String, List<Info>> e : listMap.entrySet()) {
                    List<Info> infos = e.getValue();
                    String name = infos.get(0).staffName;
                    String email = infos.get(0).email;
                    StringBuilder sb = new StringBuilder();
                    Mail mail = new Mail();
                    mail
                            .setSubject("學生歷程系統 - 課程審核通知")
                            .setTo(email);

                    for (Info info : infos) {
                        Map<String, String> mapping = new HashMap<>();
                        mapping.put("year", String.valueOf(info.year));
                        mapping.put("sem", info.sem);
                        mapping.put("course_num", String.valueOf(info.courseNo));
                        mapping.put("course_name", info.courseName);
                        mapping.put("cnt", String.valueOf(info.cnt));
                        sb.append(StringUtils.replaceTemplate(rowTemplate, new HashMap<>(mapping)));
                    }

                    String mailCont = mailTemplate;
                    mailCont = mailCont.replace("<!-- tr template start -->", sb.toString());
                    Map<String, String> mailContMap = new HashMap<>();
                    mailContMap.put("name", name);
                    mailCont = StringUtils.replaceTemplate(mailCont, mailContMap);

                    mail.setContent(mailCont);
                    mails.add(mail);
                }
            }

            for (Mail mail : mails) {
                mail.send();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class Info {
        public final Integer year;
        public final String sem;
        public final String courseName;
        public final Integer courseNo;
        public final String staffCode;
        public final String staffName;
        public final String email;
        public final Integer cnt;


        private Info(Integer year, String sem, String courseName, Integer courseNo, String staffCode, String staffName, String email, Integer cnt) {
            this.year = year;
            this.sem = sem;
            this.courseName = courseName;
            this.courseNo = courseNo;
            this.staffCode = staffCode;
            this.staffName = staffName;
            this.email = email;
            this.cnt = cnt;
        }
    }
}
