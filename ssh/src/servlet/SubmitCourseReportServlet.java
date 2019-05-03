package servlet;

import dao.SubmitCourseDao;
import dao.SubmitPerformanceDao;
import dao.impl.SubmitCourseDaoImpl;
import dao.impl.SubmitPerformanceDaoImpl;
import model.Account;
import model.Authority;
import model.PerformanceRecordSubmitRecord;
import model.SubmitCourse;
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

@WebServlet("/SubmitCourseReport")
@MultipartConfig
public class SubmitCourseReportServlet extends _BaseServlet {
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

        SubmitCourseDao submitCourseDao = new SubmitCourseDaoImpl(account);
        SubmitCourseDao.Report report;

        try {
            Integer rgno = null;
            if (account.getAuthority().isStudent()) {
                rgno = account.<Student>getRoleDetail(Authority.RoleIndex.STUDENT).getRgno();
                report = submitCourseDao.getReportByYearAndSem(rgno);
            } else if (account.getAuthority().isTutor()) {
                try {
                    rgno = Integer.valueOf(request.getParameter("rgno"));
                } catch (NumberFormatException x) {
                    x.printStackTrace();
                    response.setStatus(400);
                    return;
                }

                report = submitCourseDao.getReportByYearAndSem(rgno);
            } else {
                try {
                    rgno = Integer.valueOf(request.getParameter("rgno"));
                    report = submitCourseDao.getReportByYearAndSem(rgno);
                } catch (NumberFormatException x) {
                    report = submitCourseDao.getReportByYearAndSem();
                }
            }

            request.setAttribute("submitReport", report);

            if (account.getAuthority().isStudent()) {
                request.getRequestDispatcher("/WEB-INF/jsp/stuSubmitCourseReport.jsp").forward(request, response);
            } else {
//                if (!account.getAuthority().isPerformanceSubmitter()) {
//                    request.setAttribute("hideBar", true);
//                }
                request.setAttribute("MenuBarUtil", new MenuBarUtil());
                request.getRequestDispatcher("/WEB-INF/jsp/submitCourseReport.jsp").forward(request, response);
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

        SubmitCourseDao submitCourseDao = new SubmitCourseDaoImpl(account);

        Integer rgno = null;
        try {
            if (account.getAuthority().isStudent()) {
                rgno = account.<Student>getRoleDetail(Authority.RoleIndex.STUDENT).getRgno();
            }

            List<Object> list = submitCourseDao.getDetails(
                    Integer.parseInt(request.getParameter("year")),
                    Integer.parseInt(request.getParameter("sem")),
                    Integer.parseInt(request.getParameter("q")),
                    rgno
            );

            request.setAttribute("list", list);

            request.getRequestDispatcher("/WEB-INF/jsp/submitCourseReportDetails.jsp").forward(request, response);
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
