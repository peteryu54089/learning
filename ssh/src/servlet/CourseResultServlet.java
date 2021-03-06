package servlet;

import dao.CourseResultDao;
import dao.StudentDao;
import dao.impl.CourseResultDaoImpl;
import dao.impl.StudentDaoImpl;
import model.Account;
import model.CourseResult;
import model.role.Student;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/courseresult")
public class CourseResultServlet extends _BaseServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");
        CourseResultDao courseResultDao = new CourseResultDaoImpl(account);
        StudentDao studentDao = new StudentDaoImpl(account);
        Student student = null;
        try {
            student = studentDao.getStudentByRegNumber(account.getRegNumber());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (!account.getAuthority().isStudent()) {
//        	response.sendRedirect("login");
        	request.setAttribute("error", "您無權限使用此系統");
        	request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            return;
        }
        try {
            Integer page = 1;
            if (request.getParameter("page") != null) {
                page = Integer.parseInt(request.getParameter("page"));
            }
            String year = request.getParameter("year");
            String semester = request.getParameter("semester");
            String name = request.getParameter("courseName");
            List<CourseResult> courseResultList = courseResultDao.getCourseResult(student.getRgno(), year, semester, name, page);
            Integer pageAmount = courseResultDao.getPageNumber(student.getRgno(), year, semester, name);
            request.setAttribute("year", year);
            request.setAttribute("semester", semester);
            request.setAttribute("courseName", name);
            request.setAttribute("page", page);
            request.setAttribute("pageAmount", pageAmount);
            request.setAttribute("courseResultList", courseResultList);
            request.getRequestDispatcher("/WEB-INF/jsp/courseresult.jsp").forward(request, response);

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

