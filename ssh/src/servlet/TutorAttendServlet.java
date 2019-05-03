package servlet;

import dao.CUnitDao;
import dao.StudentDao;
import dao.TutorDao;
import dao.impl.CUnitDaoImpl;
import dao.impl.StudentDaoImpl;
import dao.impl.TutorDaoImpl;
import model.Account;
import model.CUnit;
import model.MisSystemData;
import model.role.Student;
import model.role.Tutor;
import util.AuthCheck;
import util.MisSystemUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/tutorAttend")
public class TutorAttendServlet extends _BaseServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!AuthCheck.isAuth(request)) {
//        	response.sendRedirect("login");
        	request.setAttribute("error", "您無權限使用此系統");
        	request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            return;
        }
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");
        TutorDao tutordao = new TutorDaoImpl(account);
        CUnitDao cUnitdao = new CUnitDaoImpl(account);
        //TODO 找學期學年度
        int year = 0;
        int sem = 0;
        MisSystemData misSystemData = null;
        try {	// 取得學務系統學年期 
        	misSystemData = MisSystemUtil.getStuAffairsSbjYS(account.getSchoolSchema());
		    year = misSystemData.getSbj_year();
		    sem = misSystemData.getSbj_sem();
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        
        StudentDao studentDao = new StudentDaoImpl(account);
        Tutor tutor;
        List<CUnit> CUnitList = null;
        List<Student> StudentList = null;
        Integer page = 1;
        Integer pageAmount = 1;
        String clscode = request.getParameter("class");
        String regno = request.getParameter("regno");
        String name = request.getParameter("name");
        if (request.getParameter("page") != null) {
            page = Integer.parseInt(request.getParameter("page"));
        }
        try {
            tutor = tutordao.getTutorByRegNumber(account.getRegNumber());
//            CUnitList = cUnitdao.getCUnit(105, 2, null, tutor.getStaffCode(), null);
//            StudentList = studentDao.getStudentList(regno, "105", "2", clscode, name, null, tutor.getStaffCode(), page);
//            pageAmount = studentDao.getPageNumber(regno, "105", "2", clscode, name, null, tutor.getStaffCode(), page);
            CUnitList = cUnitdao.getCUnit(year, sem, null, tutor.getStaffCode(), null);
            StudentList = studentDao.getStudentList(regno, Integer.toString(year), Integer.toString(sem), clscode, name, null, tutor.getStaffCode(), page);
            pageAmount = studentDao.getPageNumber(regno, Integer.toString(year), Integer.toString(sem), clscode, name, null, tutor.getStaffCode(), page);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        request.setAttribute("page", page);
        request.setAttribute("clscode", clscode);
        request.setAttribute("regno", regno);
        request.setAttribute("name", name);
        request.setAttribute("pageAmount", pageAmount);
        request.setAttribute("StudentList", StudentList);
        request.setAttribute("CUnitList", CUnitList);
        request.getRequestDispatcher("/WEB-INF/jsp/tutorAttend.jsp").forward(request, response);
    }
}
