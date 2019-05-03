package servlet;

//import com.mysql.cj.x.protobuf.MysqlxDatatypes;

import dao.*;
import dao.impl.*;
import model.*;
import model.role.Student;
import util.AuthCheck;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by David on 2018/1/29.
 */

@WebServlet("/performanceUnlock")
@MultipartConfig
public class PerformanceUnlockServlet extends _BaseServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        if (!AuthCheck.isAuth(request, Authority.RoleIndex.STUDENT)) {
//        	response.sendRedirect("login");
        	request.setAttribute("error", "您無權限使用此系統");
        	request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            return;
        }
        try {
            HttpSession session = request.getSession();
            Account account = (Account) session.getAttribute("account");
            Student student = account.getRoleDetail(Authority.RoleIndex.STUDENT);
            Integer rgno = student.getRgno();
            Integer id = Integer.valueOf(request.getParameter("id"));
            String type = request.getParameter("type");
            PerformanceUnlockStatusDao unlockStatusDao = new PerformanceUnlockStatusDaoImpl(account);

            CadreDao cadredao = new CadreDaoImpl(account);
            CompetitionDao competitiondao = new CompetitionDaoImpl(account);
            LicenseRecordDao licensedao = new LicenseRecordDaoImpl(account);
            VolunteerDao volunteerdao = new VolunteerDaoImpl(account);
            OtherDao otherdao = new OtherDaoImpl(account);

            if (type.equals("1")) {
                Cadre cadre = cadredao.getCadreByID(rgno, id);
                cadre.setUnlockStatus(unlockStatusDao.isInUnlockStatus(cadre.getSelectedYear(), rgno));

                if (!cadre.getIsUnlockStatus())
                    return;
                cadre.setCheck(0);
                cadre.setSelectedYear(null);
                cadre.setStatus("1");
                cadredao.updateCadreByID(cadre, rgno, id);
            }
            if (type.equals("2")) {
                Competition competition = competitiondao.getCompetitionByID(rgno, id);
                competition.setUnlockStatus(unlockStatusDao.isInUnlockStatus(competition.getSelectedYear(), rgno));

                if (!competition.getIsUnlockStatus())
                    return;
                competition.setCheck(0);
                competition.setSelectedYear(null);
                competition.setStatus("1");
                competitiondao.updateCompetitionByID(competition, rgno, id);
            }
            if (type.equals("3")) {
                License license = licensedao.getLicenseByID(rgno, id);
                license.setUnlockStatus(unlockStatusDao.isInUnlockStatus(license.getSelectedYear(), rgno));

                if (!license.getIsUnlockStatus())
                    return;
                license.setCheck(0);
                license.setSelectedYear(null);
                license.setStatus("1");
                licensedao.updateLicenseByID(license, rgno, id);
            }
            if (type.equals("4")) {
                Volunteer volunteer = volunteerdao.getVolunteerByID(rgno, id);
                volunteer.setUnlockStatus(unlockStatusDao.isInUnlockStatus(volunteer.getSelectedYear(), rgno));

                if (!volunteer.getIsUnlockStatus())
                    return;
                volunteer.setCheck(0);
                volunteer.setSelectedYear(null);
                volunteer.setStatus("1");
                volunteerdao.updateVolunteerByID(volunteer, rgno, id);
            }
            if (type.equals("5")) {
                Other other = otherdao.getOtherByID(rgno, id);
                other.setUnlockStatus(unlockStatusDao.isInUnlockStatus(other.getSelectedYear(), rgno));

                if (!other.getIsUnlockStatus())
                    return;
                other.setCheck(0);
                other.setSelectedYear(null);
                other.setStatus("1");
                otherdao.updateOtherByID(other, rgno, id);
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
}
