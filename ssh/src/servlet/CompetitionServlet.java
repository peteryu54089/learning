package servlet;

import com.google.gson.Gson;
import config.Config;
import dao.CompetitionDao;
import dao.SystemConfigDao;
import dao.UploadFileDao;
import dao.impl.CompetitionDaoImpl;
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

@WebServlet("/competition")
@MultipartConfig
public class CompetitionServlet extends _BasePerformanceServlet {


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        if (!AuthCheck.isAuth(request, Authority.RoleIndex.STUDENT)) {
            request.setAttribute("error", "您無權限使用此系統");
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            return;
        }
        Account account = (Account) request.getSession().getAttribute("account");
        SystemConfigDao systemConfigDao = new SystemConfigDaoImpl(account);
        SystemConfig systemConfig = systemConfigDao.getSystemConfig();
        CompetitionDao competitiondao = new CompetitionDaoImpl(account);


        try {
            request.setAttribute("docSize", systemConfig.getPerformance().getMaxDocSize());
            request.setAttribute("docAllowType", systemConfig.getPerformance().getAllowDocTypesString());
            request.setAttribute("videoSize", systemConfig.getPerformance().getMaxVideoSize());
            request.setAttribute("videoAllowType", systemConfig.getPerformance().getAllowVideoTypesString());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        try {
            String name = request.getParameter("name");//競賽名稱
            String item = request.getParameter("item");//項目
            String field = request.getParameter("field");//競賽領域代碼
            String level = request.getParameter("level");
            String award = request.getParameter("award");
            String content = request.getParameter("content");
            String time = request.getParameter("time");
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
                Competition competition = new Competition(
                        rgno, content, PerformanceBaseModel.SourceType.STUDENT,
                        name, item, level, award, time, type, field,
                        docFile != null ? docFile.getId() : null,
                        videoFile != null ? videoFile.getId() : null,
                        externalLink);

                competitiondao.insertCompetition(competition);
                //回傳最後一頁
                Integer pageAmount = competitiondao.getPageNumber(rgno);
                response.sendRedirect("competition?page=" + pageAmount);
                return;
            }
            //更新資料
            else {
                Competition competition = competitiondao.getCompetitionByID(rgno, Integer.parseInt(modifyid));
                if (!competition.student_modifiable()) {
                    response.setContentType("text/html; charset=UTF-8");
                    PrintWriter out = response.getWriter();
                    out.println("<script>alert('無法修改')</script>");
                    out.println("<script>window.history.go(-1)</script>");
                    return;
                }

                competition.setName(name);
                competition.setItem(item);
                competition.setField(field);
                competition.setLevel(level);
                competition.setAward(award);
                competition.setContent(content);
                competition.setTime(time);
                competition.setType(type);
                competition.setExternalLink(externalLink);

                updateOldPerformanceModel(uploadFileDao, competition, docFile, videoFile);

                competitiondao.updateCompetitionByID(competition, rgno, Integer.parseInt(modifyid));
                Integer page = 1;
                if (request.getParameter("page") != null) {
                    page = Integer.parseInt(request.getParameter("page"));
                }

                response.sendRedirect("competition?page=" + page);
                return;
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
        SystemConfig systemConfig;
        CompetitionDao competitiondao;
        try {
            Account account = (Account) request.getSession().getAttribute("account");
            SystemConfigDao systemConfigDao = new SystemConfigDaoImpl(account);
            systemConfig = systemConfigDao.getSystemConfig();
            competitiondao = new CompetitionDaoImpl(account);
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
            Integer page = 1;
            Integer rgno = 0;
            if (request.getParameter("page") != null) {
                page = Integer.parseInt(request.getParameter("page"));
            }

            if (account.getAuthority().isStudent()) {
                rgno = account.<Student>getRoleDetail(Authority.RoleIndex.STUDENT).getRgno();
            } else {
                rgno = Integer.parseInt(request.getParameter("rgno"));
            }

            List<Competition> competitionList = competitiondao.getCompetition(rgno, null, null, null, page);
            Integer pageAmount = competitiondao.getPageNumber(rgno);
            Field field = new Field();
            request.setAttribute("page", page);
            request.setAttribute("field", field);
            request.setAttribute("pageAmount", pageAmount);
            request.setAttribute("competitionList", competitionList);
            request.getRequestDispatcher("WEB-INF/jsp/performancePage/competition.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
