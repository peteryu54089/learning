package servlet;

import dao.CUnitDao;
import dao.StudentDao;
import dao.impl.CUnitDaoImpl;
import dao.impl.StudentDaoImpl;
import model.*;
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

@WebServlet("/tutorStuBasicInfo")
public class TutorStuBasicInfoServlet extends _BaseServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!AuthCheck.isAuth(request)) {
//        	response.sendRedirect("login");
        	request.setAttribute("error", "您無權限使用此系統");
        	request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            return;
        }
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");
        CUnitDao cUnitdao = new CUnitDaoImpl(account);
        //TODO 找學期學年度
        MisSystemData misSystemData = null;
        try {	// 取得學務系統學年期 
			misSystemData = MisSystemUtil.getStuAffairsSbjYS(account.getSchoolSchema());
		} catch (Exception e) {
			e.printStackTrace();
		}
        Semester semester = new Semester(misSystemData.getSbj_year(), misSystemData.getSbj_sem());
//        Semester semester = new Semester(105, 2);
        StudentDao studentDao = new StudentDaoImpl(account);
        List<CUnit> CUnitList = null;
        List<Student> StudentList = null;
        Integer page = 1;
        Integer totalAmount = 1;
        String clscode = request.getParameter("class");
        String regno = request.getParameter("regno");
        String name = request.getParameter("name");

        try {
            page = Integer.parseInt(request.getParameter("page"));
        } catch (NullPointerException | NumberFormatException ignored) {
        }
        try {
            Tutor tutor = account.getRoleDetail(Authority.RoleIndex.TUTOR);
            CUnitList = cUnitdao.getCUnit(semester.year, semester.semester, null, tutor.getStaffCode(), null);
            StudentList = studentDao.getStudentList(regno,
                    String.valueOf(semester.year),
                    String.valueOf(semester.semester),
                    clscode, name, null, tutor.getStaffCode(), page);

            totalAmount = studentDao.getTotalAmount(regno,
                    String.valueOf(semester.year),
                    String.valueOf(semester.semester),
                    clscode, name, null, tutor.getStaffCode());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        request.setAttribute("page", page);
        request.setAttribute("clscode", clscode);
        request.setAttribute("regno", regno);
        request.setAttribute("name", name);
        request.setAttribute("list", new ModelList<>(StudentList, totalAmount));
        request.setAttribute("itemsPerPage", StudentDaoImpl.RECORD_PER_PAGE);
        request.setAttribute("classList", CUnitList);

        request.setAttribute("q_class", clscode);
        request.setAttribute("q_regno", regno);
        request.setAttribute("q_name", name);
        request.getRequestDispatcher("/WEB-INF/jsp/tutorViewStudentBasicInfo.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");
        if (!account.getAuthority().isTutor()) {
            response.setStatus(401);
            return;
        }

        StudentDao studentDao = new StudentDaoImpl(account);
        try {
            request.setAttribute("student", studentDao.getStudentByRegNumber(
                    request.getParameter("year"),
                    request.getParameter("sem"),
                    request.getParameter("regno")
            ));
            request.getRequestDispatcher("/WEB-INF/jsp/managerPage/components/stuBasicInfoModal.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(500);
        }
    }
}
