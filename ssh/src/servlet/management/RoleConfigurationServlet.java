package servlet.management;

import com.google.gson.Gson;
import dao.AccountDao;
import dao.impl.AccountDaoImpl;
import model.Account;
import model.Authority;
import model.ModelList;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@WebServlet("/roleConfiguration")
@MultipartConfig
public class RoleConfigurationServlet extends BaseManageServlet {
    private List<Authority.RoleIndex> ignoreRoles = Arrays.asList(
            Authority.RoleIndex.STUDENT,
            Authority.RoleIndex.TEACHER,
            Authority.RoleIndex.TUTOR,
            Authority.RoleIndex.ADMIN
    );

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        Account account;
        if ((account = checkPermission(request, response))==null)
            return;

        AccountDao accountDao = new AccountDaoImpl(account);
        String keyword = request.getParameter("keyword");
        if (keyword == null) keyword = "";
        int page = 1;
        try {
            page = Integer.parseInt(request.getParameter("page"));
        } catch (NumberFormatException ignored) {
        }

        ModelList<Account> accountList = accountDao.getStaffByNameOrStaffCode(keyword, page);
        request.setAttribute("keyword", keyword);
        request.setAttribute("keywordJson", new Gson().toJson(keyword));
        request.setAttribute("accountList", accountList);
        request.setAttribute("itemsPerPage", accountDao.getItemsPerPage());
        request.setAttribute("page", page);
        List<Authority.RoleIndex> roleIndexList = new ArrayList<>(Arrays.asList(Authority.RoleIndex.values()));
        roleIndexList.remove(Authority.RoleIndex.NONE);
        roleIndexList.remove(Authority.RoleIndex.ADMIN);
        request.setAttribute("roleList", roleIndexList);
        request.setAttribute("ignoreRoles", ignoreRoles);

        request.getRequestDispatcher("/WEB-INF/jsp/managerPage/roleConfiguration.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        Account account;
        if ((account = checkPermission(request, response))==null)
            return;

        AccountDao accountDao = new AccountDaoImpl(account);
        response.setContentType("application/json");

        try {
            String staffCode = request.getParameter("staffCode");
            int roleCode = Integer.parseInt(request.getParameter("roleCode"));
            Account target = accountDao.getAccountByRegNumber(staffCode);
            target.setRoleCode(roleCode);
            accountDao.updateRole(target);
            try (PrintWriter printWriter = new PrintWriter(response.getOutputStream())) {
                printWriter.print("true");
            }

        } catch (NullPointerException | SQLException e) {
            e.printStackTrace();
            response.setStatus(500);
            try (PrintWriter printWriter = new PrintWriter(response.getOutputStream())) {
                printWriter.print("false");
            }
        }
    }
}
