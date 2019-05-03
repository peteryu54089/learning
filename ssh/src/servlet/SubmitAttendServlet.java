package servlet;

import dao.SystemConfigDao;
import dao.impl.SystemConfigDaoImpl;
import dbconn.DbConn;
import model.*;
import util.AuthCheck;
import util.HttpUtils;
import util.Upload;
import util.excel.ExcelParse;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static servlet.SubmitCourseServlet.sendResponse;

@WebServlet("/SubmitAttend")
@MultipartConfig
public class SubmitAttendServlet extends _BaseServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            Account account = (Account) session.getAttribute("account");
            if (account == null || !account.getAuthority().containsRole(
                    Authority.RoleIndex.MANAGER, Authority.RoleIndex.WORK_TEAM, Authority.RoleIndex.ATTEND_SUBMITTER
            )) {
                request.setAttribute("error", "您無權限使用此系統");
                request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
                return;
            }


            request.getRequestDispatcher("/WEB-INF/jsp/submitAttend.jsp").forward(request, response);
        } catch (NullPointerException e) {
            e.printStackTrace();
            request.setAttribute("error", "參數設定錯誤，若有疑問請洽系統管理員。");
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            return;
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "系統發生錯誤，若有疑問請洽系統管理員。");
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            return;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!AuthCheck.isAuth(request)) {
            request.setAttribute("error", "您無權限使用此系統");
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            return;
        }

        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");
        if (!account.getAuthority().containsRole(
                Authority.RoleIndex.ATTEND_SUBMITTER,
                Authority.RoleIndex.WORK_TEAM,
                Authority.RoleIndex.MANAGER
        )) {
            response.setStatus(401);
            request.setAttribute("error", "參數設定錯誤，若有疑問請洽系統管理員。");
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            return;
        }

        SystemConfigDao systemConfigDao = new SystemConfigDaoImpl(account);
        SystemConfig config = systemConfigDao.getSystemConfig();
        Path templatePath = Paths.get(Upload.getSaveRootDir(getServletContext()).getAbsolutePath(), "..", "template");
        sendResponse(
                request, response,
                Integer.parseInt(request.getParameter("course_year_sem")),
                6,
                account,
                config, templatePath);
    }
}
