package servlet;

import dao.CourseRecordDao;
import dao.TeacherDao;
import dao.impl.CourseRecordDaoImpl;
import dao.impl.TeacherDaoImpl;
import model.Account;
import model.Authority.RoleIndex;
import model.CourseRecord;
import model.role.Teacher;
import util.AuthCheck;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.Timestamp;

@WebServlet("/courseRecordVerified")
@MultipartConfig
public class CourseRecordVerifiedServlet extends _BaseServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        if (!AuthCheck.isAuth(request, RoleIndex.TEACHER)) {
//        	response.sendRedirect("login");
        	request.setAttribute("error", "您無權限使用此系統");
        	request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            return;
        }
        try {
            HttpSession session = request.getSession();
            Account account = (Account) session.getAttribute("account");
            CourseRecordDao courseRecordDao = new CourseRecordDaoImpl(account);
            TeacherDao teacherDao = new TeacherDaoImpl(account);

            String verifyid = request.getParameter("verifyid");
            String courseno = request.getParameter("courseno");
            String page = request.getParameter("page");
            String status = request.getParameter("status");
            String content = request.getParameter("content");
            CourseRecord courseRecord = courseRecordDao.getCourseRecordByID(null, verifyid);
            Teacher teacher = teacherDao.getTeacherByRegNumber(account.getRegNumber());
            PrintWriter out = response.getWriter();
            //TODO　可驗證
            if (courseRecord.isVerifyable()==false) {
                out.println("失敗");
                response.sendRedirect("teacherViewCourseRecord?courseno=" + courseno + "&page=" + page);
                return;
            }
            if (status == null) {
                out.println("失敗");
                response.sendRedirect("teacherViewCourseRecord?courseno=" + courseno + "&page=" + page);
                return;
            } else if (true && (status.equals("3") || status.equals("4"))) {
                courseRecord.setStatus(status);
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                courseRecord.setVerify_at(timestamp);
                courseRecord.setVerifier_staff_code(teacher.getStaffCode());
                courseRecord.setVerify_message(content);
                courseRecordDao.updateCourseRecordByID(courseRecord.getRg_no(), verifyid, courseRecord);
                out.println("成功送出");
                response.sendRedirect("teacherViewCourseRecord?courseno=" + courseno + "&page=" + page);

            } else {
                out.println("失敗");
                response.sendRedirect("teacherViewCourseRecord?courseno=" + courseno + "&page=" + page);
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }
}
