package servlet;

import dao.DocumentDao;
import dao.UploadFileDao;
import dao.impl.DocumentDaoImpl;
import dao.impl.UploadFileDaoImpl;
import model.Account;
import model.Authority;
import model.Authority.RoleIndex;
import model.Document;
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

@WebServlet("/documentdelete")
@MultipartConfig
public class DocumentDeleteServlet extends _BaseServlet {
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
            DocumentDao documentdao = new DocumentDaoImpl(account);
            //TODO 更好的取得RGNO
            Integer rgno = ((Student) account.getRoleDetail(Authority.RoleIndex.STUDENT)).getRgno();
            String reg_no = account.getRegNumber();
            Integer id = Integer.parseInt(request.getParameter("id"));
            //刪除
            UploadFileDao uploadFileDao = new UploadFileDaoImpl(account);
            Document document = documentdao.findDocument(id,rgno);
            if(document != null)
            {
                    uploadFileDao.deleteById(document.getUpload_file_id());
            }

            //刪除
            documentdao.deleteDocument(rgno, id);
            Integer after = documentdao.getPageNumber(rgno);
            PrintWriter out = response.getWriter();
            if (after < Integer.parseInt(request.getParameter("page"))) {
                out.println(after);
            } else {
                String page = "1";
                if (request.getParameter("page") != null)
                    page = request.getParameter("page");
                out.println();
            }
        } catch (NullPointerException | NumberFormatException | SQLException e) {
            e.printStackTrace();
            response.getWriter().write("發生異常錯誤，若有疑問請洽系統管理員");
            return;
        }

    }
}
