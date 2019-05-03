package servlet;

import com.google.gson.Gson;
import config.Config;
import dao.OtherDao;
import dao.SystemConfigDao;
import dao.UploadFileDao;
import dao.impl.OtherDaoImpl;
import dao.impl.SystemConfigDaoImpl;
import dao.impl.UploadFileDaoImpl;
import model.*;
import model.role.Student;
import util.AuthCheck;
import util.DateUtils;
import util.Upload;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;


@WebServlet("/other")
@MultipartConfig
public class OtherServlet extends _BasePerformanceServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        if (!AuthCheck.isAuth(request, Authority.RoleIndex.STUDENT)) {
            request.setAttribute("error", "您無權限使用此系統");
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            return;
        }

        Account account = (Account) request.getSession().getAttribute("account");

        OtherDao otherDao = new OtherDaoImpl(account);
        SystemConfigDao systemConfigDao = new SystemConfigDaoImpl(account);
        SystemConfig systemConfig = systemConfigDao.getSystemConfig();

        try {
            request.setAttribute("docSize", systemConfig.getPerformance().getMaxDocSize());
            request.setAttribute("docAllowType", systemConfig.getPerformance().getAllowDocTypesString());
            request.setAttribute("videoSize", systemConfig.getPerformance().getMaxVideoSize());
            request.setAttribute("videoAllowType", systemConfig.getPerformance().getAllowVideoTypesString());
        } catch (NullPointerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            String name = request.getParameter("name");
            String unit = request.getParameter("unit");
            String startTime = request.getParameter("startTime");
            String endTime = request.getParameter("endTime");
            String count = request.getParameter("count");
            String content = request.getParameter("content");
            String type = request.getParameter("type");
            String externalLink = request.getParameter("externalLink");
            String modifyid = request.getParameter("modifyid");
            Part documentFilePart = request.getPart("document");
            Part videoFilePart = request.getPart("video");
            Integer rgno = 0;

            try {
                documentFilePart = checkDocFile(systemConfig.getPerformance(), documentFilePart);
                videoFilePart = checkVideoFile(systemConfig.getPerformance(), videoFilePart);
            } catch (FileCheckException ex) {
                PrintWriter out = response.getWriter();
                out.println("<script>alert(" + new Gson().toJson(ex.getMessage()) + ")</script>");
                out.println("<script>window.history.go(-1)</script>");
                return;
            }

            {
                Student stu = account.getRoleDetail(Authority.RoleIndex.STUDENT);
                if (stu != null) {
                    rgno = stu.getRgno();
                } else {
                    rgno = Integer.valueOf(request.getParameter("rgno"));
                }
            }

            Semester sem = DateUtils.getCurrentSemester();
            UploadFileDao uploadFileDao = new UploadFileDaoImpl(account);
            UploadFile docFile = null;
            UploadFile videoFile = null;

            if (documentFilePart != null) {
                docFile = uploadFileDao.insert(
                        Config.STUDENT_UPLOADED_FILE_DIR, systemConfig.getSchoolInfo().getId(), sem,
                        rgno,
                        Upload.getFileName(documentFilePart), documentFilePart.getInputStream());
            }

            if (videoFilePart != null) {
                videoFile = uploadFileDao.insert(
                        Config.STUDENT_UPLOADED_FILE_DIR, systemConfig.getSchoolInfo().getId(), sem,
                        rgno,
                        Upload.getFileName(videoFilePart), videoFilePart.getInputStream());
            }

            //新增一筆資料
            if (modifyid.equals("0")) {
                Other other = new Other(
                        rgno,
                        content, Cadre.SourceType.STUDENT,
                        name, unit, startTime, endTime, count, type,
                        docFile != null ? docFile.getId() : null,
                        videoFile != null ? videoFile.getId() : null,
                        externalLink);
                otherDao.insertOther(other);
                //回傳最後一頁
                Integer pageAmount = otherDao.getPageNumber(rgno);
                response.sendRedirect("other?page=" + pageAmount);
                return;
            }
            //更新資料
            else {
                Other other = otherDao.getOtherByID(rgno, Integer.parseInt(modifyid));
                if (!other.student_modifiable()) {
                    response.setContentType("text/html; charset=UTF-8");
                    PrintWriter out = response.getWriter();
                    out.println("<script>alert('無法修改')</script>");
                    out.println("<script>window.history.go(-1)</script>");
                    return;
                }

                other.setName(name);
                other.setUnit(unit);
                other.setStartTime(startTime);
                other.setEndTime(endTime);
                other.setCount(count);
                other.setContent(content);
                other.setExternalLink(externalLink);

                updateOldPerformanceModel(uploadFileDao, other, docFile, videoFile);
                otherDao.updateOtherByID(other, rgno, Integer.valueOf(modifyid));
                Integer page = 1;
                if (request.getParameter("page") != null) {
                    page = Integer.parseInt(request.getParameter("page"));
                }
                response.sendRedirect("other?page=" + page);
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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!AuthCheck.isAuth(request)) {
            request.setAttribute("error", "您無權限使用此系統");
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            return;
        }

        HttpSession session = request.getSession();
        Account account = (Account)session.getAttribute("account");

        OtherDao otherDao = new OtherDaoImpl(account);
        SystemConfigDao systemConfigDao = new SystemConfigDaoImpl(account);
        SystemConfig systemConfig = systemConfigDao.getSystemConfig();

        try {
            request.setAttribute("docSize", systemConfig.getPerformance().getMaxDocSize());
            request.setAttribute("docAllowType", systemConfig.getPerformance().getAllowDocTypesString());
            request.setAttribute("videoSize", systemConfig.getPerformance().getMaxVideoSize());
            request.setAttribute("videoAllowType", systemConfig.getPerformance().getAllowVideoTypesString());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        try {
            Integer rgno = 0;

            if (account.getAuthority().isStudent()) {
                rgno = account.<Student>getRoleDetail(Authority.RoleIndex.STUDENT).getRgno();
            } else if (account.getAuthority().containsRole(Authority.RoleIndex.MANAGER, Authority.RoleIndex.WORK_TEAM)) {
                rgno = Integer.valueOf(request.getParameter("rgno"));
            }

            String type = request.getParameter("type");
            Integer page = 1;
            if (request.getParameter("page") != null) {
                page = Integer.parseInt(request.getParameter("page"));
            }
            List<Other> otherList = otherDao.getOther(rgno, null, null, null, page, type);
            Integer pageAmount = otherDao.getPageNumber(rgno);
            Field field = new Field();
            request.setAttribute("page", page);
            request.setAttribute("field", field);
            request.setAttribute("pageAmount", pageAmount);
            request.setAttribute("otherList", otherList);
            request.getRequestDispatcher("WEB-INF/jsp/performancePage/other.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
