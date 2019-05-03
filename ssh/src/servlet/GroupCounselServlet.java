package servlet;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dao.*;
import dao.impl.*;
import model.*;
import util.AuthCheck;
import util.DateUtils;
import util.HttpUtils;
import util.Upload;
import util.excel.ExcelParse;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by Ching Yun Yu on 2018/5/8.
 */

@WebServlet("/GroupCounsel")
@MultipartConfig
public class GroupCounselServlet extends _BaseServlet {


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!AuthCheck.isAuth(request)) {
            request.setAttribute("error", "您無權限使用此系統");
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            return;
        }
        String action = request.getParameter("action");
        Account account = (Account) request.getSession().getAttribute("account");
        UploadFileDao uploadFileDao = new UploadFileDaoImpl(account);
        if (action == null) {
            if (account.getAuthority().containsRole(Authority.RoleIndex.COUNSELOR, Authority.RoleIndex.MANAGER)) {
                request.getRequestDispatcher("WEB-INF/jsp/groupCounsel.jsp").forward(request, response);

            } else if (account.getAuthority().containsRole(Authority.RoleIndex.STUDENT)) {
                request.getRequestDispatcher("WEB-INF/jsp/stuGroupCounsel.jsp").forward(request, response);
            }
            return;
        }
        try {
            if (action != null && action.equals("export")) {
                this.doPost(request, response);
            }
            GroupCounselDao groupcounseldao = new GroupCounselDaoImpl(account);
            StudentDao studentDao = new StudentDaoImpl(account);
            String startDate = request.getParameter("startDate");
            String endDate = request.getParameter("endDate");
            String title = request.getParameter("title");
            Date startTime = null;
            Date endTime = null;
            if (startDate != null && !startDate.trim().isEmpty()) {
                startTime = DateUtils.parseDateInput(startDate);
            }
            if (endDate != null && !endDate.trim().isEmpty()) {
                endTime = DateUtils.parseDateInput(endDate);
            }
            if (account.getAuthority().containsRole(Authority.RoleIndex.COUNSELOR, Authority.RoleIndex.MANAGER)) {
                List<GroupCounsel> groupCounselList = groupcounseldao.getGroupCounselByTitleOrDate(startTime, endTime, title);
                Map<Integer, String> fileMap = new HashMap<>();
                for (GroupCounsel groupCounsel :
                        groupCounselList) {
                    if (groupCounsel.getFileId() == null || groupCounsel.getFileId() == 0) continue;
                    Integer fileId = groupCounsel.getFileId();
                    UploadFile uploadFile = uploadFileDao.getById(fileId);
                    String link = Upload.generateDownloadLink(request, account, uploadFile);
                    fileMap.put(fileId, link);
                }
                request.setAttribute("groupCounselList", groupCounselList);
                request.setAttribute("fileMap", fileMap);
                request.getRequestDispatcher("/WEB-INF/jsp/groupCounsel.jsp").forward(request, response);
            } else if (account.getAuthority().containsRole(
                    Authority.RoleIndex.STUDENT)) {
                List<GroupCounsel> groupCounselList = groupcounseldao.getGroupCounselByTitleOrDateAndRegNo(startTime, endTime, title, account.getRoleDetail(Authority.RoleIndex.STUDENT).getRegNumber());
                request.setAttribute("groupCounselList", groupCounselList);
                request.getRequestDispatcher("/WEB-INF/jsp/stuGroupCounsel.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @SuppressWarnings("unchecked")
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        if (!AuthCheck.isAuth(request)) {
            request.setAttribute("error", "您無權限使用此系統");
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            return;
        }
        HttpSession session = request.getSession();

        Account account = (Account) session.getAttribute("account");
        if (!account.getAuthority().containsRole(
                Authority.RoleIndex.COUNSELOR,
                Authority.RoleIndex.MANAGER
        )) {
            response.setStatus(401);
            request.setAttribute("error", "參數設定錯誤，若有疑問請洽系統管理員。");
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            return;
        }
        GroupCounselDao groupcounseldao = new GroupCounselDaoImpl(account);

        Path basePath = Paths.get(Upload.getSaveRootDir(getServletContext()).getAbsolutePath(), "..", "template");
        Path templatePath = Paths.get(basePath.toFile().getAbsolutePath(), "03_諮詢紀錄資料", "2_團體諮詢紀錄.xlsx");
        ExcelParse parser = new ExcelParse();

        try {
            List<GroupCounsel> groupCounselList = groupcounseldao.getGroupCounselByTitleOrDate(null, null, "");

            parser.loadExcel(templatePath.toFile().getAbsolutePath());
            int row = 1;
            int sheetNo = 1;

            for (GroupCounsel groupCounsel : groupCounselList) {
                int col = 0;
                parser.writeExcelByRowAndCell(sheetNo, ++row, ++col, groupCounsel.getGroupCounselMember().size());
                parser.writeExcelByRowAndCell(sheetNo, row, ++col, groupCounsel.getStartTime().toString());
                parser.writeExcelByRowAndCell(sheetNo, row, ++col, groupCounsel.getEndTime().toString());
                parser.writeExcelByRowAndCell(sheetNo, row, ++col, groupCounsel.getTitle());
                parser.writeExcelByRowAndCell(sheetNo, row, ++col, groupCounsel.getLocation());
                parser.writeExcelByRowAndCell(sheetNo, row, ++col, groupCounsel.getDescription());
                parser.writeExcelByRowAndCell(sheetNo, row, ++col, groupCounsel.getCounselor());
                parser.writeExcelByRowAndCell(sheetNo, row, ++col, groupCounsel.getOriginalFilename());
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            parser.write(outputStream);
            HttpUtils.sendFile(response, outputStream, "application/octet-stream", "2_團體諮詢紀錄.xlsx");
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

        response.sendRedirect("GroupCounsel");
    }

}
