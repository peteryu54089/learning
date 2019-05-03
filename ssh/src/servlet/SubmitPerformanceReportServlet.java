package servlet;

import dao.SubmitPerformanceDao;
import dao.impl.SubmitPerformanceDaoImpl;
import model.Account;
import model.Authority;
import model.PerformanceRecordSubmitRecord;
import model.role.Student;
import util.MenuBarUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/SubmitPerformanceReport")
@MultipartConfig
public class SubmitPerformanceReportServlet extends _BaseServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");

        if (account == null || !account.getAuthority().containsRole(
                Authority.RoleIndex.MANAGER, Authority.RoleIndex.WORK_TEAM, Authority.RoleIndex.STUDENT,
                Authority.RoleIndex.PERFORMANCE_SUBMITTER
        )) {
//        	response.sendRedirect("login");
            request.setAttribute("error", "您無權限使用此系統");
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            return;
        }

        SubmitPerformanceDao submitPerformanceDao = new SubmitPerformanceDaoImpl(account);
        SubmitPerformanceDao.Report report;

        try {
            Integer rgno = null;
            if (account.getAuthority().isStudent()) {
                rgno = account.<Student>getRoleDetail(Authority.RoleIndex.STUDENT).getRgno();
                report = submitPerformanceDao.getReportByYearAndSem(rgno);
            } else if (account.getAuthority().isTutor()) {
                try {
                    rgno = Integer.valueOf(request.getParameter("rgno"));
                } catch (NumberFormatException x) {
                    x.printStackTrace();
                    response.setStatus(400);
                    return;
                }

                report = submitPerformanceDao.getReportByYearAndSem(rgno);
            } else {
                try {
                    rgno = Integer.valueOf(request.getParameter("rgno"));
                    report = submitPerformanceDao.getReportByYearAndSem(rgno);
                } catch (NumberFormatException x) {
                    report = submitPerformanceDao.getReportByYearAndSem();
                }
            }

            request.setAttribute("submitReport", report);

            if (account.getAuthority().isStudent()) {
                request.getRequestDispatcher("/WEB-INF/jsp/stuSubmitPerformanceReport.jsp").forward(request, response);
            } else {
//                if (!account.getAuthority().isPerformanceSubmitter()) {
//                    request.setAttribute("hideBar", true);
//                }
                request.setAttribute("MenuBarUtil", new MenuBarUtil());
                request.getRequestDispatcher("/WEB-INF/jsp/submitPerformanceReport.jsp").forward(request, response);
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
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");

        if (account == null || !account.getAuthority().containsRole(
                Authority.RoleIndex.MANAGER, Authority.RoleIndex.WORK_TEAM, Authority.RoleIndex.STUDENT,
                Authority.RoleIndex.PERFORMANCE_SUBMITTER
        )) {
//        	response.sendRedirect("login");
            request.setAttribute("error", "您無權限使用此系統");
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            return;
        }

        SubmitPerformanceDao submitPerformanceDao = new SubmitPerformanceDaoImpl(account);

        Integer rgno = null;
        try {
            if (account.getAuthority().isStudent()) {
                rgno = account.<Student>getRoleDetail(Authority.RoleIndex.STUDENT).getRgno();
            }

            List<Object> list = submitPerformanceDao.getDetails(
                    Integer.parseInt(request.getParameter("year")),
                    rgno,
                    Integer.parseInt(request.getParameter("status")),
                    request.getParameter("date")
            );

            request.setAttribute("list", list);

            request.getRequestDispatcher("/WEB-INF/jsp/submitPerformanceReportDetails.jsp").forward(request, response);
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
    }
}
