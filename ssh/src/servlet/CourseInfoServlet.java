package servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dao.SelectCourseDao;
import dao.impl.SelectCourseDaoImpl;
import model.Account;
import model.Authority;
import model.Course;
import util.AuthCheck;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@WebServlet("/courseInfo")

public class CourseInfoServlet extends _BaseServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        if (!AuthCheck.isAuth(request, Authority.RoleIndex.STUDENT)) {
//        	response.sendRedirect("login");
        	request.setAttribute("error", "您無權限使用此系統");
        	request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            return;
        }

        Account account = (Account) request.getSession().getAttribute("account");
        SelectCourseDao selectCourseDao = new SelectCourseDaoImpl(account);
        String course_no = request.getParameter("course_no");
        try {
            Course course = selectCourseDao.getSelectCourseByCourseNo(course_no);
            Map<String, String> options = new LinkedHashMap<>();
            options.put("term_year", course.getTerm_year());
            options.put("term_sem", course.getTerm_sem());
            options.put("course_num", course.getCourse_no());
            options.put("course_cname", course.getCourse_Cname());
            options.put("names", course.getNames());
            Gson gson = new GsonBuilder().create();
            String json = gson.toJson(options);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);

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

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
