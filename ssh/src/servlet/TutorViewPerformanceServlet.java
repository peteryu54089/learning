package servlet;

import dao.*;
import dao.impl.*;
import model.*;
import model.role.Student;
import util.AuthCheck;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/tutorViewPerformance")
public class TutorViewPerformanceServlet extends _BaseServlet {


    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        if (!AuthCheck.isAuth(request)) {
//        	response.sendRedirect("login");
        	request.setAttribute("error", "您無權限使用此系統");
        	request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            return;
        }
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");
        CadreDao cadreDao = new CadreDaoImpl(account);
        CompetitionDao competitionDao = new CompetitionDaoImpl(account);
        LicenseRecordDao licenseRecordDao = new LicenseRecordDaoImpl(account);
        VolunteerDao volunteerDao = new VolunteerDaoImpl(account);
        OtherDao otherDao = new OtherDaoImpl(account);
        StudentDao studentDao = new StudentDaoImpl(account);
        if (!account.getAuthority().isTutor()) {
//        	response.sendRedirect("login");
        	request.setAttribute("error", "您無權限使用此系統");
        	request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            return;
        }

        Integer rgno = Integer.parseInt(request.getParameter("rgno"));
        Student student = studentDao.getStudentByRgno(rgno);
        String type = request.getParameter("type");
        if (type == null)
            type = "1";

        try {
            Integer page = 1;
            if (request.getParameter("page") != null) {
                page = Integer.parseInt(request.getParameter("page"));
            }
            Integer pageAmount = 1;
            if (type.equals("1")) {
                List<Cadre> cadreList = cadreDao.getCadre(rgno, null, null, null, page);
                pageAmount = cadreDao.getPageNumber(rgno);
                request.setAttribute("cadreList", cadreList);
            }
            if (type.equals("2")) {
                List<Competition> competitionList = competitionDao.getCompetition(rgno, null, null, null, page);
                pageAmount = competitionDao.getPageNumber(rgno);
                request.setAttribute("competitionList", competitionList);
                Field field = new Field();
                request.setAttribute("field", field);
            }
            if (type.equals("3")) {
                List<License> licenseList = licenseRecordDao.getLicense(rgno, null, null, null, page);
                pageAmount = licenseRecordDao.getPageNumber(rgno);
                request.setAttribute("licenseList", licenseList);
            }
            if (type.equals("4")) {
                List<Volunteer> volunteerList = volunteerDao.getVolunteer(rgno, null, null, null, page);
                pageAmount = volunteerDao.getPageNumber(rgno);
                request.setAttribute("volunteerList", volunteerList);
            }
            if (type.equals("5")) {
                List<Other> otherList = otherDao.getOther(rgno, null, null, null, page, null);
                pageAmount = competitionDao.getPageNumber(rgno);
                request.setAttribute("otherList", otherList);
            }
            request.setAttribute("rgno", rgno);
            request.setAttribute("page", page);
            request.setAttribute("student", student);
            request.setAttribute("type", type);
            request.setAttribute("pageAmount", pageAmount);
            request.getRequestDispatcher("/WEB-INF/jsp/tutorViewPerformance.jsp").forward(request, response);

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
}
