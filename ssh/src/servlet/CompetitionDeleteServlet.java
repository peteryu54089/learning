package servlet;

import dao.CompetitionDao;
import dao.impl.CompetitionDaoImpl;
import model.Account;
import model.Authority.RoleIndex;
import model.Competition;
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

@WebServlet("/competitiondelete")
@MultipartConfig
public class CompetitionDeleteServlet extends _BaseServlet {

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
            CompetitionDao competitionDao = new CompetitionDaoImpl(account);

            Integer rgno = Integer.valueOf(request.getParameter("rgno"));
            Integer id = Integer.valueOf(request.getParameter("id"));
            Competition competition = competitionDao.getCompetitionByID(rgno, id);

            if (!competition.student_modifiable()) {
                response.setContentType("text/html; charset=UTF-8");
                PrintWriter out = response.getWriter();
                out.println("<script>alert('無法刪除')</script>");
                out.println("<script>window.history.go(-1)</script>");
                return;
            }
            competitionDao.deleteCompetition(rgno, id);
            Integer after = competitionDao.getPageNumber(rgno);
            PrintWriter out = response.getWriter();
            if (after < Integer.parseInt(request.getParameter("page"))) {
                out.println(after);
            } else {
                String page = "1";
                if (request.getParameter("page") != null)
                    page = request.getParameter("page");
                out.println();
            }
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

