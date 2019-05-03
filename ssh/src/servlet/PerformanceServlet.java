package servlet;

import dao.*;
import dao.impl.*;
import model.Account;
import model.SystemConfig;
import model.role.Student;
import util.AuthCheck;
import util.TimeCheck;
import util.Upload;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * Created by David on 2018/1/29.
 */

@WebServlet("/performance")
@MultipartConfig
public class PerformanceServlet extends _BaseServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if (!AuthCheck.isAuth(request)) {
//        	response.sendRedirect("login");
        	request.setAttribute("error", "您無權限使用此系統");
        	request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            return;
        }

        String type = request.getParameter("type");
        try {
            if (type != null) {
                int i = Integer.parseInt(type);
                request.setAttribute("type", type);
            } else {
                request.setAttribute("type", 1);
            }
            request.getRequestDispatcher("/WEB-INF/jsp/performance.jsp").forward(request, response);
            return;
        } catch (NumberFormatException e) {
            request.setAttribute("error", "參數設定錯誤，若有疑問請洽系統管理員。");
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            return;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        if (!AuthCheck.isAuth(request)) {
//        	response.sendRedirect("login");
        	request.setAttribute("error", "您無權限使用此系統");
        	request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            return;
        }

        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");
        CadreDao cadredao = new CadreDaoImpl(account);
        CompetitionDao competitiondao = new CompetitionDaoImpl(account);
        LicenseRecordDao licensedao = new LicenseRecordDaoImpl(account);
        VolunteerDao volunteerdao = new VolunteerDaoImpl(account);
        OtherDao otherdao = new OtherDaoImpl(account);
        Part file = request.getPart("document");

        try {
            SystemConfigDao systemConfigDao = new SystemConfigDaoImpl(account);
            SystemConfig systemConfig = systemConfigDao.getSystemConfig();

            if (!TimeCheck.isAllowUploadPerformance(systemConfig)) {
                request.setAttribute("error", "已超過上傳時間，無法上傳");
                request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
                return;
            }

            Student student = (Student) session.getAttribute("student");
            String idno = account.getIdNumber();
            String type = request.getParameter("type");

            //  ------------------------------------ start 上傳檔案 ------------------------------------
            Upload upload = new Upload(getServletContext());
            String fileName = upload.uploadFile(account, file, type);
            // ------------------------------------ end 上傳檔案 --------------------------------------

            if (type.equals("1")) {
                //已拿走
            } else if (type.equals("2")) {
                //已拿走
            } else if (type.equals("3")) {
                //已拿走
            } else if (type.equals("4")) {
                //已拿走
            } else if (type.equals("5")) {
                //已拿走
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            request.setAttribute("error", "參數設定錯誤，若有疑問請洽系統管理員。");
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            return;
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "系統發生錯誤，若有疑問請洽系統管理員。");
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            return;
        }
        response.sendRedirect("history");
    }
}
