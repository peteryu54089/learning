package servlet;

import dao.*;
import dao.impl.*;
import model.*;
import model.role.Student;
import util.AuthCheck;
import util.DateUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by David on 2018/1/29.
 */

@WebServlet("/performanceCheck")
@MultipartConfig
public class PerformanceCheckServlet extends _BaseServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        if (!AuthCheck.isAuth(request, Authority.RoleIndex.STUDENT)) {
//        	response.sendRedirect("login");
        	request.setAttribute("error", "您無權限使用此系統");
        	request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            return;
        }
        try {
            HttpSession session = request.getSession();
            Account account = (Account) session.getAttribute("account");
            CadreDao cadredao = new CadreDaoImpl(account);
            CompetitionDao competitiondao = new CompetitionDaoImpl(account);
            LicenseRecordDao licensedao = new LicenseRecordDaoImpl(account);
            VolunteerDao volunteerdao = new VolunteerDaoImpl(account);
            OtherDao otherdao = new OtherDaoImpl(account);

            SystemConfigDao systemConfigDao = new SystemConfigDaoImpl(account);
            Student student = account.getRoleDetail(Authority.RoleIndex.STUDENT);
            PerformanceUnlockStatusDao performanceUnlockStatusDao = new PerformanceUnlockStatusDaoImpl(account);
            SystemConfig systemConfig = systemConfigDao.getSystemConfig();
            Integer reg_no = student.getRgno();
            Integer id = Integer.valueOf(request.getParameter("id"));
            String type = request.getParameter("type");
            Integer check = Integer.valueOf(request.getParameter("check"));
            String submited = request.getParameter("submited");
            
            //TODO 檢查是否可勾選
            if (submited == null) {
                if (type.equals("cadre")) {
                    Cadre cadre = cadredao.getCadreByID(reg_no, id);
                    cadre.setCheck(check);
                    cadredao.updateCadreByID(cadre, reg_no, id);
                }
                if (type.equals("competition")) {
                    Competition competition = competitiondao.getCompetitionByID(reg_no, id);
                    competition.setCheck(check);
                    competitiondao.updateCompetitionByID(competition, reg_no, id);
                }
                if (type.equals("license")) {
                    License license = licensedao.getLicenseByID(reg_no, id);
                    license.setCheck(check);
                    licensedao.updateLicenseByID(license, reg_no, id);
                }
                if (type.equals("volunteer")) {
                    Volunteer volunteer = volunteerdao.getVolunteerByID(reg_no, id);
                    volunteer.setCheck(check);
                    volunteerdao.updateVolunteerByID(volunteer, reg_no, id);
                }
                if (type.equals("other")) {
                    Other other = otherdao.getOtherByID(reg_no, id);
                    other.setCheck(check);
                    otherdao.updateOtherByID(other, reg_no, id);
                }
            } else {
                //TODO 檢查是否為當學年/學期
                //TODO 抓取各學年/學期上限
                String selectyear = Integer.toString(systemConfig.getPerformance().getSubmitYear());
                List<Cadre> cadreList = cadredao.getCadre(student.getRgno(), "1", null, null, null);
                cadreList.addAll(cadredao.getCadre(student.getRgno(), null, null, selectyear, null));
                List<Competition> competitionList = competitiondao.getCompetition(student.getRgno(), "1", null, null, null);
                competitionList.addAll(competitiondao.getCompetition(student.getRgno(), null, null, selectyear, null));
                List<License> licenseList = licensedao.getLicense(student.getRgno(), "1", null, null, null);
                licenseList.addAll(licensedao.getLicense(student.getRgno(), null, null, selectyear, null));
                List<Volunteer> volunteerList = volunteerdao.getVolunteer(student.getRgno(), "1", null, null, null);
                volunteerList.addAll(volunteerdao.getVolunteer(student.getRgno(), null, null, selectyear, null));
                List<Other> otherList = otherdao.getOther(student.getRgno(), "1", null, null, null, null);
                otherList.addAll(otherdao.getOther(student.getRgno(), null, null, selectyear, null, null));
                Integer count = cadreList.size() + competitionList.size() + licenseList.size() + volunteerList.size();//目前勾選數量
                if (count > systemConfig.getPerformance().getMaxSubmitAmount()) {
                    response.getWriter().write("數量錯誤");
                    return;
                }
                performanceUnlockStatusDao.revokeUnlock(Integer.toString(systemConfig.getPerformance().getSubmitYear()), student.getRgno());
                for (Cadre c : cadreList) {
                    c.setStatus("2");
                    c.setCheck(0);
                    c.setSelectedYear(Integer.toString(systemConfig.getPerformance().getSubmitYear()));
                    cadredao.updateCadreByID(c, student.getRgno(),(c.getId()));
                }
                for (Competition c : competitionList) {
                    c.setStatus("2");
                    c.setCheck(0);
                    c.setSelectedYear(Integer.toString(systemConfig.getPerformance().getSubmitYear()));
                    competitiondao.updateCompetitionByID(c, student.getRgno(), (c.getId()));
                }
                for (License c : licenseList) {
                    c.setStatus("2");
                    c.setCheck(0);
                    c.setSelectedYear(Integer.toString(systemConfig.getPerformance().getSubmitYear()));
                    licensedao.updateLicenseByID(c, student.getRgno(), (c.getId()));
                }
                for (Volunteer c : volunteerList) {
                    c.setStatus("2");
                    c.setCheck(0);
                    c.setSelectedYear(Integer.toString(systemConfig.getPerformance().getSubmitYear()));
                    volunteerdao.updateVolunteerByID(c, student.getRgno(), (c.getId()));
                }
                for (Other c : otherList) {
                    c.setStatus("2");
                    c.setCheck(0);
                    c.setSelectedYear(Integer.toString(systemConfig.getPerformance().getSubmitYear()));
                    otherdao.updateOtherByID(c, student.getRgno(), (c.getId()));
                }
            }
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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        if (!AuthCheck.isAuth(request)) {
//        	response.sendRedirect("login");
        	request.setAttribute("error", "您無權限使用此系統");
        	request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            return;
        }

        CadreDao cadredao;
        CompetitionDao competitiondao;
        LicenseRecordDao licensedao;
        VolunteerDao volunteerdao;
        SystemConfigDao systemConfigDao;
        OtherDao otherdao;
        SystemConfig systemConfig;

        try {
            HttpSession session = request.getSession();
            Account account = (Account) session.getAttribute("account");

            cadredao = new CadreDaoImpl(account);
            competitiondao = new CompetitionDaoImpl(account);
            licensedao = new LicenseRecordDaoImpl(account);
            volunteerdao = new VolunteerDaoImpl(account);
            systemConfigDao = new SystemConfigDaoImpl(account);
            otherdao = new OtherDaoImpl(account);
            systemConfig = systemConfigDao.getSystemConfig();
        } catch (NullPointerException e) {
            e.printStackTrace();
            response.getWriter().write("發生異常錯誤，若有疑問請洽系統管理員");
            return;
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("發生異常錯誤，若有疑問請洽系統管理員");
            return;
        }
        HttpSession session = request.getSession();

        try {
            Account account = (Account) session.getAttribute("account");
            Student student = account.getRoleDetail(Authority.RoleIndex.STUDENT);
            String selectyear = Integer.toString(systemConfig.getPerformance().getSubmitYear());
            List<Cadre> cadreList = cadredao.getCadre(student.getRgno(), null, "1", null, null);
            List<Competition> competitionList = competitiondao.getCompetition(student.getRgno(), null, "1", null, null);
            List<License> licenseList = licensedao.getLicense(student.getRgno(), null, "1", null, null);
            List<Volunteer> volunteerList = volunteerdao.getVolunteer(student.getRgno(), null, "1", null, null);
            List<Other> otherList = otherdao.getOther(student.getRgno(), null, "1", null, null, null);
            Field field = new Field();
            //TODO　勾選數量是哪一個... 確認多元表現"勾選"數量
            request.setAttribute("maxAmount", systemConfig.getPerformance().getMaxSubmitAmount());
            request.setAttribute("field", field);
            request.setAttribute("cadreList", cadreList);
            request.setAttribute("competitionList", competitionList);
            request.setAttribute("licenseList", licenseList);
            request.setAttribute("otherList", otherList);
            request.setAttribute("volunteerList", volunteerList);
            request.setAttribute("config", systemConfig);
            request.setAttribute("DateUtils", new DateUtils());
            request.getRequestDispatcher("WEB-INF/jsp/performancePage/performanceCheck.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
