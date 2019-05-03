package servlet;

import dao.*;
import dao.impl.*;
import model.Account;
import model.Course;
import model.CourseRecord;
import model.role.Teacher;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/teacherCourseRecord")
public class TeacherCourseRecordServlet extends _BaseServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");
        TeachingCourseDao teachingCourseDao = new TeachingCourseDaoImpl(account);
        CourseRecordDao courseRecordDao = new CourseRecordDaoImpl(account);
        TeacherDao teacherDao = new TeacherDaoImpl(account);
        Integer page = 1;
        Integer pageAmount = 1;
        String year = request.getParameter("year");
        String sem = request.getParameter("semester");
        String course_name = request.getParameter("courseName");
        String class_name = request.getParameter("className");
        if (!account.getAuthority().isTeacher()) {
//        	response.sendRedirect("login");
        	request.setAttribute("error", "您無權限使用此系統");
        	request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            return;
        }

        if (request.getParameter("page") != null) {
            page = Integer.parseInt(request.getParameter("page"));
        }
        try {
            Teacher teacher = teacherDao.getTeacherByRegNumber(account.getRegNumber());
            List<Course> courseList = teachingCourseDao.getTeachingCourse(teacher.getStaffCode(), year, sem, class_name, course_name, page);
            for (Course sc : courseList) {
                List<CourseRecord> courseRecordList = courseRecordDao.getCourseRecord(null, null, null, sc.getCourse_no(), null, "2", null, true, null, null);
                sc.setCourseRecordList(courseRecordList);
            }
            pageAmount = teachingCourseDao.getPageNumber(teacher.getStaffCode(), year, sem, class_name, course_name);
            request.setAttribute("courseList", courseList);
            request.setAttribute("pageAmount", pageAmount);
            request.setAttribute("page", page);
            request.setAttribute("year", year);
            request.setAttribute("semester", sem);
            request.setAttribute("className", class_name);
            request.setAttribute("courseName", course_name);
            request.getRequestDispatcher("/WEB-INF/jsp/teacherCourseRecord.jsp").forward(request, response);
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

