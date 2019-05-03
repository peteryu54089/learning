package servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dao.OtherDao;
import dao.impl.OtherDaoImpl;
import model.Account;
import model.Authority;
import model.Other;
import model.role.Student;
import util.AuthCheck;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@WebServlet("/otherInfo")

public class OtherInfoServlet extends _BasePerformanceServlet {

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
        OtherDao otherDao = new OtherDaoImpl(account);
        Integer rgno = account.<Student>getRoleDetail(Authority.RoleIndex.STUDENT).getRgno();
        Integer id = Integer.valueOf(request.getParameter("id"));
        try {
            Other other = otherDao.getOtherByID(rgno, id);
            Map<String, String> options = new LinkedHashMap<>();
            options.put("id", other.getId().toString());
            options.put("name", other.getName());
            options.put("unit", other.getUnit());
            options.put("startTime", other.getStartTime());
            options.put("endTime", other.getEndTime());
            options.put("count", other.getCount());
            options.put("content", other.getContent());
            options.put("type", other.getType());
            options.put("external_link", other.getExternalLink());
            setInfoMap(request, account, options, other);
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

