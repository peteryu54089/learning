package servlet.management;

import dao.PerformanceUnlockStatusDao;
import dao.impl.PerformanceUnlockStatusDaoImpl;
import dao.impl.SystemConfigDaoImpl;
import model.Account;
import model.Authority;
import util.DateUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/UnlockPerformance")
@MultipartConfig
public class UnlockPerformanceServlet extends BaseManageServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        if (checkPermission(request, response) == null)
            return;

        bindStuClsInfo(request, response);
        request.setAttribute("title", "解鎖多元表現");
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");
        if (account.getAuthority()
                .containsRole(
                        Authority.RoleIndex.MANAGER,
                        Authority.RoleIndex.WORK_TEAM
                )) {
            request.getRequestDispatcher("/WEB-INF/jsp/managerPage/unlockPerformance.jsp").forward(request, response);
        }
        else
        {
            request.getRequestDispatcher("/WEB-INF/jsp/performanceSubmitterUnlockPerformance.jsp").forward(request, response);
        }

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");
        PerformanceUnlockStatusDao performanceUnlockStatusDao = new PerformanceUnlockStatusDaoImpl(account);
        Integer rgno = Integer.valueOf(request.getParameter("rgno"));
        try {
            int year =new SystemConfigDaoImpl(account).getSystemConfig().getPerformance().getSubmitYear();
            if (performanceUnlockStatusDao.isInUnlockStatus(Integer.toString(year), rgno)) {
                performanceUnlockStatusDao.revokeUnlock(Integer.toString(year), rgno);
            } else {
                performanceUnlockStatusDao.grantUnlock(Integer.toString(year), rgno);
            }


        } catch (NullPointerException e) {
            e.printStackTrace();
            response.getWriter().write("發生異常錯誤，若有疑問請洽系統管理員");
            return;
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("發生異常錯誤，若有疑問請洽系統管理員");
            return;
        }
    }

    protected Account checkPermission(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");
        if (account == null || !account.getAuthority()
                .containsRole(
                        Authority.RoleIndex.MANAGER,
                        Authority.RoleIndex.WORK_TEAM,
                        Authority.RoleIndex.PERFORMANCE_SUBMITTER
                )
        ) {
            response.setStatus(401);
            return null;
        }

        return account;
    }
}
