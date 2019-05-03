package servlet;

import com.google.gson.Gson;

import config.Config;
import dao.AccountDao;
import dao.impl.AccountDaoImpl;
import model.Account;
import model.Authority.RoleIndex;
import model.ModelList;
import util.AuthCheck;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet("/manage")
public class ManageServlet extends _BaseServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String keyword = request.getParameter("keyword");
        if (keyword == null) keyword = "";
        int page = 1;
        try {
        	page = Integer.parseInt(request.getParameter("page"));
        } catch (NumberFormatException ignored) {}

        Account account = (Account) request.getSession().getAttribute("account");
        AccountDao accountDao = new AccountDaoImpl(account);
        
        ModelList<Account> accountList = accountDao.getStaffByNameOrStaffCode(keyword, page);
        request.setAttribute("accountList", accountList);
        request.setAttribute("keyword", keyword);
        request.setAttribute("page", page);
        request.setAttribute("keywordJson", new Gson().toJson(keyword));
        request.setAttribute("itemsPerPage", accountDao.getItemsPerPage());

        request.getRequestDispatcher("/WEB-INF/jsp/mainPage/adminMain.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Account account = (Account) request.getSession().getAttribute("account");
        if (account == null) {
            response.setStatus(401);
            try (PrintWriter printWriter = new PrintWriter(response.getOutputStream())) {
                printWriter.print("false");
            }
            return;
        }

        try {
            AccountDao accountDao = new AccountDaoImpl(account);
            response.setContentType("application/json");

            String staffCode = request.getParameter("staffCode");
            boolean setManager = Integer.parseInt(request.getParameter("setManager")) != 0;
            Account target = accountDao.getAccountByRegNumber(staffCode);
            if (setManager) {
                target.grantRole(RoleIndex.MANAGER);
            } else {
                target.revokeRole(RoleIndex.MANAGER);
            }
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
