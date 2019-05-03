package servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dao.CompetitionDao;
import dao.impl.CompetitionDaoImpl;
import model.Account;
import model.Authority;
import model.Competition;
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

@WebServlet("/competitionInfo")

public class CompetitionInfoServlet extends _BaseServlet {

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
        CompetitionDao competitionDao = new CompetitionDaoImpl(account);
        Student student =account.getRoleDetail(Authority.RoleIndex.STUDENT);
        Integer rgno = student.getRgno();
        Integer id = Integer.valueOf(request.getParameter("id"));
        try {
            Competition competition = competitionDao.getCompetitionByID(rgno, id);
            Map<String, Object> options = new LinkedHashMap<>();
            options.put("id", competition.getId().toString());
            options.put("name", competition.getName());
            options.put("item", competition.getItem());
            options.put("field", competition.getField());
            options.put("level", competition.getLevel());
            options.put("award", competition.getAward());
            options.put("time", competition.getTime());
            options.put("content", competition.getContent());
            options.put("type", competition.getType());
            options.put("document_file_id", competition.getDocumentFileId());
            options.put("video_file_id", competition.getVideoFileId());
            options.put("external_link", competition.getExternalLink());
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
