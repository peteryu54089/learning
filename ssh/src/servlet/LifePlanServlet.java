package servlet;

import dao.SystemConfigDao;
import dao.impl.SystemConfigDaoImpl;
import model.Account;
import model.SystemConfig;
import model.role.Student;
import util.AuthCheck;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/lifeplan")
public class LifePlanServlet extends _BaseServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!AuthCheck.isAuth(request)) {
//        	response.sendRedirect("login");
        	request.setAttribute("error", "您無權限使用此系統");
        	request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            return;
        }
        try {
            HttpSession session = request.getSession();
            Student student = (Student) session.getAttribute("student");

        } catch (NullPointerException e) {
            e.printStackTrace();
            response.getWriter().write("發生異常錯誤，若有疑問請洽系統管理員");
            return;
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("發生異常錯誤，若有疑問請洽系統管理員");
            return;
        }
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");
        SystemConfigDao systemConfigDao = new SystemConfigDaoImpl(account);
        try {
            SystemConfig systemConfig = systemConfigDao.getSystemConfig();
            session.setAttribute("size", systemConfig.getLifePlan().getMaxSize() * 1000000);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        request.getRequestDispatcher("WEB-INF/jsp/lifeplan.jsp").forward(request, response);
    }
}
