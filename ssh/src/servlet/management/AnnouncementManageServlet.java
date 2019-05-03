package servlet.management;

import dao.AnnouncementDao;
import dao.impl.AnnouncementDaoImpl;
import model.Account;
import model.Announcement;
import util.DateUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@WebServlet("/announcementManage")
public class AnnouncementManageServlet extends BaseManageServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        Account account;
        if ((account = checkPermission(request, response))==null)
            return;

        int page = 1;
        try {
            page = Integer.parseInt(request.getParameter("page"));
        } catch (Exception ignored) {
        }

        AnnouncementDao dao = new AnnouncementDaoImpl(account);
        List<Announcement> announcementList = dao.getAnnouncements(page);
        request.setAttribute("list", announcementList);
        request.setAttribute("page", page);
        request.setAttribute("amount", dao.getItemCount());
        request.setAttribute("itemsPerPage", AnnouncementDao.ITEM_PER_PAGE);
        request.setAttribute("DateUtils", new DateUtils());
        request.setAttribute("Announcement", new Announcement(0, (String) null, new Date(), new Date(), new Timestamp(new Date().getTime())));

        request.getRequestDispatcher("/WEB-INF/jsp/managerPage/manageAnnouncement.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        Account account;
        if ((account = checkPermission(request, response))==null)
            return;

        AnnouncementDao dao = new AnnouncementDaoImpl(account);

        Announcement announcement = parseAnnouncementFromRequest(request);
        if (dao.insertAnnouncement(announcement) > 0) {
            response.setStatus(201);
        } else {
            response.setStatus(400);
        }
    }

    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        Account account;
        if ((account = checkPermission(request, response))==null)
            return;

        AnnouncementDao dao = new AnnouncementDaoImpl(account);

        Announcement announcement = parseAnnouncementFromRequest(request);
        if (dao.updateAnnouncement(announcement) > 0) {
            response.setStatus(204);
        } else {
            response.setStatus(400);
        }
    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        Account account;
        if ((account = checkPermission(request, response))==null)
            return;

        AnnouncementDao dao = new AnnouncementDaoImpl(account);

        int id = -1;

        try {
            id = Integer.parseInt(request.getParameter("id"));
        } catch (Exception ignored) {

        }
        if (dao.deleteAnnouncement(id)) {
            response.setStatus(204);
        } else {
            response.setStatus(400);
        }
    }

    private Announcement parseAnnouncementFromRequest(HttpServletRequest request) {
        Announcement announcement = null;
        try {
            try (InputStream is = request.getInputStream()) {
                try (Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
                    announcement = Announcement.getGson().fromJson(reader, Announcement.class);
                }
            }

            return announcement;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
