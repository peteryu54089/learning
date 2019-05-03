package servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dao.AttendDao;
import dao.StudentDao;
import dao.SystemConfigDao;
import dao.impl.AttendDaoImpl;
import dao.impl.StudentDaoImpl;
import dao.impl.SystemConfigDaoImpl;
import model.Account;
import model.Attend;
import model.SystemConfig;
import model.role.Student;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

@WebServlet("/calendar")
public class CalendarServlet extends _BaseServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");
        if (!account.getAuthority().isStudent()) {
//        	response.sendRedirect("login");
        	request.setAttribute("error", "您無權限使用此系統");
        	request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            return;
        }
        request.getRequestDispatcher("/WEB-INF/jsp/calendar.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");
        StudentDao studentDao = new StudentDaoImpl(account);
        AttendDao attendDao = new AttendDaoImpl(account);
        SystemConfigDao systemConfigDao = new SystemConfigDaoImpl(account);
        SystemConfig systemConfig = systemConfigDao.getSystemConfig();

        Student student = null;
        try {
            student = studentDao.getStudentByRegNumber(account.getRegNumber());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //多元表現
        Date a1 = systemConfig.getPerformance().getStudentStartDateTime();
        Date a2 = systemConfig.getPerformance().getStudentEndDateTime();

        //課程學習紀錄截止日
        Date b1 = systemConfig.getCourseStudyRecord().getStudentStartDateTime();
        Date b2 = systemConfig.getCourseStudyRecord().getStudentEndDateTime();
        //
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<Map<String, String>> mapList = new ArrayList<>();
        Map<String, String> map;
        //
        map = new HashMap<>();
        map.put("order", "1");
        map.put("title", "多元表現提交");
        map.put("start", sdf.format(a1));
        map.put("end", sdf.format(a2));
        mapList.add(map);
        //
        map = new HashMap<>();
        map.put("order", "1");
        map.put("title", "課程學習紀錄提交");
        map.put("start", sdf.format(b1));
        map.put("end", sdf.format(b2));
        mapList.add(map);
        try {
            List<Attend> attendList = new ArrayList<>();
            attendList = attendDao.getCalenderByDate(student.getRgno(), request.getParameter("start"), request.getParameter("end"));
            for (Attend attend : attendList) {
                map = new HashMap<>();
                map.put("order", Integer.toString(attend.getPeriod()));
                map.put("title", attend.getAr_Cname() + "(" + attend.getSprd_name() + ")");
                map.put("start", attend.getDateADDateString());
                map.put("end", attend.getDateADDateString());
                mapList.add(map);
            }
            Gson gson = new GsonBuilder().create();
            String json = gson.toJson(mapList);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
