package servlet;

import dao.StudentDao;
import dao.StudyPlanDao;
import dao.impl.StudentDaoImpl;
import dao.impl.StudyPlanDaoImpl;
import model.Account;
import model.Authority;
import model.StudyPlan;
import model.role.Student;
import util.AuthCheck;
import util.Upload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/tutorViewStudyPlan")

public class TutorViewStudyplanServlet extends _BaseServlet {


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
        StudyPlanDao studyPlanDao = new StudyPlanDaoImpl(account);
        StudentDao studentDao = new StudentDaoImpl(account);
        if (!account.getAuthority().containsRole(Authority.RoleIndex.TUTOR, Authority.RoleIndex.WORK_TEAM, Authority.RoleIndex.MANAGER)) {
//        	response.sendRedirect("login");
        	request.setAttribute("error", "您無權限使用此系統");
        	request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            return;
        }
        try {

            Integer page = 1;
            if (request.getParameter("page") != null) {
                page = Integer.parseInt(request.getParameter("page"));
            }
            String rgnos = request.getParameter("rgno");
            Integer rgno = Integer.parseInt(rgnos);
            List<StudyPlan> studyPlanList = studyPlanDao.getStudyPlan(rgno, page);
            for (StudyPlan a :studyPlanList)
            {
                a.setMain_link(Upload.generateDownloadLink(request, account, a.getMain_uploadFile()));
                if(a.getSub_file_id()!=null && a.getSub_file_id()!=0)
                    a.setSub_link(Upload.generateDownloadLink(request, account, a.getSub_uploadFile()));
            }
            Integer pageAmount = studyPlanDao.getPageNumber(rgno);
            Student student = studentDao.getStudentByRegNumber(rgnos);
            request.setAttribute("page", page);
            request.setAttribute("student", student);
            request.setAttribute("pageAmount", pageAmount);
            request.setAttribute("studyPlanList", studyPlanList);
            request.getRequestDispatcher("WEB-INF/jsp/tutorViewStudyPlan.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
