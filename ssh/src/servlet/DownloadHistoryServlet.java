package servlet;

import dao.ProfitDao;
import dao.impl.ProfitDaoImpl;
import model.Account;
import model.Profit;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@WebServlet("/DownloadHistory")
public class DownloadHistoryServlet extends _BaseServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");

        ProfitDao profitDao = new ProfitDaoImpl(account);

        List<Profit> profitList = Collections.emptyList();
            //List<Cadre> cadreList = cadreDao.getCadre();
            // List<Competition> competitionList = competitionDao.getCompetition();
            // List<License> licenseList = licenseDao.getLicense();
            // List<Volunteer> volunteerList = volunteerDao.getVolunteer();
            //List<Other> otherList = otherDao.getOther();

            //  session.setAttribute("profitList", profitList);
            //session.setAttribute("cadreList", cadreList);
            // session.setAttribute("competitionList", competitionList);
            //session.setAttribute("licenseList", licenseList);
            // session.setAttribute("volunteerList", volunteerList);
            // session.setAttribute("otherList", otherList);

            request.getRequestDispatcher("WEB-INF/jsp/downloadHistory.jsp").forward(request, response);
    }
}
