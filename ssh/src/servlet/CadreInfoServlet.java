package servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dao.CadreDao;
import dao.impl.CadreDaoImpl;
import model.Account;
import model.Authority;
import model.Cadre;
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

@WebServlet("/cadreInfo")

public class CadreInfoServlet extends _BasePerformanceServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");
        CadreDao cadreDao = new CadreDaoImpl(account);
        Integer rgno = null;
        if ((AuthCheck.isAuth(request, Authority.RoleIndex.STUDENT))) {
            rgno = account.<Student>getRoleDetail(Authority.RoleIndex.STUDENT).getRgno();
        }
        Integer id = Integer.valueOf(request.getParameter("id"));
        try {
            Cadre cadre = cadreDao.getCadreByID(rgno, id);
            Map<String, String> options = new LinkedHashMap<>();
            options.put("id", cadre.getId().toString());
            options.put("unit", cadre.getUnit());
            options.put("startTime", cadre.getStartTime());
            options.put("endTime", cadre.getEndTime());
            options.put("term", cadre.getTerm());
            options.put("level", cadre.getLevel());
            options.put("job", cadre.getJob());
            options.put("external_link", cadre.getExternalLink());
            options.put("content", cadre.getContent());
            setInfoMap(request, account, options, cadre);
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
