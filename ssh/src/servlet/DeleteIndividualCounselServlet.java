package servlet;

import dao.IndividualCounselDao;
import dao.UploadFileDao;
import dao.impl.IndividualCounselDaoImpl;
import dao.impl.UploadFileDaoImpl;
import model.Account;
import model.Authority;
import model.IndividualCounsel;
import model.UploadFile;
import util.AuthCheck;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Ching Yun Yu on 2018/5/9.
 */

@WebServlet("/DeleteIndividualCounsel")
public class DeleteIndividualCounselServlet extends _BaseServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if (!AuthCheck.isAuth(request, Authority.RoleIndex.COUNSELOR)) {
//        	response.sendRedirect("login");
            request.setAttribute("error", "您無權限使用此系統");
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            return;
        }
        Account account = (Account) request.getSession().getAttribute("account");
        IndividualCounselDao individualCounselDao = new IndividualCounselDaoImpl(account);
        UploadFileDao uploadFileDao = new UploadFileDaoImpl(account);

        response.setHeader("Content-Type", "text/html; charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        int id = Integer.parseInt(request.getParameter("id"));
        try {
            Integer fileId = individualCounselDao.getIndividualCounselById(id).getFileId();
            uploadFileDao.deleteById(fileId);
            individualCounselDao.deleteIndividualCounsel(id);
            response.getWriter().write("刪除成功");
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().write("發生異常錯誤，若有疑問請洽系統管理員");
        }

    }

}
