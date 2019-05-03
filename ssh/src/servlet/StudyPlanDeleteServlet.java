package servlet;

import dao.StudyPlanDao;
import dao.UploadFileDao;
import dao.impl.StudyPlanDaoImpl;
import dao.impl.UploadFileDaoImpl;
import model.Account;
import model.Authority;
import model.Authority.RoleIndex;
import model.StudyPlan;
import model.role.Student;
import util.AuthCheck;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/studyplandelete")
@MultipartConfig
public class StudyPlanDeleteServlet extends _BaseServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        if (!AuthCheck.isAuth(request, RoleIndex.STUDENT)) {
            response.sendRedirect("login");
            return;
        }
        try {
            HttpSession session = request.getSession();
            Account account = (Account) session.getAttribute("account");
            //TODO 更好的取得RGNO
            Integer rgno = ((Student) account.getRoleDetail(Authority.RoleIndex.STUDENT)).getRgno();
            StudyPlanDao studyplandao = new StudyPlanDaoImpl(account);
            String id = request.getParameter("id");
            //刪除
            UploadFileDao uploadFileDao = new UploadFileDaoImpl(account);
            StudyPlan studyPlan = studyplandao.getStudyPlanByID(rgno,id);
            if(studyPlan != null)
            {
                if(studyPlan.getMain_file_id()!=null && studyPlan.getMain_file_id()!=0)
                    uploadFileDao.deleteById(studyPlan.getMain_file_id());
                if(studyPlan.getSub_file_id()!=null && studyPlan.getSub_file_id()!=0)
                    uploadFileDao.deleteById(studyPlan.getSub_file_id());
            }

            //刪除
            studyplandao.deleteStudyPlan(rgno, id);
            Integer after = studyplandao.getPageNumber(rgno);
            PrintWriter out = response.getWriter();
            if (after < Integer.parseInt(request.getParameter("page"))) {
                out.println(after);
            } else {
                String page = "1";
                if (request.getParameter("page") != null)
                    page = request.getParameter("page");
                out.println();
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
