package servlet;

import config.Config;
import dao.AccountDao;
import dao.impl.AccountDaoImpl;
import model.Account;
import model.Authority;
import services.account.AccountValidator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

@WebServlet("/LoginSID")
public class LoginSIDServlet extends _BaseServlet {
    private static final long serialVersionUID = 799114715804059548L;

    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8"); // 設定Get, Post編碼

        super.service(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();
        session.removeAttribute("selectedRoleIndexValue");
        session.removeAttribute("selectedRoleIndex");
        Account account = (Account) session.getAttribute("account");
        account.clearCurrentRole();
        session.setAttribute("account", account);

        String[] authRoleList = Arrays.stream(Authority.RoleIndex.values()).map(Enum::name).toArray(String[]::new);
        for (String authRole : authRoleList) {
            session.removeAttribute(authRole);
        }

        response.sendRedirect("/main");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        // 1. 取得Cookie(新北市教育局正式環境)
//        CookieES cookieES = new CookieES();
//        UserInfo userInfo = cookieES.getCookie(request);

        Properties loginProps = new Properties();
        AccountValidator accountValidator = AccountValidator.getInstance();

        Object userInfo = 1;
        if (userInfo != null) {
            final Config.LoginMethod loginMethod = Config.getLoginMethod();
            if (loginMethod == Config.LoginMethod.DEV) {
                loginProps.setProperty("username", request.getParameter("inputID"));
                loginProps.setProperty("password", request.getParameter("inputPassword"));
            } else if (loginMethod == Config.LoginMethod.LDAP) {
                //                loginProps.setProperty("sid", userInfo.getSid());
                //                loginProps.setProperty("role", userInfo.getRole());
                //                loginProps.setProperty("uid", String.valueOf(userInfo.getUid()));
            }

            String schSchema = accountValidator.getSchema(loginProps);

            try {
                AccountDao accountDao = new AccountDaoImpl(() -> schSchema);
                Account account = null;

//                if(userInfo.isAdmin()){
//                    account = accountDao.getAccountByRegNumber(Integer.toString(userInfo.getUid()));
//
//                    session.setAttribute("account", account);
//                    response.sendRedirect("manage");
//                } else
                {
                    /* 驗證小組學校系統測試中不開放給學生測試 */
//                    if(role.equals("std")){
//                        request.setAttribute("error", "系統測試中，您無權限使用此系統");
//                        request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
//                        return;
//                    }

                    account = accountValidator.verify(loginProps);

                    if (account == null || !account.isExist()) {
                        request.setAttribute("error", "您無權限使用此系統");
                        request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
                        return;
                    } else {
                        session.setAttribute("account", account);
                        response.sendRedirect("main");
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("error", "連線時發生錯誤");
                request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
                return;
            }
        } else {
            request.setAttribute("error", "您無權限使用此系統");
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            return;
        }


    }

}
