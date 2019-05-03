package servlet;

import dao.CourseRecordDao;
import dao.impl.CourseRecordDaoImpl;
import model.Account;
import model.Authority.RoleIndex;
import model.CourseRecord;
import model.role.Student;
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

@WebServlet("/courseRecordsubmit")
@MultipartConfig
public class CourseRecordSubmitServlet extends _BaseServlet {


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        if (!AuthCheck.isAuth(request, RoleIndex.STUDENT)) {
//        	response.sendRedirect("login");
        	request.setAttribute("error", "您無權限使用此系統");
        	request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            return;
        }
        try {
            HttpSession session = request.getSession();
            Account account = (Account) session.getAttribute("account");
            String reg_no = account.getRegNumber();
            String id = request.getParameter("id");
            CourseRecordDao courseRecordDao = new CourseRecordDaoImpl(account);
            //TODO 更好的取得RGNO
            Integer rgno  =Integer.valueOf(((Student)account.getRoleDetail(RoleIndex.STUDENT)).getRgno());
            CourseRecord courseRecord = courseRecordDao.getCourseRecordByID(rgno, id);
            PrintWriter out = response.getWriter();
            if (courseRecord.isSubmittable()) {
                courseRecord.setStatus("2");
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                courseRecord.setSubmitted_at(timestamp);
                courseRecordDao.updateCourseRecordByID(rgno, id, courseRecord);
                out.println("成功送出");
            } else {
                out.println("失敗");
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }
}
