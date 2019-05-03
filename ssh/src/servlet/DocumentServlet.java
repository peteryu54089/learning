package servlet;

import config.Config;
import dao.DocumentDao;
import dao.StudentDao;
import dao.SystemConfigDao;
import dao.UploadFileDao;
import dao.impl.DocumentDaoImpl;
import dao.impl.StudentDaoImpl;
import dao.impl.SystemConfigDaoImpl;
import dao.impl.UploadFileDaoImpl;
import model.*;
import model.Authority.RoleIndex;
import model.role.Student;
import org.apache.commons.io.FilenameUtils;
import util.AuthCheck;
import util.DateUtils;
import util.FilePartUtils;
import util.StringUtils;
import util.Upload;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/document")
@MultipartConfig
public class DocumentServlet extends _BaseServlet {


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        if (!AuthCheck.isAuth(request, RoleIndex.STUDENT)) {
//        	response.sendRedirect("login");
        	request.setAttribute("error", "您無權限使用此系統");
        	request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            return;
        }
        try {
            HttpSession session = request.getSession();
            Account account = (Account) session.getAttribute("account");
            UploadFileDao uploadDao = new UploadFileDaoImpl(account);
            //TODO 更好的取得RGNO
            Integer rgno = ((Student) account.getRoleDetail(Authority.RoleIndex.STUDENT)).getRgno();
            StudentDao studentdao = new StudentDaoImpl(account);
            DocumentDao documentdao = new DocumentDaoImpl(account);
            SystemConfigDao systemConfigDao = new SystemConfigDaoImpl(account);
            SystemConfig systemConfig = systemConfigDao.getSystemConfig();
            String title = request.getParameter("title");
            Part file = request.getPart("file1");
            Upload upload = new Upload(getServletContext());
            if (file.getSize() > systemConfig.getOtherDocument().getMaxSize()) {
                PrintWriter out = response.getWriter();
                out.println("<script>alert('file size to large')</script>");
                out.println("<script>window.history.go(-1)</script>");
                return;
                // TODO 警告訊息
            }
            if (!systemConfig.getOtherDocument().getAllowTypes().contains(upload.getExtension(FilePartUtils.getSubmittedFileName(file)))) {
                PrintWriter out = response.getWriter();
                out.println("<script>alert('Extension not allow')</script>");
                out.println("<script>window.history.go(-1)</script>");
                return;
                // TODO 警告訊息
            }
            String description = request.getParameter("content");
            UploadFile uploadFile = uploadDao.insert(
                    Config.STUDENT_UPLOADED_FILE_DIR,
                    systemConfig.getSchoolInfo().getId(),
                    DateUtils.getCurrentSemester(),
                    rgno,
                    FilePartUtils.getSubmittedFileName(file),
                    file.getInputStream());
            documentdao.uploadDocument(rgno,title,description,DateUtils.getCurrentSemester().year,DateUtils.getCurrentSemester().semester,uploadFile.getId());

            Integer pageAmount = documentdao.getPageNumber(rgno);
            List<Document> documentList = documentdao.getDocument(rgno, pageAmount);
            for (Document a :documentList)
            {
                a.setLink(Upload.generateDownloadLink(request, account, a.getUploadFile()));
            }
            request.setAttribute("documentList", documentList);
            request.setAttribute("pageAmount", pageAmount);
            request.setAttribute("page", pageAmount);
            //request.getRequestDispatcher("WEB-INF/jsp/document.jsp?page="+pageAmount).forward(request, response);
            response.sendRedirect("document?page=" + pageAmount);
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
        Integer rgno = null;
        Account account = null;
        try {
            HttpSession session = request.getSession();
            account = (Account) session.getAttribute("account");
            //TODO 更好的取得RGNO

            if (account.getAuthority().isStudent()) {
                rgno = ((Student) account.getRoleDetail(Authority.RoleIndex.STUDENT)).getRgno();
            } else if (account.getAuthority().containsRole(RoleIndex.MANAGER, RoleIndex.WORK_TEAM)) {
                rgno = Integer.parseInt(request.getParameter("rgno"));
            }

            if (rgno == null) {
                throw new NullPointerException();
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("發生異常錯誤，若有疑問請洽系統管理員");
            return;
        }

        SystemConfigDao systemConfigDao = new SystemConfigDaoImpl(account);
        SystemConfig systemConfig = systemConfigDao.getSystemConfig();
        try {
            if (account.getAuthority().isStudent()) {
                request.setAttribute("size", systemConfig.getOtherDocument().getMaxSize());
                request.setAttribute("AllowType", systemConfig.getOtherDocument().getAllowTypesString());
            } else {
                request.setAttribute("size", -1);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            request.setAttribute("size", -1);
        }

        try {
            DocumentDao documentdao = new DocumentDaoImpl(account);
            Integer page = 1;
            if (request.getParameter("page") != null) {
                page = Integer.parseInt(request.getParameter("page"));
            }

            List<Document> documentList = documentdao.getDocument(rgno, page);
            for (Document a :documentList)
            {
                a.setLink(Upload.generateDownloadLink(request, account, a.getUploadFile()));
            }
            Integer pageAmount = documentdao.getPageNumber(rgno);

            request.setAttribute("page", page);
            request.setAttribute("pageAmount", pageAmount);
            request.setAttribute("documentList", documentList);
            request.getRequestDispatcher("WEB-INF/jsp/document.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
