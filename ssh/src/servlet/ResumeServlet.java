package servlet;

import dao.ResumeDao;
import dao.StudentDao;
import dao.SystemConfigDao;
import dao.impl.ResumeDaoImpl;
import dao.impl.StudentDaoImpl;
import dao.impl.SystemConfigDaoImpl;
import model.Account;
import model.Authority.RoleIndex;
import model.Resume;
import model.Semester;
import model.SystemConfig;
import model.role.Student;
import org.apache.commons.io.FilenameUtils;
import util.AuthCheck;
import util.DateUtils;
import util.FilePartUtils;
import util.Upload;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/resume")
@MultipartConfig
public class ResumeServlet extends _BaseServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        Account account = (Account) request.getSession().getAttribute("account");
        SystemConfigDao systemConfigDao = new SystemConfigDaoImpl(account);
        StudentDao studentdao = new StudentDaoImpl(account);
        ResumeDao resumedao = new ResumeDaoImpl(account);

        SystemConfig systemConfig;
        try {
            systemConfig = systemConfigDao.getSystemConfig();
            if (systemConfig == null)
                throw new NullPointerException("Failed to get system config");
        } catch (NullPointerException e) {
            e.printStackTrace();
            response.getWriter().write("發生異常錯誤，若有疑問請洽系統管理員");
            return;
            // TODO 警告訊息
        }
        if (!AuthCheck.isAuth(request, RoleIndex.STUDENT)) {
//        	response.sendRedirect("login");
        	request.setAttribute("error", "您無權限使用此系統");
        	request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            return;
        }
        try {
            String title = request.getParameter("title");
            Part file = request.getPart("file1");
            // TODO: change to resume format
            if (file.getSize() > systemConfig.getAutoBio().getMaxSize()) {
                PrintWriter out = response.getWriter();
                out.println("<script>alert('file size to large')</script>");
                out.println("<script>window.history.go(-1)</script>");
                return;
                // TODO 警告訊息
            }
            String description = request.getParameter("content");

            Upload upload = new Upload(getServletContext());
//            String fileName = upload.uploadFile(account, file, FilenameUtils.getExtension(file.getSubmittedFileName()));
            String fileName = upload.uploadFile(account, file, FilenameUtils.getExtension(FilePartUtils.getSubmittedFileName(file)));
            Semester sem = DateUtils.getCurrentSemester();
//            resumedao.uploadResume(account.getRegNumber(), title, description, fileName, sem.year, sem.semester, file.getSubmittedFileName());
            resumedao.uploadResume(account.getRegNumber(), title, description, fileName, sem.year, sem.semester, FilePartUtils.getSubmittedFileName(file));
            Integer pageAmount = resumedao.getPageNumber(account.getRegNumber());
            List<Resume> resumeList = resumedao.getResume(account.getRegNumber(), pageAmount);
            request.setAttribute("resumeList", resumeList);
            request.setAttribute("pageAmount", pageAmount);
            request.setAttribute("page", pageAmount);
            response.sendRedirect("resume?page=" + pageAmount);
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

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!AuthCheck.isAuth(request)) {
//        	response.sendRedirect("login");
        	request.setAttribute("error", "您無權限使用此系統");
        	request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            return;
        }
        Account account = (Account) request.getSession().getAttribute("account");
        ResumeDao resumedao = new ResumeDaoImpl(account);
        try {
            HttpSession session = request.getSession();
            Student student = (Student) session.getAttribute("student");

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
        SystemConfigDao systemConfigDao = new SystemConfigDaoImpl(account);
        SystemConfig systemConfig;
        try {
            systemConfig = systemConfigDao.getSystemConfig();
            session.setAttribute("size", systemConfig.getAutoBio().getMaxSize());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        try {
            Integer page = 1;
            if (request.getParameter("page") != null) {
                page = Integer.parseInt(request.getParameter("page"));
            }
            List<Resume> resumeList = resumedao.getResume(account.getRegNumber(), page);
            Integer pageAmount = resumedao.getPageNumber(account.getRegNumber());
            request.setAttribute("page", page);
            request.setAttribute("pageAmount", pageAmount);
            request.setAttribute("resumeList", resumeList);
            request.getRequestDispatcher("WEB-INF/jsp/resume.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
