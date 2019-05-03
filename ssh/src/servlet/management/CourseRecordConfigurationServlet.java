package servlet.management;

import dao.SystemConfigDao;
import dao.impl.SystemConfigDaoImpl;
import model.Account;
import model.SystemConfig;
import util.DateUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

@WebServlet("/courseRecordConfiguration")
@MultipartConfig
public class CourseRecordConfigurationServlet extends BaseManageServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        Account account;
        if ((account = checkPermission(request, response))==null)
            return;

        SystemConfigDao systemConfigDao = new SystemConfigDaoImpl(account);
        SystemConfig config = systemConfigDao.getSystemConfig();
        request.setAttribute("courseConfig", config.getCourseStudyRecord());
        request.setAttribute("DateUtils", new DateUtils());

        request.getRequestDispatcher("/WEB-INF/jsp/managerPage/courseRecordConfiguration.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        Account account;
        if ((account = checkPermission(request, response))==null)
            return;

        SystemConfigDaoImpl systemConfigDao = new SystemConfigDaoImpl(account);
        try {
            SystemConfig config = systemConfigDao.getSystemConfig();
            SystemConfig.CourseStudyRecord courseRecord = config.getCourseStudyRecord();

            Date studentStartDate = DateUtils.parseDateTimeInput(request.getParameter("studentStartDate"));
            Date studentEndDate = DateUtils.parseDateTimeInput(request.getParameter("studentEndDate"));

            Date teacherStartDate = DateUtils.parseDateTimeInput(request.getParameter("teacherStartDate"));
            Date teacherEndDate = DateUtils.parseDateTimeInput(request.getParameter("teacherEndDate"));

            int maxAmountPerRecord = Integer.parseInt(request.getParameter("maxAmountPerRecord"));
            int maxSubmitAmount = Integer.parseInt(request.getParameter("maxSubmitAmount"));

            int maxSize = Integer.parseInt(request.getParameter("maxSize"));
            int mailInterval = Integer.parseInt(request.getParameter("mailInterval"));

            int activeYear = Integer.parseInt(request.getParameter("activeYear"));
            int activeSem = Integer.parseInt(request.getParameter("activeSem"));

            String allowTypes = request.getParameter("allowTypes");

            courseRecord.setStudentStartDateTime(studentStartDate);
            courseRecord.setStudentEndDateTime(studentEndDate);

            courseRecord.setTeacherStartDateTime(teacherStartDate);
            courseRecord.setTeacherEndDateTime(teacherEndDate);

            courseRecord.setMaxAmountPerRecord(maxAmountPerRecord);
            courseRecord.setMaxSubmitAmount(maxSubmitAmount);
            courseRecord.setMaxSize(maxSize);
            courseRecord.setMailInterval(mailInterval);

            courseRecord.setActiveYear(activeYear);
            courseRecord.setActiveSem(activeSem);

            courseRecord.setAllowTypes(allowTypes);

            systemConfigDao.saveSystemConfig(config);
            response.setStatus(200);
        } catch (NullPointerException e) {
            e.printStackTrace();
            response.setStatus(500);
        } catch (ParseException | NumberFormatException e) {
            e.printStackTrace();
            response.setStatus(400);
        }
    }
}
