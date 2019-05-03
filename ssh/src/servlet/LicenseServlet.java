package servlet;

import com.google.gson.Gson;
import config.Config;
import dao.LicenseRecordDao;
import dao.LicensesDao;
import dao.SystemConfigDao;
import dao.UploadFileDao;
import dao.impl.LicenseRecordDaoImpl;
import dao.impl.LicensesDaoImpl;
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

/**
 * Created by David on 2018/1/29.
 */

@WebServlet("/license")
@MultipartConfig
public class LicenseServlet extends _BasePerformanceServlet {


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        if (!AuthCheck.isAuth(request, Authority.RoleIndex.STUDENT)) {
            request.setAttribute("error", "您無權限使用此系統");
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            return;
        }

        Account account = (Account) request.getSession().getAttribute("account");

        LicenseRecordDao licenseRecordDao = new LicenseRecordDaoImpl(account);
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
            String code = request.getParameter("code");
            String note = request.getParameter("note");
            String point = request.getParameter("point");
            String result = request.getParameter("result");
            String time = request.getParameter("time");
            String licensenumber = request.getParameter("licensenumber");
            String group = request.getParameter("group");
            String content = request.getParameter("content");
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
                License license = new License(
                        rgno, content, PerformanceBaseModel.SourceType.STUDENT,
                        code, note, point, result, time,
                        licensenumber, group,
                        docFile != null ? docFile.getId() : null,
                        videoFile != null ? videoFile.getId() : null,
                        externalLink);

                licenseRecordDao.insertLicense(license);
                //回傳最後一頁
                Integer pageAmount = licenseRecordDao.getPageNumber(rgno);
                response.sendRedirect("license?page=" + pageAmount);
            }
            //更新資料
            else {
                License license = licenseRecordDao.getLicenseByID(rgno, Integer.parseInt(modifyid));
                if (!license.student_modifiable()) {
                    response.setContentType("text/html; charset=UTF-8");
                    PrintWriter out = response.getWriter();
                    out.println("<script>alert('無法修改')</script>");
                    out.println("<script>window.history.go(-1)</script>");
                    return;
                }

                license.setCode(code);
                license.setNote(note);
                license.setPoint(point);
                license.setResult(result);
                license.setTime(time);
                license.setContent(content);
                license.setLicensenumber(licensenumber);
                license.setGroup(group);
                license.setExternalLink(externalLink);
                updateOldPerformanceModel(uploadFileDao, license, docFile, videoFile);

                licenseRecordDao.updateLicenseByID(license, rgno, Integer.valueOf(modifyid));
                Integer page = 1;
                if (request.getParameter("page") != null) {
                    page = Integer.parseInt(request.getParameter("page"));
                }
                response.sendRedirect("license?page=" + page);
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!AuthCheck.isAuth(request)) {
            request.setAttribute("error", "您無權限使用此系統");
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            return;
        }

        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");

        LicenseRecordDao licenseRecordDao = new LicenseRecordDaoImpl(account);
        LicensesDao licensesDao = new LicensesDaoImpl(account);
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
            } else {
                rgno = Integer.parseInt(request.getParameter("rgno"));
            }

            Integer page = 1;
            if (request.getParameter("page") != null) {
                page = Integer.parseInt(request.getParameter("page"));
            }
            List<License> licenseList = licenseRecordDao.getLicense(rgno, null, null, null, page);
            List<Licenses> licenses = licensesDao.getAllLicense();
            Integer pageAmount = licenseRecordDao.getPageNumber(rgno);
            request.setAttribute("page", page);
            request.setAttribute("pageAmount", pageAmount);
            request.setAttribute("licenseList", licenseList);
            request.setAttribute("licenses", licenses);
            request.getRequestDispatcher("WEB-INF/jsp/performancePage/license.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
