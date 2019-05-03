package servlet;

import dao.AttendDao;
import dao.StudentDao;
import dao.SystemConfigDao;
import dao.impl.AttendDaoImpl;
import dao.impl.StudentDaoImpl;
import dao.impl.SystemConfigDaoImpl;
import model.*;
import model.role.Student;
import util.AuthCheck;
import util.DateUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/tutorViewAttend")
public class TutorViewAttendServlet extends _BaseServlet {


    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        if (!AuthCheck.isAuth(request)) {
//        	response.sendRedirect("login");
            request.setAttribute("error", "您無權限使用此系統");
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            return;
        }
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");
        SystemConfigDao systemConfigDao = new SystemConfigDaoImpl(account);
        SystemConfig systemConfig = systemConfigDao.getSystemConfig();
        AttendDao attendDao = new AttendDaoImpl(account);
        if (!account.getAuthority().containsRole(Authority.RoleIndex.TUTOR, Authority.RoleIndex.WORK_TEAM, Authority.RoleIndex.MANAGER)) {
//        	response.sendRedirect("login");
            request.setAttribute("error", "您無權限使用此系統");
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            return;
        }
        StudentDao studentDao = new StudentDaoImpl(account);
        Integer rgno = Integer.parseInt(request.getParameter("rgno"));
        Student student = studentDao.getStudentByRgno(rgno);
        try {
            Integer page = 1;
            if (request.getParameter("page") != null) {
                page = Integer.parseInt(request.getParameter("page"));
            }
            String year = request.getParameter("year");
            String semester = request.getParameter("semester");
            String aType = request.getParameter("aType");
            List<Attend> attendList = attendDao.getAttend(student.getRgno(), year, semester, aType, page);
            Integer pageAmount = attendDao.getPageNumber(student.getRgno(), year, semester, aType);

            List<ArCode> arCodeList = attendDao.getArCodeList();
            Integer count = attendDao.getCount(student.getRgno(), year, semester, aType);
            Integer UpdateInterval = systemConfig.getAbsenceUpdateInfo().getUpdateInterval();
            String lastUpdateDate = DateUtils.formatDateTime(systemConfig.getAbsenceUpdateInfo().getLastUpdateDate());
            request.setAttribute("rgno", rgno);
            request.setAttribute("page", page);
            request.setAttribute("year", year);
            request.setAttribute("semester", semester);
            request.setAttribute("student", student);
            request.setAttribute("aType", aType);
            request.setAttribute("count", count);
            request.setAttribute("lastUpdateDate", lastUpdateDate);
            request.setAttribute("UpdateInterval", UpdateInterval);
            request.setAttribute("pageAmount", pageAmount);
            request.setAttribute("attendList", attendList);
            request.setAttribute("arCodeList", arCodeList);
            request.getRequestDispatcher("/WEB-INF/jsp/tutorViewAttend.jsp").forward(request, response);

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
}
