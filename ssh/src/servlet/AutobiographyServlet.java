package servlet;

import config.Config;
import dao.AutobiographyDao;
import dao.StudentDao;
import dao.SystemConfigDao;
import dao.UploadFileDao;
import dao.impl.AutobiographyDaoImpl;
import dao.impl.StudentDaoImpl;
import dao.impl.SystemConfigDaoImpl;
import dao.impl.UploadFileDaoImpl;
import model.*;
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

@WebServlet("/autobiography")
@MultipartConfig
public class AutobiographyServlet extends _BaseServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        if (!AuthCheck.isAuth(request, Authority.RoleIndex.STUDENT)) {
//            response.sendRedirect("login");
            request.setAttribute("error", "您無權限使用此系統");
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            return;
        }
        try {
            HttpSession session = request.getSession();
            Account account = (Account) session.getAttribute("account");
            UploadFileDao uploadDao = new UploadFileDaoImpl(account);
            SystemConfigDao systemConfigDao = new SystemConfigDaoImpl(account);
            SystemConfig systemConfig = systemConfigDao.getSystemConfig();
            StudentDao studentdao = new StudentDaoImpl(account);
            AutobiographyDao autobiographydao = new AutobiographyDaoImpl(account);

            String title = request.getParameter("title");
            String modifyid = request.getParameter("modifyid");
            String content = request.getParameter("content");
            Part file1 = request.getPart("file1");
            Part file2 = request.getPart("file2");

            Upload upload = new Upload(getServletContext());
            UploadFile uploadFile1 = null;
            UploadFile uploadFile2 = null;
            //資料一處理
            String file1Originalname = null;
            //TODO 更好的取得RGNO
            Integer rgno = ((Student) account.getRoleDetail(Authority.RoleIndex.STUDENT)).getRgno();
            if (file1 != null && file1.getSize() != 0) {
                if (file1.getSize() > systemConfig.getAutoBio().getMaxSize()) {
                    PrintWriter out = response.getWriter();
                    out.println("<script>alert('file size to large')</script>");
                    out.println("<script>window.history.go(-1)</script>");
                    return;
                    // TODO 警告訊息
                }
                
//                if (!systemConfig.getAutoBio().getAllowTypes().contains(upload.getExtension(file1.getSubmittedFileName()))) {
                if (!systemConfig.getAutoBio().getAllowTypes().contains(upload.getExtension(FilePartUtils.getSubmittedFileName(file1)))) {               
                    PrintWriter out = response.getWriter();
                    out.println("<script>alert('Extension not allow')</script>");
                    out.println("<script>window.history.go(-1)</script>");
                    return;
                    // TODO 警告訊息
                }
                
//                file1Name = upload.uploadFile(account, file1, FilenameUtils.getExtension(file1.getSubmittedFileName()));
//                file1Originalname = file1.getSubmittedFileName();
                file1Originalname = FilePartUtils.getSubmittedFileName(file1);

                uploadFile1 = uploadDao.insert(
                        Config.STUDENT_UPLOADED_FILE_DIR,
                        systemConfig.getSchoolInfo().getId(),
                        DateUtils.getCurrentSemester(),
                        rgno,
                        file1Originalname,
                        file1.getInputStream());
            }
            //資料二處理
            //String file2Name = null;
            String file2Originalname = null;
            if (file2 != null && file2.getSize() != 0) {
                if (file2.getSize() > systemConfig.getAutoBio().getMaxSize()) {
                    PrintWriter out = response.getWriter();
                    out.println("<script>alert('file size to large')</script>");
                    out.println("<script>window.history.go(-1)</script>");
                    return;
                    // TODO 警告訊息
                }
                
//                if (!systemConfig.getAutoBio().getAllowTypes().contains(upload.getExtension(file2.getSubmittedFileName()))) {
                if (!systemConfig.getAutoBio().getAllowTypes().contains(upload.getExtension(FilePartUtils.getSubmittedFileName(file2)))) {
                    PrintWriter out = response.getWriter();
                    out.println("<script>alert('Extension not allow')</script>");
                    out.println("<script>window.history.go(-1)</script>");
                    return;
                    // TODO 警告訊息
                }
//              file2Name = upload.uploadFile(account, file2, FilenameUtils.getExtension(file2.getSubmittedFileName()));
//              file2Originalname = file2.getSubmittedFileName();
             // file2Name = upload.uploadFile(account, file2, FilenameUtils.getExtension(FilePartUtils.getSubmittedFileName(file2)));
              file2Originalname = FilePartUtils.getSubmittedFileName(file2);
                uploadFile2 = uploadDao.insert(
                        Config.STUDENT_UPLOADED_FILE_DIR,
                        systemConfig.getSchoolInfo().getId(),
                        DateUtils.getCurrentSemester(),
                        rgno,
                        file2Originalname,
                        file2.getInputStream());
            }
            Integer old_main_file_id =null;
            Integer old_sub_file_id =null;
            //新增一筆資料
            if (modifyid.equals("0")) {
                Integer file2ID = null;
                if (uploadFile2!=null)
                    file2ID = uploadFile2.getId();
                autobiographydao.uploadAutobiography(rgno, title, content, uploadFile1.getId(), file2ID, DateUtils.getCurrentSemester().year, DateUtils.getCurrentSemester().semester);
                //回傳最後一頁
                Integer pageAmount = autobiographydao.getPageNumber(rgno);
                List<Autobiography> autobiographyList = autobiographydao.getAutobiography(rgno, pageAmount);
                request.setAttribute("page", pageAmount);
                request.setAttribute("pageAmount", pageAmount);
                request.setAttribute("autobiographyList", autobiographyList);
                response.sendRedirect("autobiography?page=" + pageAmount);
            }
            //更新資料
            else {
                Autobiography autobiograph = autobiographydao.getAutobiographyByID(rgno, modifyid);
                autobiograph.setTopic(title);
                autobiograph.setDescription(content);
                if (uploadFile1 != null) {
                    //刪除
                    if(autobiograph.getMain_file_id()!=null && autobiograph.getMain_file_id()!=0)
                        old_main_file_id = autobiograph.getMain_file_id();
                    //刪除
                    autobiograph.setMain_file_id(uploadFile1.getId());

                }

                if (request.getParameter("delsub").equals("1")) {
                    //刪除

                    if(autobiograph.getSub_file_id()!=null && autobiograph.getSub_file_id()!=0)
                        old_sub_file_id = autobiograph.getSub_file_id();
                    //刪除
                    autobiograph.setSub_file_id(null);
                }
                if (uploadFile2 != null) {
                    autobiograph.setSub_file_id(uploadFile2.getId());
                }
                autobiographydao.updateAutobiographyByID(rgno, modifyid, autobiograph);
                UploadFileDao uploadFileDao = new UploadFileDaoImpl(account);
                if(old_main_file_id!=null)
                    uploadFileDao.deleteById(old_main_file_id);
                if(old_sub_file_id!=null)
                    uploadFileDao.deleteById(old_sub_file_id);
                Integer pageAmount = autobiographydao.getPageNumber(rgno);
                List<Autobiography> autobiographyList = autobiographydao.getAutobiography(rgno, pageAmount);
                request.setAttribute("autobiographyList", autobiographyList);
                request.setAttribute("pageAmount", pageAmount);
                request.setAttribute("page", request.getParameter("page"));
                response.sendRedirect("autobiography?page=" + request.getParameter("page"));

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

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if (!AuthCheck.isAuth(request)) {
//            response.sendRedirect("login");
            request.setAttribute("error", "您無權限使用此系統");
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            return;
        }
        try {
            HttpSession session = request.getSession();
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
        Account account = (Account) session.getAttribute("account");
        //TODO 更好的取得RGNO
        Integer rgno = ((Student) account.getRoleDetail(Authority.RoleIndex.STUDENT)).getRgno();
        SystemConfigDao systemConfigDao = new SystemConfigDaoImpl(account);
        SystemConfig systemConfig = systemConfigDao.getSystemConfig();
        try {
            session.setAttribute("size", systemConfig.getAutoBio().getMaxSize());
            session.setAttribute("allowType", systemConfig.getAutoBio().getAllowTypesString());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        try {
            AutobiographyDao autobiographydao = new AutobiographyDaoImpl(account);

            Integer page = 1;
            if (request.getParameter("page") != null) {
                page = Integer.parseInt(request.getParameter("page"));
            }
            List<Autobiography> autobiographyList = autobiographydao.getAutobiography(rgno, page);
            for (Autobiography a :autobiographyList)
            {
                a.setMain_link(Upload.generateDownloadLink(request, account, a.getMain_uploadFile()));
                if(a.getSub_file_id()!=null && a.getSub_file_id()!=0)
                    a.setSub_link(Upload.generateDownloadLink(request, account, a.getSub_uploadFile()));
            }
            Integer pageAmount = autobiographydao.getPageNumber(rgno);
            request.setAttribute("page", page);
            request.setAttribute("pageAmount", pageAmount);
            request.setAttribute("autobiographyList", autobiographyList);
            request.getRequestDispatcher("WEB-INF/jsp/autobiography.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
