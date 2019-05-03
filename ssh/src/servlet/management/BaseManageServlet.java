package servlet.management;

import dao.CUnitDao;
import dao.StudentDao;
import dao.impl.CUnitDaoImpl;
import dao.impl.StudentDaoImpl;
import model.Account;
import model.Authority;
import model.CUnit;
import model.MisSystemData;
import model.ModelList;
import model.role.Student;
import servlet._BaseServlet;
import util.MisSystemUtil;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

abstract class BaseManageServlet extends _BaseServlet {
    protected Account checkPermission(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");
        if (account == null || !account.getAuthority().checkRole(Authority.RoleIndex.MANAGER)) {
            response.setStatus(401);
            return null;
        }

        return account;
    }

    protected void bindStuClsInfo(HttpServletRequest request, HttpServletResponse response) {
        Account account = (Account)request.getSession().getAttribute("account");
        CUnitDao cUnitdao = new CUnitDaoImpl(account);
        StudentDao studentDao = new StudentDaoImpl(account);

        int page = 1;
        String clscode = request.getParameter("class");
        String regno = request.getParameter("regno");
        String name = request.getParameter("name");


        List<CUnit> cUnitList = new ArrayList<>();
        ModelList<Student> studentList = null;
        int totalAmount = 0;

        try {
            page = Integer.parseInt(request.getParameter("page"));
        } catch (NullPointerException | NumberFormatException ignored) {
        }

////        Semester semester = DateUtils.getCurrentSemester();
//        //TODO: 使用目前學期
////        final int year = semester.year;
////        final int sem = semester.semester;
//        final int year = 105;
//        final int sem = 2;
        
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

        try {
            cUnitList = cUnitdao.getCUnit(year, sem, null, null, null);
            List<Student> stuList = studentDao.getStudentList(
                    regno,
                    String.valueOf(year), String.valueOf(sem),
                    clscode, name, null, null, page
            );

            totalAmount = studentDao.getTotalAmount(
                    regno,
                    String.valueOf(year), String.valueOf(sem),
                    clscode, name, null, null
            );

            studentList = new ModelList<>(stuList, totalAmount);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        request.setAttribute("page", page);
        request.setAttribute("classList", cUnitList);
        request.setAttribute("list", studentList);
        request.setAttribute("itemsPerPage", StudentDaoImpl.RECORD_PER_PAGE);

        request.setAttribute("q_class", clscode);
        request.setAttribute("q_regno", regno);
        request.setAttribute("q_name", name);

    }
}
