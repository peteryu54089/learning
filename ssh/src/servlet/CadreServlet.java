package servlet;

import com.google.gson.Gson;
import config.Config;
import dao.*;
import dao.impl.*;
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

@WebServlet("/cadre")
@MultipartConfig
public class CadreServlet extends _BasePerformanceServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");

        CadreDao cadredao = new CadreDaoImpl(account);
        StudentDao studentdao = new StudentDaoImpl(account);
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
            String unit = request.getParameter("unit");
            String startTime = request.getParameter("startTime");
            String endTime = request.getParameter("endTime");
            String term = request.getParameter("term");
            String jobLevel = request.getParameter("jobLevel");
            String job = request.getParameter("job");
            String externalLink = request.getParameter("externalLink");
            String content = request.getParameter("content");
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
                if (account.getAuthority().isStudent()) {
                    Cadre cadre = new Cadre(
                            rgno,
                            content, Cadre.SourceType.STUDENT,
                            unit, startTime, endTime, term, job, jobLevel,
                            docFile != null ? docFile.getId() : null,
                            videoFile != null ? videoFile.getId() : null,
                            externalLink);
                    cadredao.insertCadre(cadre);
                    Integer pageAmount = cadredao.getPageNumber(rgno);
                    List<Cadre> cadreList = cadredao.getCadre(rgno, null, null, null, pageAmount);
                    request.setAttribute("page", pageAmount);
                    request.setAttribute("pageAmount", pageAmount);
                    request.setAttribute("cadreList", cadreList);
                    response.sendRedirect("cadre?page=" + pageAmount);
                    return;
                }
                if (account.getAuthority().isCadreSubmitter()) {
                    Cadre cadre = new Cadre(
                            rgno,
                            content, Cadre.SourceType.CADRE_SUBMITTER,
                            unit, startTime, endTime, term, job, jobLevel,
                            docFile != null ? docFile.getId() : null,
                            videoFile != null ? videoFile.getId() : null,
                            externalLink);
                    cadredao.insertCadre(cadre);
                    response.sendRedirect("cadreRecordViewCadreRecord?rgno=" + rgno);
                    return;
                }
            }
            //更新資料
            else {
                Cadre cadre = cadredao.getCadreByID(rgno, Integer.valueOf(modifyid));
                if (account.getAuthority().isStudent()) {
                    if (!cadre.student_modifiable()) {
                        response.getWriter().write("無法修改");
                        return;
                    }
                }
                if (account.getAuthority().isCadreSubmitter()) {
                    if (!cadre.cadre_submitter_modifiable()) {
                        response.getWriter().write("無法修改");
                        return;
                    }
                }
                cadre.setUnit(unit);
                cadre.setStartTime(startTime);
                cadre.setEndTime(endTime);
                cadre.setTerm(term);
                cadre.setLevel(jobLevel);
                cadre.setJob(job);
                cadre.setExternalLink(externalLink);
                cadre.setContent(content);
                updateOldPerformanceModel(uploadFileDao, cadre, docFile, videoFile);

                cadredao.updateCadreByID(cadre, rgno, Integer.valueOf(modifyid));

                Integer page = 1;
                if (request.getParameter("page") != null) {
                    page = Integer.parseInt(request.getParameter("page"));
                }

                if (account.getAuthority().isStudent()) {
                    List<Cadre> cadreList = cadredao.getCadre(rgno, null, null, null, page);
                    Integer pageAmount = cadredao.getPageNumber(rgno);
                    request.setAttribute("page", page);
                    request.setAttribute("pageAmount", pageAmount);
                    request.setAttribute("cadreList", cadreList);
                    response.sendRedirect("cadre?page=" + request.getParameter("page"));
                    return;
                } else if (account.getAuthority().isCadreSubmitter()) {
                    List<Cadre> cadreList = cadredao.getCadre(rgno, null, null, null, page);
                    Integer pageAmount = cadredao.getPageNumber(rgno);
                    request.setAttribute("page", page);
                    request.setAttribute("pageAmount", pageAmount);
                    request.setAttribute("cadreList", cadreList);
                    response.sendRedirect("cadreRecordViewCadreRecord?rgno=" + rgno + "&page=" + page);
                    return;
                }
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
//        	response.sendRedirect("login");
            request.setAttribute("error", "您無權限使用此系統");
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            return;
        }
        CadreDao cadredao;
        SystemConfigDao systemConfigDao;
        SystemConfig systemConfig;

        try {
            HttpSession session = request.getSession();
            Account account = (Account) session.getAttribute("account");

            cadredao = new CadreDaoImpl(account);
            systemConfigDao = new SystemConfigDaoImpl(account);
            systemConfig = systemConfigDao.getSystemConfig();
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
        try {
            request.setAttribute("docSize", systemConfig.getPerformance().getMaxDocSize());
            request.setAttribute("docAllowType", systemConfig.getPerformance().getAllowDocTypesString());
            request.setAttribute("videoSize", systemConfig.getPerformance().getMaxVideoSize());
            request.setAttribute("videoAllowType", systemConfig.getPerformance().getAllowVideoTypesString());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        try {
            Account account = (Account) session.getAttribute("account");
            Integer rgno = 0;

            if (account.getAuthority().isStudent()) {
                rgno = account.<Student>getRoleDetail(Authority.RoleIndex.STUDENT).getRgno();
            } else if (account.getAuthority().containsRole(Authority.RoleIndex.MANAGER, Authority.RoleIndex.WORK_TEAM)) {
                rgno = Integer.valueOf(request.getParameter("rgno"));
            }

            Integer page = 1;
            if (request.getParameter("page") != null) {
                page = Integer.parseInt(request.getParameter("page"));
            }
            List<Cadre> cadreList = cadredao.getCadre(rgno, null, null, null, page);
            Integer pageAmount = cadredao.getPageNumber(rgno);
            request.setAttribute("page", page);
            request.setAttribute("pageAmount", pageAmount);
            request.setAttribute("cadreList", cadreList);
            request.getRequestDispatcher("WEB-INF/jsp/performancePage/cadre.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
