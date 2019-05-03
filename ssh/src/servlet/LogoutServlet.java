package servlet;

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
@WebServlet("/logout")
public class LogoutServlet extends _BaseServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        HttpSession session = request.getSession();
//        session.invalidate();
//        response.sendRedirect("login");

        try {
//            CookieES cookieES = new CookieES();
//            cookieES.cleanCookie(response);  // 清除Cookie: 即把MaxAge設為0
//            cookieES.setMaxAge(0);

            // 清session
            HttpSession session = request.getSession(false);  // 取得 session 物件;
            if (session != null) {
                session.invalidate();// 清除 session 中的所有物件
            }

            request.setAttribute("succsMsg", "./login");
            request.getRequestDispatcher("/WEB-INF/jsp/LogoutOk.jsp").forward(request, response);
            return;

        } catch (Exception ex) {
            request.setAttribute("error", "登出系統發生錯誤，請聯絡系統管理人員!");
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            return;
        }
    }
}
