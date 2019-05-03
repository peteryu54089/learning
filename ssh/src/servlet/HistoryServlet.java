package servlet;

import dao.*;
import dao.impl.*;
import model.Account;
import model.Authority.RoleIndex;
import model.role.Student;
import util.AuthCheck;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by David on 2018/1/29.
 */

@WebServlet("/history")
public class HistoryServlet extends _BaseServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!AuthCheck.isAuth(request, RoleIndex.STUDENT)) {
//        	response.sendRedirect("login");
        	request.setAttribute("error", "您無權限使用此系統");
        	request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            return;
        }

        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");


        CadreDao cadredao = new CadreDaoImpl(account);
        CompetitionDao competitiondao = new CompetitionDaoImpl(account);
        LicenseRecordDao licensedao = new LicenseRecordDaoImpl(account);
        VolunteerDao volunteerdao = new VolunteerDaoImpl(account);
        OtherDao otherdao = new OtherDaoImpl(account);

        Student student = (Student) account.getRoleDetail(RoleIndex.STUDENT);
        String idno = account.getIdNumber();
        //List<Profit> profitList = profitdao.getProfitByIdno(idno);
        //List<Cadre> cadreList = cadredao.getCadreByIdno(idno);
        //List<Competition> competitionList = competitiondao.getCompetitionByIdno(idno);
        //List<License> licenseList = licensedao.getLicenseByIdno(idno);
        //List<Volunteer> volunteerList = volunteerdao.getVolunteerByIdno(idno);
        //List<Other> otherList = otherdao.getOtherByIdno(idno);
        //request.setAttribute("profitList", profitList);
        //request.setAttribute("cadreList", cadreList);
        //request.setAttribute("competitionList", competitionList);
        //request.setAttribute("licenseList", licenseList);
        //request.setAttribute("volunteerList", volunteerList);
        //request.setAttribute("otherList", otherList);
        request.getRequestDispatcher("/WEB-INF/jsp/history.jsp").forward(request, response);
    }
}
