package servlet.management;

import dao.StudentDao;
import dao.impl.StudentDaoImpl;
import model.Account;
import model.Authority;
import model.role.Student;
import util.MenuBarUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/showStudentBasicInfo")
@MultipartConfig
public class ShowStudentBasicInfoServlet extends BaseManageServlet {


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        if (checkPermission(request, response) == null)
            return;

        bindStuClsInfo(request, response);
        request.setAttribute("title", "查詢學生基本資料");
        request.setAttribute("MenuBarUtil", new MenuBarUtil());

        request.getRequestDispatcher("/WEB-INF/jsp/managerPage/studentBasicInfo.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        Account account;
        if ((account = checkPermission(request, response)) == null)
            return;

        StudentDao studentDao = new StudentDaoImpl(account);
        Student stu = studentDao.getStudentByRgno(
                Integer.valueOf(request.getParameter("rgno"))
        );
        request.setAttribute("student", stu);
        request.getRequestDispatcher("/WEB-INF/jsp/managerPage/components/stuBasicInfoModal.jsp").forward(request, response);
    }

    protected Account checkPermission(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");
        if (account == null || !account.getAuthority()
                .containsRole(
                        Authority.RoleIndex.MANAGER,
                        Authority.RoleIndex.WORK_TEAM
                )
        ) {
            response.setStatus(401);
            return null;
        }

        return account;
    }
}
