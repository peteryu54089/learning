package servlet;

import dao.CadreDao;
import dao.StudentDao;
import dao.SystemConfigDao;
import dao.impl.CadreDaoImpl;
import dao.impl.StudentDaoImpl;
import dao.impl.SystemConfigDaoImpl;
import model.Account;
import model.Authority;
import model.Cadre;
import model.SystemConfig;
import model.role.Student;
import util.AuthCheck;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by David on 2018/1/29.
 */

@WebServlet("/cadreRecordViewCadreRecord")
@MultipartConfig
public class CadreRecordViewCadreRecordServlet extends _BaseServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        if (!AuthCheck.isAuth(request)) {
//        	response.sendRedirect("login");
            request.setAttribute("error", "您無權限使用此系統");
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            return;
        }
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");

        if (!account.getAuthority().containsRole(Authority.RoleIndex.MANAGER, Authority.RoleIndex.WORK_TEAM, Authority.RoleIndex.CADRE_SUBMITTER)) {
            response.setStatus(401);
            return;
        }

        CadreDao cadredao = new CadreDaoImpl(account);
        StudentDao studentdao = new StudentDaoImpl(account);
        SystemConfigDao systemConfigDao = new SystemConfigDaoImpl(account);
        SystemConfig systemConfig = systemConfigDao.getSystemConfig();
        try {
            request.setAttribute("docSize", systemConfig.getPerformance().getMaxDocSize());
            request.setAttribute("docAllowType", systemConfig.getPerformance().getAllowDocTypesString());
            request.setAttribute("videoSize", systemConfig.getPerformance().getMaxVideoSize());
            request.setAttribute("videoAllowType", systemConfig.getPerformance().getAllowVideoTypesString());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        try {
            Integer page = 1;
            if (request.getParameter("page") != null) {
                page = Integer.parseInt(request.getParameter("page"));
            }
            Integer rgno = Integer.valueOf(request.getParameter("rgno"));
            Student student = studentdao.getStudentByRgno(rgno);

            List<Cadre> cadreList = cadredao.getCadre(rgno, null, null, null, page);
            Integer pageAmount = cadredao.getPageNumber(rgno);
            request.setAttribute("student", student);
            request.setAttribute("rgno", rgno);
            request.setAttribute("page", page);
            request.setAttribute("pageAmount", pageAmount);
            request.setAttribute("cadreList", cadreList);
            request.getRequestDispatcher("WEB-INF/jsp/cadreRecordViewCadreRecord.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
