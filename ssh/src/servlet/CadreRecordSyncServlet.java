package servlet;

import dao.StuClubRecordDao;
import dao.SystemConfigDao;
import dao.impl.StuClubRecordDaoImpl;
import dao.impl.SystemConfigDaoImpl;
import model.Account;
import model.Authority;
import model.SystemConfig;
import util.AuthCheck;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/CadreRecordSync")
public class CadreRecordSyncServlet extends _BaseServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        if (!AuthCheck.isAuth(request)) {
            request.setAttribute("error", "您無權限使用此系統");
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            return;
        }
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");

        if (!account.getAuthority().containsRole(Authority.RoleIndex.MANAGER, Authority.RoleIndex.WORK_TEAM, Authority.RoleIndex.CADRE_SUBMITTER)) {
            response.setStatus(401);
            return;
        }
        StuClubRecordDao stuClubRecordDao = new StuClubRecordDaoImpl(account);
        Boolean syncSuccess = stuClubRecordDao.syncClubRecordToCadre();
        session.setAttribute("clubSyncSuccess", syncSuccess);
        response.sendRedirect("./CadreRecordSync");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        if (!AuthCheck.isAuth(request)) {
            request.setAttribute("error", "您無權限使用此系統");
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            return;
        }
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");

        if (!account.getAuthority().containsRole(Authority.RoleIndex.MANAGER, Authority.RoleIndex.WORK_TEAM, Authority.RoleIndex.CADRE_SUBMITTER)) {
            response.setStatus(401);
            return;
        }

        SystemConfigDao systemConfigDao = new SystemConfigDaoImpl(account);
        SystemConfig systemConfig = systemConfigDao.getSystemConfig();
        request.setAttribute("config", systemConfig);
        Boolean clubSyncSuccess = (Boolean) session.getAttribute("clubSyncSuccess");
        request.setAttribute("clubSyncSuccess", clubSyncSuccess);
        session.removeAttribute("clubSyncSuccess");

        request.getRequestDispatcher("/WEB-INF/jsp/cadreRecordSync.jsp").forward(request, response);
    }
}
