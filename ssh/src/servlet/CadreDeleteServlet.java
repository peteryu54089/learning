package servlet;

import dao.CadreDao;
import dao.impl.CadreDaoImpl;
import model.Account;
import model.Authority;
import model.Authority.RoleIndex;
import model.Cadre;
import model.role.Student;
import util.AuthCheck;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/cadredelete")
@MultipartConfig
public class CadreDeleteServlet extends _BaseServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        try {
            HttpSession session = request.getSession();
            Account account = (Account) session.getAttribute("account");

            CadreDao cadreDao = new CadreDaoImpl(account);
            Integer id = Integer.valueOf(request.getParameter("id"));
            Cadre cadre = cadreDao.getCadreByID(null, id);
            Integer rgno = 0;
            if ((AuthCheck.isAuth(request, Authority.RoleIndex.STUDENT))) {
                rgno = account.<Student>getRoleDetail(RoleIndex.STUDENT).getRgno();
            } else if (AuthCheck.isAuth(request, RoleIndex.CADRE_SUBMITTER)) {
                rgno = cadre.getRgno();
            }

            if (AuthCheck.isAuth(request, RoleIndex.STUDENT)) {
                if (!cadre.student_modifiable()) {
                    response.getWriter().write("無法刪除");
                    return;
                }
            }

            if (AuthCheck.isAuth(request, RoleIndex.CADRE_SUBMITTER)) {
                if (!cadre.cadre_submitter_modifiable()) {
                    response.getWriter().write("無法刪除");
                    return;
                }
            }
            cadreDao.deleteCadre(rgno, id);
            Integer after = cadreDao.getPageNumber(rgno);
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
