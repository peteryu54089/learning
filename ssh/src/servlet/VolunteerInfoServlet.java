package servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dao.VolunteerDao;
import dao.impl.VolunteerDaoImpl;
import model.Account;
import model.Authority;
import model.Volunteer;
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

@WebServlet("/volunteerInfo")

public class VolunteerInfoServlet extends _BasePerformanceServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        if (!AuthCheck.isAuth(request, Authority.RoleIndex.STUDENT)) {
            request.setAttribute("error", "您無權限使用此系統");
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            return;
        }
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");
        Student stu = account.getRoleDetail(Authority.RoleIndex.STUDENT);
        VolunteerDao volunteerDao = new VolunteerDaoImpl(account);
        String id = request.getParameter("id");
        try {
            Volunteer volunteer = volunteerDao.getVolunteerByID(stu.getRgno(), Integer.valueOf(id));
            Map<String, String> options = new LinkedHashMap<>();
            options.put("id", volunteer.getId().toString());
            options.put("name", volunteer.getName());
            options.put("place", volunteer.getPlace());
            options.put("startTime", volunteer.getStartTime());
            options.put("endTime", volunteer.getEndTime());
            options.put("count", volunteer.getCount());
            options.put("content", volunteer.getContent());
            options.put("external_link", volunteer.getExternalLink());
            setInfoMap(request, account, options, volunteer);
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

