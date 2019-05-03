package servlet;

import dao.SystemConfigDao;
import dao.impl.SystemConfigDaoImpl;
import model.Account;
import model.Authority.RoleIndex;
import model.SystemConfig;
import org.apache.commons.codec.digest.DigestUtils;
import services.account.AccountValidator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by David on 2018/1/29.
 */

@WebServlet("/login")
public class LoginServlet extends _BaseServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        if (session.getAttribute("account") != null/*
        || session.getAttribute("teacher") != null || session.getAttribute("student") != null
        || session.getAttribute("manager") != null || session.getAttribute("counselor") != null
        || session.getAttribute("courseSubmitter") != null
        || session.getAttribute("performanceSubmitter") != null
        */) {
            response.sendRedirect("main");
            return;
        }
        request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
    }
}
