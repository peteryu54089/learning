package servlet;

import com.google.gson.Gson;
import dao.AnnouncementDao;
import dao.impl.AnnouncementDaoImpl;
import model.Account;
import model.Announcement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

@WebServlet("/announcement")
public class AnnouncementServlet extends _BaseServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        Account account;
        if ((account = (Account) request.getSession().getAttribute("account"))==null)
            return;

        AnnouncementDao dao = new AnnouncementDaoImpl(account);
        Gson gson = Announcement.getGson();

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        try (OutputStream os = response.getOutputStream()) {
            String json = gson.toJson(dao.listAvailableAnnouncements());
            os.write(json.getBytes(StandardCharsets.UTF_8));
        }
    }
}
