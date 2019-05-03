package servlet;

import dao.SystemConfigDao;
import dao.impl.SystemConfigDaoImpl;
import model.Account;
import model.SystemConfig;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/singlecontent")
public class SingleContentServlet extends _BaseServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");
        SystemConfigDao systemConfigDao = new SystemConfigDaoImpl(account);
        SystemConfig systemConfig = systemConfigDao.getSystemConfig();
        try {
            session.setAttribute("size", systemConfig.getOtherDocument().getMaxSize());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        request.getRequestDispatcher("/WEB-INF/jsp/singlecontent.jsp").forward(request, response);
    }
}
