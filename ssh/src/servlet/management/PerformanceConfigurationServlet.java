package servlet.management;

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

@WebServlet("/performanceConfiguration")
@MultipartConfig
public class PerformanceConfigurationServlet extends BaseManageServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        Account account;
        if ((account = checkPermission(request, response))==null)
            return;

        SystemConfigDaoImpl systemConfigDao = new SystemConfigDaoImpl(account);

        SystemConfig config = systemConfigDao.getSystemConfig();
        request.setAttribute("performanceConfig", config.getPerformance());
        request.setAttribute("DateUtils", new DateUtils());

        request.getRequestDispatcher("/WEB-INF/jsp/managerPage/performanceConfiguration.jsp").forward(request, response);
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
            SystemConfig.Performance performance = config.getPerformance();

            Date studentStartDate = DateUtils.parseDateTimeInput(request.getParameter("studentStartDate"));
            Date studentEndDate = DateUtils.parseDateTimeInput(request.getParameter("studentEndDate"));

            int submitYear = Integer.parseInt(request.getParameter("submitYear"));
            int maxSubmitAmount = Integer.parseInt(request.getParameter("maxSubmitAmount"));
            int maxDocSize = Integer.parseInt(request.getParameter("maxDocSize"));
            //int maxVideoSize = Integer.parseInt(request.getParameter("maxVideoSize"));

            String allowDocTypes = request.getParameter("allowDocTypes");
            String allowVideoTypes = request.getParameter("allowVideoTypes");

            performance.setSubmitYear(submitYear);

            performance.setStudentStartDateTime(studentStartDate);
            performance.setStudentEndDateTime(studentEndDate);

            performance.setMaxSubmitAmount(maxSubmitAmount);
            performance.setMaxDocSize(maxDocSize);
            // performance.setMaxVideoSize(maxVideoSize);

            performance.setAllowDocTypes(allowDocTypes);
            performance.setAllowVideoTypes(allowVideoTypes);

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
