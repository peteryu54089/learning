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

@WebServlet("/SubmitCourse")
@MultipartConfig
public class SubmitCourseServlet extends _BaseServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            Account account = (Account) session.getAttribute("account");
            if (account == null || !account.getAuthority().containsRole(
                    Authority.RoleIndex.MANAGER, Authority.RoleIndex.WORK_TEAM, Authority.RoleIndex.COURSE_SUBMITTER
            )) {
//            	response.sendRedirect("login");
                request.setAttribute("error", "您無權限使用此系統");
                request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
                return;
            }

            SystemConfigDao dao = new SystemConfigDaoImpl(account);
            boolean isNight = dao.isNightSchool();

            request.setAttribute("isNight", isNight);
            request.getRequestDispatcher("/WEB-INF/jsp/submitCourse.jsp").forward(request, response);
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
                Authority.RoleIndex.COURSE_SUBMITTER,
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
                Integer.parseInt(request.getParameter("form_idx")),
                (Account) request.getSession().getAttribute("account"),
                config, templatePath);
    }

    public static void sendResponse(
            HttpServletRequest request,
            HttpServletResponse response,
            Integer yearAndSem,
            Integer formIdx,
            Account account,
            SystemConfig config, Path basePath
    ) throws IOException, ServletException {
        Integer year;
        Integer sem;
        ExcelTemplateData excelTemplateData;

        try {
            sem = yearAndSem % 10;
            year = yearAndSem / 10;

            excelTemplateData = getTemplateData().get(formIdx);

            // test exists
            excelTemplateData.idx.toString();
            account.getAuthority();
        } catch (NumberFormatException | NullPointerException e) {
            response.setStatus(400);
            return;
        }

        Path templatePath = Paths.get(basePath.toFile().getAbsolutePath(), "01_學業表現上傳資料", excelTemplateData.fileName);
        ExcelParse parser = new ExcelParse();

        try {
            parser.loadExcel(templatePath.toFile().getAbsolutePath());
            int col = 0;
            int row = 1;
            int sheetNo = 1;
            parser.writeExcelByRowAndCell(sheetNo, ++row, ++col, config.getSchoolInfo().getId());
            parser.writeExcelByRowAndCell(sheetNo, row, ++col, String.valueOf(year));
            parser.writeExcelByRowAndCell(sheetNo, row, ++col, String.valueOf(sem));

            ////

            try (Connection con = DbConn.getConnection(account)) {
                for (int k = 0; k < excelTemplateData.tableNames.length; k++) {
                    String table = excelTemplateData.tableNames[k];
                    Integer totalColCnt = excelTemplateData.colCnts[k];
                    sheetNo++;

                    try (PreparedStatement ps = con.prepareStatement(
                            "SELECT * FROM " + table + " WHERE (" +
                                    "TERM_YEAR IS NULL AND TERM_SEM IS NULL" +
                                ") OR (" +
                                    "TERM_YEAR = ? AND TERM_SEM = ?" +
                                ")")) {
                        int j = 0;
                        ps.setInt(++j, year);
                        ps.setInt(++j, sem);
                        try (ResultSet rs = ps.executeQuery()) {
                            row = 1;
                            while (rs.next()) {
                                row++;
                                for (col = 1; col <= totalColCnt; col++) {
                                    parser.writeExcelByRowAndCell(sheetNo, row, col, rs.getString(col));
                                    postHandlerRow(account, request, excelTemplateData, parser, sheetNo, row, rs);
                                }
                            }
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            parser.write(outputStream);
            HttpUtils.sendFile(response, outputStream, "application/octet-stream", excelTemplateData.fileName);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "參數設定錯誤，若有疑問請洽系統管理員。");
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            return;
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "發生異常錯誤，請洽系統管理員");
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            return;
        }
    }

    private static Map<Integer, ExcelTemplateData> _formData = null;

    private static synchronized Map<Integer, ExcelTemplateData> getTemplateData() {
        if (_formData != null) return Collections.unmodifiableMap(_formData);
        _formData = new HashMap<>();
        _formData.put(1, new ExcelTemplateData(1, "1_科目名冊.xlsx", "EDU_EXCEL_1", 8));
        _formData.put(2, new ExcelTemplateData(2, "2_學生資料名冊.xlsx", "EDU_EXCEL_2", 7));
        _formData.put(3, new ExcelTemplateData(3, "3_學生成績名冊.xlsx", new String[]{
                "EDU_EXCEL_3_1", "EDU_EXCEL_3_2" // , "EDU_EXCEL_3_3"
        }, new Integer[]{11, 13, 13}));
        _formData.put(4, new ExcelTemplateData(4, "4_學生重修重讀成績名冊.xlsx", new String[]{
                "EDU_EXCEL_4_1", "EDU_EXCEL_4_2"
        }, new Integer[]{11, 13}));

//        _formData.put(5, new ExcelTemplateData(5, "5_進修學校學生成績名冊.xlsx", new String[]{
//                "EDU_EXCEL_5_1", "EDU_EXCEL_5_2"
//        }, new Integer[]{10, 10}));

        _formData.put(6, new ExcelTemplateData(6, "6_缺勤紀錄名冊.xlsx", "EDU_EXCEL_6", 6));

        _formData.put(7, new ExcelTemplateData(7, "7_學生課程學習成果名冊.xlsx",
                new String[]{"EDU_EXCEL_7_1", "EDU_EXCEL_7_2"/*, "EDU_EXCEL_7_3"*/},
                new Integer[]{8, 10, 11}));

        _formData.put(8, new ExcelTemplateData(8, "8_學生重修重讀課程學習成果名冊.xlsx",
                new String[]{"EDU_EXCEL_8_1", "EDU_EXCEL_8_2"},
                new Integer[]{11, 10}));

        _formData.put(9, new ExcelTemplateData(9, "9_進修學校學生課程學習成果名冊.xlsx",
                new String[]{"EDU_EXCEL_7_1"},
                new Integer[]{8}));


        return Collections.unmodifiableMap(_formData);
    }

    private static void postHandlerRow(
            HasSchoolSchema hasSchoolSchema, HttpServletRequest request,
            ExcelTemplateData excelTemplateData, ExcelParse parser, int sheetNo, int row, ResultSet rs) throws Exception {
        if (excelTemplateData.idx == 7 || excelTemplateData.idx == 8 || excelTemplateData.idx == 9) { // 課程成果
            DownloadFileRequest r = null;
            try {
                r = new DownloadFileRequest(hasSchoolSchema.getSchoolSchema(), rs.getInt("CRID"), rs.getString("idno"), "", "");
            } catch (SQLException e) {
                e.printStackTrace();
                return;
            }

            String url = Upload.generateDownloadLink(request, r);
            url = url.replace("/externalDownloadLink", "/externalCourseRecordView");
            if (excelTemplateData.idx == 7) {
                switch (sheetNo) {
                    case 2:
                        parser.writeExcelByRowAndCell(sheetNo, row, 6, url);
                        break;
                    case 3:
                        parser.writeExcelByRowAndCell(sheetNo, row, 8, url);
                        break;
                    case 4:
                        parser.writeExcelByRowAndCell(sheetNo, row, 9, url);
                        break;
                }
            } else if (excelTemplateData.idx == 8) {

                switch (sheetNo) {
                    case 2:
                        parser.writeExcelByRowAndCell(sheetNo, row, 9, url);
                        break;
                    case 3:
                        parser.writeExcelByRowAndCell(sheetNo, row, 8, url);
                        break;
                }
            } else if (excelTemplateData.idx == 9) {
                switch (sheetNo) {
                    case 2:
                        parser.writeExcelByRowAndCell(sheetNo, row, 6, url);
                        break;
                }
            }
        }
    }

    private static class ExcelTemplateData {
        private Integer idx;
        private String fileName;
        private String[] tableNames;
        private Integer[] colCnts;

        private ExcelTemplateData(Integer idx, String fileName, String tableName, Integer colCnt) {
            this(idx, fileName, new String[]{tableName}, new Integer[]{colCnt});
        }

        private ExcelTemplateData(Integer idx, String fileName, String[] tableName, Integer[] colCnt) {
            this.idx = idx;
            this.fileName = fileName;
            this.tableNames = tableName;
            this.colCnts = colCnt;
        }
    }
}
