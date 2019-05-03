package servlet;

import dao.CourseRecordDao;
import dao.SelectCourseDao;
import dao.StudentDao;
import dao.impl.CourseRecordDaoImpl;
import dao.impl.SelectCourseDaoImpl;
import dao.impl.StudentDaoImpl;
import model.Account;
import model.Course;
import model.CourseRecord;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/teacherViewCourseRecord")
public class TeacherViewCourseRecordServlet extends _BaseServlet {


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");
        SelectCourseDao selectCourseDao = new SelectCourseDaoImpl(account);
        CourseRecordDao courseRecordDao = new CourseRecordDaoImpl(account);
        StudentDao studentDao = new StudentDaoImpl(account);
        Integer page = 1;
        if (request.getParameter("page") != null) {
            page = Integer.parseInt(request.getParameter("page"));
        }
        String stu_name = request.getParameter("stuname");
        String status = request.getParameter("status");
        Integer pageAmount = 1;
        String courseno = request.getParameter("courseno");
        Course course = null;
        List<CourseRecord> courseRecordList = null;
        try {
            course = selectCourseDao.getSelectCourseByCourseNo(courseno);
            courseRecordList = courseRecordDao.getCourseRecord(null, null, null, courseno, null, status, stu_name, true, null, page);
            pageAmount = courseRecordDao.getPageNumber(null, null, null, courseno, null, status, stu_name, true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            request.setAttribute("pageAmount", pageAmount);
            request.setAttribute("page", page);
            request.setAttribute("courseRecordList", courseRecordList);
            request.setAttribute("course", course);
            request.setAttribute("courseno", courseno);
            request.setAttribute("stu_name", stu_name);
            request.setAttribute("status", status);
            request.getRequestDispatcher("/WEB-INF/jsp/teacherViewCourseRecord.jsp").forward(request, response);
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

