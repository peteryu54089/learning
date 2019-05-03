package servlet;

import dao.CUnitDao;
import dao.StudentDao;
import dao.TutorDao;
import dao.impl.CUnitDaoImpl;
import dao.impl.StudentDaoImpl;
import dao.impl.TutorDaoImpl;
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

@WebServlet("/tutorCourseResult")
public class TutorCourseResultServlet extends _BaseServlet {
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

        if (!account.getAuthority().containsRole(Authority.RoleIndex.TUTOR, Authority.RoleIndex.MANAGER, Authority.RoleIndex.WORK_TEAM)) {
            response.setStatus(401);
            return;
        }

        TutorDao tutordao = new TutorDaoImpl(account);
        CUnitDao cUnitdao = new CUnitDaoImpl(account);
        //TODO 找學期學年度
        MisSystemData misSystemData = null;
        try {	// 取得學務系統學年期 
			misSystemData = MisSystemUtil.getStuAffairsSbjYS(account.getSchoolSchema());
		} catch (Exception e) {
			e.printStackTrace();
		}
        Semester sem = new Semester(misSystemData.getSbj_year(), misSystemData.getSbj_sem());
        
//        Semester sem = new Semester(105, 2);
        StudentDao studentDao = new StudentDaoImpl(account);
        List<CUnit> CUnitList = null;
        List<Student> StudentList = null;
        Integer page = 1;
        int totalAmount = 0;

        try {
            page = Integer.parseInt(request.getParameter("page"));
        } catch (NullPointerException | NumberFormatException ignored) {
        }

        String clscode = request.getParameter("class");
        String regno = request.getParameter("regno");
        String name = request.getParameter("name");


        try {
            if (account.getAuthority().isTutor()) {
                Tutor tutor = tutordao.getTutorByRegNumber(account.getRegNumber());
                CUnitList = cUnitdao.getCUnit(sem.year, sem.semester, null, tutor.getStaffCode(), null);
                StudentList = studentDao.getStudentList(regno, String.valueOf(sem.year), String.valueOf(sem.semester), clscode, name, null, tutor.getStaffCode(), page);
                totalAmount = studentDao.getTotalAmount(regno, String.valueOf(sem.year), String.valueOf(sem.semester), clscode, name, null, tutor.getStaffCode());
            } else if (account.getAuthority().containsRole(Authority.RoleIndex.MANAGER, Authority.RoleIndex.WORK_TEAM)) {
                CUnitList = cUnitdao.getCUnit(sem.year, sem.semester, null, null, null);
                StudentList = studentDao.getStudentList(
                        regno,
                        String.valueOf(sem.year), String.valueOf(sem.semester),
                        clscode, name, null, null, page
                );

                totalAmount = studentDao.getTotalAmount(
                        regno,
                        String.valueOf(sem.year), String.valueOf(sem.semester),
                        clscode, name, null, null
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        request.setAttribute("page", page);
        request.setAttribute("classList", CUnitList);
        request.setAttribute("list", new ModelList<>(StudentList, totalAmount));
        request.setAttribute("itemsPerPage", StudentDaoImpl.RECORD_PER_PAGE);

        request.setAttribute("q_class", clscode);
        request.setAttribute("q_regno", regno);
        request.setAttribute("q_name", name);
        request.setAttribute("title", "檢閱學生修課成績");

        request.getRequestDispatcher("/WEB-INF/jsp/tutorCourseResult.jsp").forward(request, response);
    }
}
