package servlet;

import dao.*;
import dao.impl.*;
import model.*;
import model.role.Student;
import util.AuthCheck;
import util.DateUtils;
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
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ching Yun Yu on 2018/5/8.
 */

@WebServlet("/IndividualCounsel")
@MultipartConfig
public class IndividualCounselServlet extends _BaseServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if (!AuthCheck.isAuth(request)) {
            request.setAttribute("error", "您無權限使用此系統");
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            return;
        }

        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");
        UploadFileDao uploadFileDao = new UploadFileDaoImpl(account);
        StudentDao studentDao = new StudentDaoImpl(account);


        if (!account.getAuthority().containsRole(
                Authority.RoleIndex.COUNSELOR,
                Authority.RoleIndex.STUDENT,
                Authority.RoleIndex.MANAGER
        )) {
            response.setStatus(401);
            request.setAttribute("error", "參數設定錯誤，若有疑問請洽系統管理員。");
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            return;
        }

        try {
            String action = request.getParameter("action");
            if (action != null && action.equals("export")) {
                this.doPost(request, response);
            }
            IndividualCounselDao individualcounseldao = new IndividualCounselDaoImpl(account);
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

            if (account.getAuthority().containsRole(
                    Authority.RoleIndex.COUNSELOR,
                    Authority.RoleIndex.MANAGER)) {
                List<IndividualCounsel> individualCounselList = individualcounseldao.getIndividualCounselByTitleOrDate(startTime, endTime, title, null);
                Map<Integer, String> fileMap = new HashMap<>();
                Map<Integer, Student> studentMap = new HashMap<>();
                for (IndividualCounsel individualCounsel :
                        individualCounselList) {
                    Student student = studentDao.getStudentByRgno(individualCounsel.getRgno());
                    studentMap.put(individualCounsel.getRgno(), student);
                    if (individualCounsel.getFileId() == null || individualCounsel.getFileId() == 0) continue;
                    Integer fileId = individualCounsel.getFileId();
                    UploadFile uploadFile = uploadFileDao.getById(fileId);
                    String link = Upload.generateDownloadLink(request, account, uploadFile);
                    fileMap.put(fileId, link);
                }
                request.setAttribute("individualCounselList", individualCounselList);
                request.setAttribute("fileMap", fileMap);
                request.setAttribute("studentMap", studentMap);
                request.getRequestDispatcher("/WEB-INF/jsp/individualCounsel.jsp").forward(request, response);
            } else if (AuthCheck.isAuth(request, Authority.RoleIndex.STUDENT)) {
                List<IndividualCounsel> individualCounselList = individualcounseldao.getIndividualCounselByTitleOrDate(startTime, endTime, title, account.<Student>getRoleDetail(Authority.RoleIndex.STUDENT).getRgno());
                Map<Integer, String> fileMap = new HashMap<>();
                Map<Integer, Student> studentMap = new HashMap<>();

                for (IndividualCounsel individualCounsel :
                        individualCounselList) {
                    Student student = studentDao.getStudentByRgno(individualCounsel.getRgno());
                    studentMap.put(individualCounsel.getRgno(), student);
                    if (individualCounsel.getFileId() == null || individualCounsel.getFileId() == 0) continue;
                    Integer fileId = individualCounsel.getFileId();
                    UploadFile uploadFile = uploadFileDao.getById(fileId);
                    String link = Upload.generateDownloadLink(request, account, uploadFile);
                    fileMap.put(fileId, link);
                }
                request.setAttribute("fileMap", fileMap);
                request.setAttribute("studentMap", studentMap);
                request.setAttribute("individualCounselList", individualCounselList);
                request.getRequestDispatcher("/WEB-INF/jsp/stuIndividualCounsel.jsp").forward(request, response);
            }

        } catch (NumberFormatException e) {
            request.setAttribute("error", "參數設定錯誤，若有疑問請洽系統管理員。");
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "發生異常錯誤，請洽系統管理員");
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        if (!AuthCheck.isAuth(request)) {
//        	response.sendRedirect("login");
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
        IndividualCounselDao individualcounseldao = new IndividualCounselDaoImpl(account);

        Path basePath = Paths.get(Upload.getSaveRootDir(getServletContext()).getAbsolutePath(), "..", "template");
        Path templatePath = Paths.get(basePath.toFile().getAbsolutePath(), "03_諮詢紀錄資料", "1_個人諮詢紀錄.xlsx");
        ExcelParse parser = new ExcelParse();

        try {
            List<IndividualCounsel> individualCounselList = individualcounseldao.getIndividualCounselByTitleOrDate(null, null, "", null);

            parser.loadExcel(templatePath.toFile().getAbsolutePath());
            int row = 1;
            int sheetNo = 1;

            for (IndividualCounsel individualCounsel : individualCounselList) {
                int col = 0;
                parser.writeExcelByRowAndCell(sheetNo, ++row, ++col, individualCounsel.getRgno());
                parser.writeExcelByRowAndCell(sheetNo, row, ++col, individualCounsel.getStartTime().toString());
                parser.writeExcelByRowAndCell(sheetNo, row, ++col, individualCounsel.getEndTime().toString());
                parser.writeExcelByRowAndCell(sheetNo, row, ++col, individualCounsel.getTitle());
                parser.writeExcelByRowAndCell(sheetNo, row, ++col, individualCounsel.getLocation());
                parser.writeExcelByRowAndCell(sheetNo, row, ++col, individualCounsel.getDescription());
                parser.writeExcelByRowAndCell(sheetNo, row, ++col, individualCounsel.getCounselor());
                parser.writeExcelByRowAndCell(sheetNo, row, ++col, individualCounsel.getOriginalFilename());
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            parser.write(outputStream);
            HttpUtils.sendFile(response, outputStream, "application/octet-stream", "1_個人諮詢紀錄.xlsx");
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
        response.sendRedirect("IndividualCounsel");
    }

}
