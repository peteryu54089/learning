package servlet;

import dao.FinalDao;
import dao.impl.FinalDaoImpl;
import model.Account;
import model.Authority.RoleIndex;
import model.role.Student;
import util.AuthCheck;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by David on 2018/1/29.
 */

@WebServlet("/deleteFinal")
public class DeleteFinalServlet extends _BaseServlet {


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!AuthCheck.isAuth(request, RoleIndex.STUDENT)) {
//        	response.sendRedirect("login");
        	request.setAttribute("error", "您無權限使用此系統");
        	request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            return;
        }

        HttpSession session = request.getSession();
        response.setHeader("Content-Type", "text/html; charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        Account account = (Account) session.getAttribute("account");
        FinalDao finaldao = new FinalDaoImpl(account);
        Student student = (Student) account.getRoleDetail(RoleIndex.STUDENT);
        String type = request.getParameter("type");
        int id = Integer.parseInt(request.getParameter("id"));
        try {
            if (type == null) {
                response.getWriter().write("發生異常錯誤，若有疑問請洽系統管理員");
                return;
            }
            if (finaldao.getByIdAndType(id, type, account.getIdNumber())) {
                response.getWriter().write("你無權刪除此檔案");
                return;
            }
            finaldao.deleteFinalByidAndIdno(id, type, account.getIdNumber());
            response.getWriter().write("刪除成功");
            return;
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().write("發生異常錯誤，若有疑問請洽系統管理員");
        }
    }
}
