package servlet;

import dao.PerformanceSubmitSetDao;
import dao.impl.PerformanceSubmitSetDaoImpl;
import model.Account;
import model.Authority.RoleIndex;
import model.PerformanceSubmitSet;
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

@WebServlet("/PerformanceSubmitSet")
public class PerformanceSubmitSetServlet extends _BaseServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if (!AuthCheck.isAuth(request, RoleIndex.MANAGER)) {
//        	response.sendRedirect("login");
        	request.setAttribute("error", "您無權限使用此系統");
        	request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            return;
        }

        Account account = (Account) request.getSession().getAttribute("account");
        PerformanceSubmitSetDao performancesubmitsetdao = new PerformanceSubmitSetDaoImpl(account);
        try {
            PerformanceSubmitSet performanceSubmitSet = performancesubmitsetdao.getPerformanceSubmitSet();
            request.setAttribute("performanceSubmitSet", performanceSubmitSet);
            request.getRequestDispatcher("/WEB-INF/jsp/performanceSubmitSet.jsp").forward(request, response);
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

        Account account = (Account) request.getSession().getAttribute("account");
        PerformanceSubmitSetDao performancesubmitsetdao = new PerformanceSubmitSetDaoImpl(account);

        String text1 = request.getParameter("text1");
        String text2 = request.getParameter("text2");
        String text3 = request.getParameter("text3");
        String text4 = null;
        String text5 = null;
        PerformanceSubmitSet performanceSubmitSet = new PerformanceSubmitSet(text1, text2, text3, text4, text5);
        try {
            if (performancesubmitsetdao.getPerformanceSubmitSet() != null) {
                performancesubmitsetdao.setPerformanceSubmitSet(performanceSubmitSet);
            } else {
                performancesubmitsetdao.insertPerformanceSubmitSet(performanceSubmitSet);
            }
            request.setAttribute("result", "<font color = \"red\" > 設定成功 </font>");
            request.setAttribute("performanceSubmitSet", performanceSubmitSet);
            request.getRequestDispatcher("/WEB-INF/jsp/performanceSubmitSet.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
