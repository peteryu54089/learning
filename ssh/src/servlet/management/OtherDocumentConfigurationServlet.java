package servlet.management;

import dao.impl.SystemConfigDaoImpl;
import model.Account;
import model.SystemConfig;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;

@WebServlet("/otherDocumentConfiguration")
@MultipartConfig
public class OtherDocumentConfigurationServlet extends BaseManageServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        Account account;
        if ((account = checkPermission(request, response)) == null)
            return;

        SystemConfigDaoImpl systemConfigDao = new SystemConfigDaoImpl(account);

        SystemConfig config = systemConfigDao.getSystemConfig();

        request.setAttribute("otherDocumentConfig", config.getOtherDocument());
        request.setAttribute("autoBioConfig", config.getAutoBio());
        request.setAttribute("studyPlanConfig", config.getStudyPlan());

        request.getRequestDispatcher("/WEB-INF/jsp/managerPage/otherDocumentConfiguration.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        Account account;
        if ((account = checkPermission(request, response)) == null)
            return;

        SystemConfigDaoImpl systemConfigDao = new SystemConfigDaoImpl(account);

        try {
            SystemConfig config = systemConfigDao.getSystemConfig();
            SystemConfig.OtherDocument otherDocument = config.getOtherDocument();
            SystemConfig.AutoBio autoBio = config.getAutoBio();
            SystemConfig.StudyPlan studyPlan = config.getStudyPlan();

            int otherMaxAmount = Integer.parseInt(request.getParameter("otherMaxAmount"));
            int otherMaxSize = Integer.parseInt(request.getParameter("otherMaxSize"));
            String otherAllowTypes = request.getParameter("otherAllowTypes");

            int autoBioMaxSize = Integer.parseInt(request.getParameter("autoBioMaxSize"));
            String autoBioAllowTypes = request.getParameter("autoBioAllowTypes");

            int studyPlanMaxSize = Integer.parseInt(request.getParameter("studyPlanMaxSize"));
            String studyPlanAllowTypes = request.getParameter("studyPlanAllowTypes");

            otherDocument.setMaxAmount(otherMaxAmount);
            otherDocument.setMaxSize(otherMaxSize);
            otherDocument.setAllowTypes(otherAllowTypes);

            autoBio.setMaxSize(autoBioMaxSize);
            autoBio.setAllowTypes(autoBioAllowTypes);

            studyPlan.setMaxSize(studyPlanMaxSize);
            studyPlan.setAllowTypes(studyPlanAllowTypes);

            systemConfigDao.saveSystemConfig(config);
            response.setStatus(200);
        } catch (NullPointerException e) {
            e.printStackTrace();
            response.setStatus(500);
        } catch (NumberFormatException | ParseException e) {
            e.printStackTrace();
            response.setStatus(400);
        }
    }
}
