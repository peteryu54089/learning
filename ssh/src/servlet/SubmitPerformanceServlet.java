package servlet;

import dao.*;
import dao.impl.*;
import model.*;
import util.AuthCheck;
import util.HttpUtils;
import util.MenuBarUtil;
import util.StringUtils;
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
import java.util.List;

@WebServlet("/SubmitPerformance")
@MultipartConfig
public class SubmitPerformanceServlet extends _BaseServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if (!AuthCheck.isAuth(request)) {
//        	response.sendRedirect("login");
        	request.setAttribute("error", "您無權限使用此系統");
        	request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            return;
        }

        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");

        if (!account.getAuthority().containsRole(
                Authority.RoleIndex.WORK_TEAM, Authority.RoleIndex.PERFORMANCE_SUBMITTER, Authority.RoleIndex.MANAGER
        )) {
            response.setStatus(401);
            return;
        }

        try {
        	request.setAttribute("MenuBarUtil", new MenuBarUtil());
            request.getRequestDispatcher("/WEB-INF/jsp/submitPerformance.jsp").forward(request, response);
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
                Authority.RoleIndex.PERFORMANCE_SUBMITTER,
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
        if ("CADRE".equals(request.getParameter("action"))) {
//            sendCadre(request, response, config, templatePath);
            sendCadre(request, response, config, templatePath.normalize());
            return;
        } else if ("PERFORMANCE".equals(request.getParameter("action"))) {
//            sendPerformance(request, response, config, templatePath);
            sendPerformance(request, response, config, templatePath.normalize());
            return;
        }

    }

    private void sendCadre(HttpServletRequest request, HttpServletResponse response,
                           SystemConfig config, Path basePath
    ) throws IOException, ServletException {

        CadreDao cadredao = new CadreDaoImpl((HasSchoolSchema) request.getSession().getAttribute("account"));

        Integer year;
        Integer sem;

        try {
            year = Integer.parseInt(request.getParameter("term_year"));
            sem = year % 10;
            year /= 10;
        } catch (NumberFormatException e) {
            response.setStatus(400);
            return;
        }

        Path templatePath = Paths.get(basePath.toFile().getAbsolutePath(), "02_非學業表現上傳資料", "1_校內幹部經歷名冊.xlsx");
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
            sheetNo++;
            row = 1;
            List<Cadre> cadreList = cadredao.getExportCadreRecords(year, sem);

            for (Cadre cadre : cadreList) {
                col = 0;
                parser.writeExcelByRowAndCell(sheetNo, ++row, ++col, cadre.getStuBasicInfo().idNumber);
                parser.writeExcelByRowAndCell(sheetNo, row, ++col, cadre.getStuBasicInfo().birth);
                parser.writeExcelByRowAndCell(sheetNo, row, ++col, cadre.getUnit());
                parser.writeExcelByRowAndCell(sheetNo, row, ++col, cadre.getStartTime());
                parser.writeExcelByRowAndCell(sheetNo, row, ++col, cadre.getEndTime());
                parser.writeExcelByRowAndCell(sheetNo, row, ++col, cadre.getJob());
                parser.writeExcelByRowAndCell(sheetNo, row, ++col, cadre.getLevel());
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            parser.write(outputStream);
            HttpUtils.sendFile(response, outputStream, "application/octet-stream", "1_校內幹部經歷名冊.xlsx");
            return;
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

    private void sendPerformance(HttpServletRequest request, HttpServletResponse response,
                                 SystemConfig config, Path basePath
    ) throws IOException, ServletException {
        Account account = (Account) request.getSession().getAttribute("account");

        CadreDao cadredao = new CadreDaoImpl(account);
        CompetitionDao competitiondao = new CompetitionDaoImpl(account);
        LicenseRecordDao licensedao = new LicenseRecordDaoImpl(account);
        VolunteerDao volunteerdao = new VolunteerDaoImpl(account);
        OtherDao otherdao = new OtherDaoImpl(account);
        SubmitPerformanceDao submitperformancedao = new SubmitPerformanceDaoImpl(account);

        Integer year;

        try {
            year = Integer.parseInt(request.getParameter("term_year"));
        } catch (NumberFormatException e) {
            response.setStatus(400);
            return;
        }

        Path templatePath = Paths.get(basePath.toFile().getAbsolutePath(), "02_非學業表現上傳資料", "2_學生自填多元表現名冊.xlsx");
        ExcelParse parser = new ExcelParse();

        try {
            parser.loadExcel(templatePath.toFile().getAbsolutePath());
            int row = 0;
            int col = 0;
            int sheetNo = 1;
            {
                sheetNo = 1;
                row = 1;
                col = 0;
                parser.writeExcelByRowAndCell(sheetNo, ++row, ++col, config.getSchoolInfo().getId());
                parser.writeExcelByRowAndCell(sheetNo, row, ++col, String.valueOf(year));
                parser.writeExcelByRowAndCell(sheetNo, row, ++col, String.valueOf(0));
            }
            {
                // 幹部經歷紀錄
                sheetNo = 2;
                row = 1;
                List<Cadre> stuReportList = cadredao.getExportStuCadreRecords(year);
                for (Cadre obj : stuReportList) {
                    col = 0;
                    ++row;
                    parser.writeExcelByRowAndCell(sheetNo, row, ++col, obj.getStuBasicInfo().idNumber);
                    parser.writeExcelByRowAndCell(sheetNo, row, ++col, obj.getStuBasicInfo().birth);
                    parser.writeExcelByRowAndCell(sheetNo, row, ++col, obj.getUnit());
                    parser.writeExcelByRowAndCell(sheetNo, row, ++col, obj.getStartTime());
                    parser.writeExcelByRowAndCell(sheetNo, row, ++col, obj.getEndTime());

                    parser.writeExcelByRowAndCell(sheetNo, row, ++col, obj.getJob());
                    parser.writeExcelByRowAndCell(sheetNo, row, ++col, obj.getContent());
                    parser.writeExcelByRowAndCell(sheetNo, row, ++col, obj.getLevel());

                    if (obj.getDocumentFile()!=null) {
                        parser.writeExcelByRowAndCell(sheetNo, row, ++col, Upload.generateDownloadLink(
                                request, account, obj.getDocumentFile())
                        );
                    }

                    if (obj.getVideoFile()!=null) {
                        parser.writeExcelByRowAndCell(sheetNo, row, ++col, Upload.generateDownloadLink(
                                request, account, obj.getVideoFile())
                        );
                    }

                    parser.writeExcelByRowAndCell(sheetNo, row, ++col, obj.getExternalLink());
                }
            }
            {
                // 競賽參與紀錄
                sheetNo = 3;
                row = 1;
                List<Competition> stuCompetitionList
                        = competitiondao.getExportStuCompetitionRecords(year);
                for (Competition obj : stuCompetitionList) {
                    col = 0;
                    ++row;
                    parser.writeExcelByRowAndCell(sheetNo, row, ++col, obj.getStuBasicInfo().idNumber);
                    parser.writeExcelByRowAndCell(sheetNo, row, ++col, obj.getStuBasicInfo().birth);
                    parser.writeExcelByRowAndCell(sheetNo, row, ++col, obj.getName());
                    parser.writeExcelByRowAndCell(sheetNo, row, ++col, obj.getItem());
                    parser.writeExcelByRowAndCell(sheetNo, row, ++col, obj.getField());

                    parser.writeExcelByRowAndCell(sheetNo, row, ++col, obj.getLevel());
                    parser.writeExcelByRowAndCell(sheetNo, row, ++col, obj.getAward());
                    parser.writeExcelByRowAndCell(sheetNo, row, ++col, obj.getTime());
                    parser.writeExcelByRowAndCell(sheetNo, row, ++col, obj.getContent());
                    parser.writeExcelByRowAndCell(sheetNo, row, ++col, obj.getType());

                    if (obj.getDocumentFile()!=null) {
                        parser.writeExcelByRowAndCell(sheetNo, row, ++col, Upload.generateDownloadLink(
                                request, account, obj.getDocumentFile())
                        );
                    }

                    if (obj.getVideoFile()!=null) {
                        parser.writeExcelByRowAndCell(sheetNo, row, ++col, Upload.generateDownloadLink(
                                request, account, obj.getVideoFile())
                        );
                    }

                    parser.writeExcelByRowAndCell(sheetNo, row, ++col, obj.getExternalLink());
                }
            }
            {
                // 檢定證照紀錄
                sheetNo = 4;
                row = 1;
                List<License> stuList
                        = licensedao.getExportStuLicenseRecords(year);
                for (License obj : stuList) {
                    col = 0;
                    ++row;
                    parser.writeExcelByRowAndCell(sheetNo, row, ++col, obj.getStuBasicInfo().idNumber);
                    parser.writeExcelByRowAndCell(sheetNo, row, ++col, obj.getStuBasicInfo().birth);
                    parser.writeExcelByRowAndCell(sheetNo, row, ++col, obj.getCode());
                    parser.writeExcelByRowAndCell(sheetNo, row, ++col, obj.getNote());
                    parser.writeExcelByRowAndCell(sheetNo, row, ++col, obj.getSource());

                    parser.writeExcelByRowAndCell(sheetNo, row, ++col, obj.getResult());
                    parser.writeExcelByRowAndCell(sheetNo, row, ++col, obj.getTime());
                    parser.writeExcelByRowAndCell(sheetNo, row, ++col, obj.getLicensenumber());
                    parser.writeExcelByRowAndCell(sheetNo, row, ++col, obj.getGroup());
                    parser.writeExcelByRowAndCell(sheetNo, row, ++col, obj.getContent());

                    if (obj.getDocumentFile()!=null) {
                        parser.writeExcelByRowAndCell(sheetNo, row, ++col, Upload.generateDownloadLink(
                                request, account, obj.getDocumentFile())
                        );
                    }

                    if (obj.getVideoFile()!=null) {
                        parser.writeExcelByRowAndCell(sheetNo, row, ++col, Upload.generateDownloadLink(
                                request, account, obj.getVideoFile())
                        );
                    }

                    parser.writeExcelByRowAndCell(sheetNo, row, ++col, obj.getExternalLink());
                }
            }
            {
                // 志工服務紀錄
                sheetNo = 5;
                row = 1;
                List<Volunteer> stuList
                        = volunteerdao.getExportStuVolunteerRecords(year);
                for (Volunteer obj : stuList) {
                    col = 0;
                    ++row;
                    parser.writeExcelByRowAndCell(sheetNo, row, ++col, obj.getStuBasicInfo().idNumber);
                    parser.writeExcelByRowAndCell(sheetNo, row, ++col, obj.getStuBasicInfo().birth);
                    parser.writeExcelByRowAndCell(sheetNo, row, ++col, obj.getName());
                    parser.writeExcelByRowAndCell(sheetNo, row, ++col, obj.getPlace());
                    parser.writeExcelByRowAndCell(sheetNo, row, ++col, obj.getStartTime());

                    parser.writeExcelByRowAndCell(sheetNo, row, ++col, obj.getEndTime());
                    parser.writeExcelByRowAndCell(sheetNo, row, ++col, obj.getCount());
                    parser.writeExcelByRowAndCell(sheetNo, row, ++col, obj.getContent());

                    if (obj.getDocumentFile()!=null) {
                        parser.writeExcelByRowAndCell(sheetNo, row, ++col, Upload.generateDownloadLink(
                                request, account, obj.getDocumentFile())
                        );
                    }

                    if (obj.getVideoFile()!=null) {
                        parser.writeExcelByRowAndCell(sheetNo, row, ++col, Upload.generateDownloadLink(
                                request, account, obj.getVideoFile())
                        );
                    }

                    parser.writeExcelByRowAndCell(sheetNo, row, ++col, obj.getExternalLink());
                }
            }
            {
                // 自主學習紀錄
                sheetNo = 6;
                row = 1;
                List<Other> stuList = otherdao.getExportStuVolunteerRecords(year, Other.Type.SELF_TAUGHT);
                for (Other obj : stuList) {
                    col = 0;
                    ++row;
                    parser.writeExcelByRowAndCell(sheetNo, row, ++col, obj.getStuBasicInfo().idNumber);
                    parser.writeExcelByRowAndCell(sheetNo, row, ++col, obj.getStuBasicInfo().birth);
                    parser.writeExcelByRowAndCell(sheetNo, row, ++col, obj.getName());
                    parser.writeExcelByRowAndCell(sheetNo, row, ++col, obj.getUnit());
                    parser.writeExcelByRowAndCell(sheetNo, row, ++col, obj.getStartTime());

                    parser.writeExcelByRowAndCell(sheetNo, row, ++col, obj.getEndTime());
                    parser.writeExcelByRowAndCell(sheetNo, row, ++col, obj.getCount());
                    parser.writeExcelByRowAndCell(sheetNo, row, ++col, obj.getContent());

                    if (obj.getDocumentFile()!=null) {
                        parser.writeExcelByRowAndCell(sheetNo, row, ++col, Upload.generateDownloadLink(
                                request, account, obj.getDocumentFile())
                        );
                    }

                    if (obj.getVideoFile()!=null) {
                        parser.writeExcelByRowAndCell(sheetNo, row, ++col, Upload.generateDownloadLink(
                                request, account, obj.getVideoFile())
                        );
                    }

                    parser.writeExcelByRowAndCell(sheetNo, row, ++col, obj.getExternalLink());
                }
            }
            {
                // 作品成果紀錄
                sheetNo = 7;
                row = 1;
                List<Other> stuList = otherdao.getExportStuVolunteerRecords(year, Other.Type.WROK_RESULT);
                for (Other obj : stuList) {
                    col = 0;
                    ++row;
                    parser.writeExcelByRowAndCell(sheetNo, row, ++col, obj.getStuBasicInfo().idNumber);
                    parser.writeExcelByRowAndCell(sheetNo, row, ++col, obj.getStuBasicInfo().birth);
                    parser.writeExcelByRowAndCell(sheetNo, row, ++col, obj.getName());
                    parser.writeExcelByRowAndCell(sheetNo, row, ++col, obj.getStartTime());
                    parser.writeExcelByRowAndCell(sheetNo, row, ++col, obj.getContent());

                    if (obj.getDocumentFile()!=null) {
                        parser.writeExcelByRowAndCell(sheetNo, row, ++col, Upload.generateDownloadLink(
                                request, account, obj.getDocumentFile())
                        );
                    }

                    if (obj.getVideoFile()!=null) {
                        parser.writeExcelByRowAndCell(sheetNo, row, ++col, Upload.generateDownloadLink(
                                request, account, obj.getVideoFile())
                        );
                    }

                    parser.writeExcelByRowAndCell(sheetNo, row, ++col, obj.getExternalLink());
                }
            }
            {
                // 其他活動紀錄
                sheetNo = 8;
                row = 1;
                List<Other> stuList = otherdao.getExportStuVolunteerRecords(year, Other.Type.OTHER_ACTIVITY);
                for (Other obj : stuList) {
                    col = 0;
                    ++row;
                    parser.writeExcelByRowAndCell(sheetNo, row, ++col, obj.getStuBasicInfo().idNumber);
                    parser.writeExcelByRowAndCell(sheetNo, row, ++col, obj.getStuBasicInfo().birth);
                    parser.writeExcelByRowAndCell(sheetNo, row, ++col, obj.getName());
                    parser.writeExcelByRowAndCell(sheetNo, row, ++col, obj.getUnit());
                    parser.writeExcelByRowAndCell(sheetNo, row, ++col, obj.getStartTime());

                    parser.writeExcelByRowAndCell(sheetNo, row, ++col, obj.getEndTime());
                    parser.writeExcelByRowAndCell(sheetNo, row, ++col, obj.getCount());
                    parser.writeExcelByRowAndCell(sheetNo, row, ++col, obj.getContent());

                    if (obj.getDocumentFile()!=null) {
                        parser.writeExcelByRowAndCell(sheetNo, row, ++col, Upload.generateDownloadLink(
                                request, account, obj.getDocumentFile())
                        );
                    }

                    if (obj.getVideoFile()!=null) {
                        parser.writeExcelByRowAndCell(sheetNo, row, ++col, Upload.generateDownloadLink(
                                request, account, obj.getVideoFile())
                        );
                    }

                    parser.writeExcelByRowAndCell(sheetNo, row, ++col, obj.getExternalLink());
                }
            }
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            parser.write(outputStream);
            HttpUtils.sendFile(response, outputStream, "application/octet-stream", "2_學生自填多元表現名冊.xlsx");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
