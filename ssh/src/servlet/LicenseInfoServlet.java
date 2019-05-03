package servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dao.LicenseRecordDao;
import dao.impl.LicenseRecordDaoImpl;
import model.Account;
import model.Authority;
import model.License;
import model.role.Student;
import util.AuthCheck;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@WebServlet("/licenseInfo")

public class LicenseInfoServlet extends _BasePerformanceServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        if (!AuthCheck.isAuth(request, Authority.RoleIndex.STUDENT)) {
//        	response.sendRedirect("login");
        	request.setAttribute("error", "您無權限使用此系統");
        	request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            return;
        }
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");
        LicenseRecordDao licenseRecordDao = new LicenseRecordDaoImpl(account);
        Student stu = account.getRoleDetail(Authority.RoleIndex.STUDENT);
        String id = request.getParameter("id");
        try {
            License license = licenseRecordDao.getLicenseByID(stu.getRgno(), Integer.valueOf(id));
            Map<String, String> options = new LinkedHashMap<>();
            options.put("id", license.getId().toString());
            options.put("code", license.getCode());
            options.put("note", license.getNote());
            options.put("point", license.getPoint());
            options.put("result", license.getResult());
            options.put("time", license.getTime());
            options.put("licensenumber", license.getLicensenumber());
            options.put("group", license.getGroup());
            options.put("external_link", license.getExternalLink());
            options.put("content", license.getContent());
            setInfoMap(request, account, options, license);

            Gson gson = new GsonBuilder().create();
            String json = gson.toJson(options);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);

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

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}

