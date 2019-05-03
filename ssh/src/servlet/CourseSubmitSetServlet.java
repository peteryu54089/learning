package servlet;

import dao.CourseSubmitSetDao;
import dao.impl.CourseSubmitSetDaoImpl;
import model.Account;
import model.Authority.RoleIndex;
import model.CourseSubmitSet;
import util.AuthCheck;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by Ching-Yun Yu on 2018/5/31.
 */

@WebServlet("/CourseSubmitSet")
public class CourseSubmitSetServlet extends _BaseServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if (!AuthCheck.isAuth(request, RoleIndex.MANAGER)) {
//        	response.sendRedirect("login");
        	request.setAttribute("error", "您無權限使用此系統");
        	request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            return;
        }

        Account account = (Account) request.getSession().getAttribute("account");

        try {
            CourseSubmitSetDao coursesubmitsetdao = new CourseSubmitSetDaoImpl(account);
            CourseSubmitSet courseSubmitSet = coursesubmitsetdao.getCourseSubmitSet();
            request.setAttribute("courseSubmitSet", courseSubmitSet);
            request.getRequestDispatcher("/WEB-INF/jsp/courseSubmitSet.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if (!AuthCheck.isAuth(request, RoleIndex.MANAGER)) {
//        	response.sendRedirect("login");
        	request.setAttribute("error", "您無權限使用此系統");
        	request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            return;
        }

        String text1 = request.getParameter("text1");
        String text2 = request.getParameter("text2");
        String text3 = request.getParameter("text3");
        String text4 = request.getParameter("text4");
        String text5 = request.getParameter("text5");
        CourseSubmitSet courseSubmitSet = new CourseSubmitSet(text1, text2, text3, text4, text5);
        Account account = (Account) request.getSession().getAttribute("account");
        CourseSubmitSetDao coursesubmitsetdao = new CourseSubmitSetDaoImpl(account);
        try {
            if (coursesubmitsetdao.getCourseSubmitSet() != null) {
                coursesubmitsetdao.setCourseSubmitSet(courseSubmitSet);
            } else {
                coursesubmitsetdao.insertCourseSubmitSet(courseSubmitSet);
            }
            request.setAttribute("result", "<font color = \"red\" > 設定成功 </font>");
            request.setAttribute("courseSubmitSet", courseSubmitSet);
            request.getRequestDispatcher("/WEB-INF/jsp/courseSubmitSet.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
