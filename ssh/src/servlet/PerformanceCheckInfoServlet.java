package servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dao.*;
import dao.impl.*;
import model.*;
import model.role.Student;
import org.apache.commons.collections4.IterableUtils;
import util.AuthCheck;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/performanceCheckInfo")
public class PerformanceCheckInfoServlet extends _BasePerformanceServlet {

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
            //TODO　分學期
            HttpSession session = request.getSession();
            Account account = (Account) session.getAttribute("account");
            Student student = account.getRoleDetail(Authority.RoleIndex.STUDENT);


            CadreDao cadredao = new CadreDaoImpl(account);
            CompetitionDao competitiondao = new CompetitionDaoImpl(account);
            LicenseRecordDao licensedao = new LicenseRecordDaoImpl(account);
            StudentDao studentdao = new StudentDaoImpl(account);
            VolunteerDao volunteerdao = new VolunteerDaoImpl(account);
            SystemConfigDao systemConfigDao = new SystemConfigDaoImpl(account);
            OtherDao otherdao = new OtherDaoImpl(account);
            SystemConfig systemConfig = systemConfigDao.getSystemConfig();

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
            //
            List<Map<String, String>> performanceList = new ArrayList<>();
            //
            PerformanceUnlockStatusDao unlockDao = new PerformanceUnlockStatusDaoImpl(account);
            boolean isUnlockStatus = unlockDao.isInUnlockStatus(selectyear, student.getRgno());
            IterableUtils
                    .chainedIterable(cadreList, competitionList, licenseList, volunteerList, otherList)
                    .forEach(x -> x.setUnlockStatus(isUnlockStatus));

            for (Cadre cadre : cadreList) {
                Map<String, String> options = new LinkedHashMap<>();
                options.put("typeC", "幹部經歷");
                options.put("type", "1");
                options.put("id", Integer.toString(cadre.getId()));
                options.put("name", cadre.getUnit() + "-" + cadre.getJob());
                options.put("date", cadre.getStartTime() + "-" + cadre.getEndTime());
                options.put("content", cadre.getContent());
                options.put("externalLink", cadre.getExternalLink());
                options.put("status", cadre.getStatusC());
                options.put("unlock", Boolean.toString(cadre.getIsUnlockStatus()));
                setInfoMap(request, account, options, cadre);
                performanceList.add(options);
            }
            for (Competition competition : competitionList) {
                Map<String, String> options = new LinkedHashMap<>();
                options.put("typeC", "競賽成果");
                options.put("type", "2");
                options.put("id", Integer.toString(competition.getId()));
                options.put("name", competition.getName() + "-" + competition.getAward());
                options.put("date", competition.getTime());
                options.put("content", competition.getContent());
                options.put("externalLink", competition.getExternalLink());
                options.put("status", competition.getStatusC());
                options.put("unlock", Boolean.toString(competition.getIsUnlockStatus()));
                setInfoMap(request, account, options, competition);
                performanceList.add(options);
            }
            for (License license : licenseList) {
                Map<String, String> options = new LinkedHashMap<>();
                options.put("typeC", "檢定證照");
                options.put("type", "3");
                options.put("id", Integer.toString(license.getId()));
                options.put("name", license.getCode() + "-" + license.getPoint());
                options.put("date", license.getTime());
                options.put("content", license.getContent());
                options.put("externalLink", license.getExternalLink());
                options.put("status", license.getStatusC());
                options.put("unlock", Boolean.toString(license.getIsUnlockStatus()));
                setInfoMap(request, account, options, license);
                performanceList.add(options);
            }
            for (Volunteer volunteer : volunteerList) {
                Map<String, String> options = new LinkedHashMap<>();
                options.put("typeC", "志工服務");
                options.put("type", "4");
                options.put("id", Integer.toString(volunteer.getId()));
                options.put("name", volunteer.getName() + "-" + volunteer.getPlace() + "-" + volunteer.getCount());
                options.put("date", volunteer.getStartTime() + "-" + volunteer.getEndTime());
                options.put("content", volunteer.getContent());
                options.put("externalLink", volunteer.getExternalLink());
                options.put("status", volunteer.getStatusC());
                options.put("unlock", Boolean.toString(volunteer.getIsUnlockStatus()));
                setInfoMap(request, account, options, volunteer);
                performanceList.add(options);
            }
            for (Other other : otherList) {
                Map<String, String> options = new LinkedHashMap<>();
                options.put("typeC", other.getTypeC());
                options.put("type", "5");
                options.put("id", Integer.toString(other.getId()));
                options.put("name", other.getName());
                options.put("unlock", Boolean.toString(other.getIsUnlockStatus()));
                if (other.getEndTime().isEmpty())
                    options.put("date", other.getStartTime());
                else
                    options.put("date", other.getStartTime() + "-" + other.getEndTime());
                options.put("content", other.getContent());
                options.put("externalLink", other.getExternalLink());
                options.put("status", other.getStatusC());
                setInfoMap(request, account, options, other);
                performanceList.add(options);
            }
            //
            Gson gson = new GsonBuilder().create();
            String json = gson.toJson(performanceList);
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


}

