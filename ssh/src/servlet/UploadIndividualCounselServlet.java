package servlet;

import config.Config;
import dao.*;
import dao.impl.*;
import model.*;
import model.role.Staff;
import model.role.Student;
import org.apache.commons.io.FilenameUtils;
import util.AuthCheck;
import util.DateUtils;
import util.FilePartUtils;
import util.MisSystemUtil;
import util.SchemaUtil;
import util.Upload;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Ching Yun Yu on 2018/5/8.
 */
@WebServlet("/UploadIndividualCounsel")
@MultipartConfig
public class UploadIndividualCounselServlet extends _BaseServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if (!AuthCheck.isAuth(request)) {
//        	response.sendRedirect("login");
            request.setAttribute("error", "您無權限使用此系統");
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            return;
        }

        HttpSession session = request.getSession();
        Account account = (Account) request.getSession().getAttribute("account");
        IndividualCounselDao individualCounselDao = new IndividualCounselDaoImpl(account);


        int year = 0;
        int sem = 0;
        MisSystemData misSystemData = null;
        try {    // 取得學務系統學年期
            misSystemData = MisSystemUtil.getStuAffairsSbjYS(account.getSchoolSchema());
            year = misSystemData.getSbj_year();
            sem = misSystemData.getSbj_sem();
        } catch (Exception e) {
            e.printStackTrace();
        }


        CUnitDao cUnitdao = new CUnitDaoImpl(account);

        List<CUnit> CUnitList = null;
        try {
//            CUnitList = cUnitdao.getCUnit(105, 2, null, null, null);
            CUnitList = cUnitdao.getCUnit(year, sem, null, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            IndividualCounsel individualCounsel = new IndividualCounsel(-1,
                    new Timestamp(new Date().getTime()),
                    new Timestamp(new Date().getTime()),
                    "",
                    "",
                    null,
                    "",
                    "",
                    "", "", 0, null
            );

            int id;
            if (request.getParameter("id") != null) {
                id = Integer.parseInt(request.getParameter("id"));
                if (id >= 0) {
                    individualCounsel = individualCounselDao.getIndividualCounselById(id);
                    StudentDao studentDao = new StudentDaoImpl(account);
                    Student student = studentDao.getStudentByRgno(individualCounsel.getRgno());
                    request.setAttribute("student", student);
                }
            }
            request.setAttribute("individualCounsel", individualCounsel);
            request.setAttribute("CUnitList", CUnitList);
            request.setAttribute("SelfStaffCode", account.<Staff>getRoleDetail(Authority.RoleIndex.COUNSELOR).getStaffCode());
            request.setAttribute("DateUtils", new DateUtils());
            request.getRequestDispatcher("/WEB-INF/jsp/uploadIndividualCounsel.jsp").forward(request, response);
        } catch (NumberFormatException |
                NullPointerException e) {
            request.setAttribute("error", "參數設定錯誤，若有疑問請洽系統管理員。");
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
        } catch (
                SQLException e) {
            request.setAttribute("error", "內容獲取錯誤，若有疑問請洽系統管理員。");
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        if (!AuthCheck.isAuth(request, Authority.RoleIndex.COUNSELOR)) {
//        	response.sendRedirect("login");
            request.setAttribute("error", "您無權限使用此系統");
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            return;
        }

        HttpSession session = request.getSession();
        boolean useOldFile = Boolean.parseBoolean(request.getParameter("useOldFile"));
        Upload upload = new Upload(getServletContext());
        Account account = (Account) request.getSession().getAttribute("account");
        IndividualCounselDao individualCounselDao = new IndividualCounselDaoImpl(account);
        UploadFileDao uploadFileDao = new UploadFileDaoImpl(account);
        SystemConfig systemConfig;
        SystemConfigDao systemConfigDao = new SystemConfigDaoImpl(account);
        try {
            systemConfig = systemConfigDao.getSystemConfig();
            if (systemConfig == null)
                throw new NullPointerException();
        } catch (NullPointerException e) {
            e.printStackTrace();
            response.getWriter().write("發生異常錯誤，若有疑問請洽系統管理員");
            return;
            // TODO 警告訊息
        }
        try {
            Timestamp startTimestamp = new Timestamp(DateUtils.parseDateTimeInput(request.getParameter("startTime")).getTime());
            Timestamp endTimestamp = new Timestamp(DateUtils.parseDateTimeInput(request.getParameter("endTime")).getTime());
            String title = request.getParameter("title");
            String location = request.getParameter("location");
            String description = request.getParameter("description");
            String counselor = request.getParameter("counselor");
            Integer target = Integer.parseInt(request.getParameter("target"));
            String id = request.getParameter("id");
            String submitter = account.<Staff>getRoleDetail(Authority.RoleIndex.COUNSELOR).getStaffCode();

            Part file = request.getPart("file");

            if (id != null && Integer.parseInt(id) != -1) {
                IndividualCounsel oldIndividualCounsel = individualCounselDao.getIndividualCounselById(Integer.parseInt(id));
                IndividualCounsel individualCounsel = new IndividualCounsel(
                        Integer.parseInt(id),
                        startTimestamp,
                        endTimestamp,
                        title,
                        (useOldFile) ? oldIndividualCounsel.getOriginalFilename() : "",
                        (useOldFile) ? oldIndividualCounsel.getFileId() : null,
                        location,
                        description,
                        counselor,
                        submitter,
                        target,
                        null
                );
                if (file != null && file.getSize() > 0) {
                    //  ------------------------------------ start 上傳檔案 ------------------------------------
//                randomFileName = upload.uploadFile(account, file, FilenameUtils.getExtension(file.getSubmittedFileName()));
//                originalFileName = file.getSubmittedFileName();
                    UploadFile uploadFile = uploadFileDao.insert(
                            Config.COUNSEL_UPLOADED_FILE_DIR,
                            systemConfig.getSchoolInfo().getId(),
                            DateUtils.getCurrentSemester(),
                            submitter,
                            FilePartUtils.getSubmittedFileName(file),
                            file.getInputStream());
                    // ------------------------------------ end 上傳檔案 ---------------------------------------
                    individualCounsel.setFileId(uploadFile.getId());
                    individualCounsel.setOriginalFilename(uploadFile.getFileName());

                    individualCounselDao.updateIndividualCounsel(individualCounsel);
                    uploadFileDao.deleteById(oldIndividualCounsel.getFileId());
                } else {
                    individualCounselDao.updateIndividualCounsel(individualCounsel);
                }
            } else {
                IndividualCounsel individualCounsel = new IndividualCounsel(
                        Integer.parseInt(id),
                        startTimestamp,
                        endTimestamp,
                        title,
                        "",
                        null,
                        location,
                        description,
                        counselor,
                        submitter,
                        target,
                        null
                );
                if (file != null && file.getSize() > 0) {
                    //  ------------------------------------ start 上傳檔案 ------------------------------------
//                randomFileName = upload.uploadFile(account, file, FilenameUtils.getExtension(file.getSubmittedFileName()));
//                originalFileName = file.getSubmittedFileName();
                    UploadFile uploadFile = uploadFileDao.insert(
                            Config.COUNSEL_UPLOADED_FILE_DIR,
                            systemConfig.getSchoolInfo().getId(),
                            DateUtils.getCurrentSemester(),
                            submitter,
                            FilePartUtils.getSubmittedFileName(file),
                            file.getInputStream());
                    // ------------------------------------ end 上傳檔案 ---------------------------------------
                    individualCounsel.setFileId(uploadFile.getId());
                    individualCounsel.setOriginalFilename(uploadFile.getFileName());

                    individualCounselDao.insertIndividualCounsel(individualCounsel);
                } else {
                    individualCounselDao.insertIndividualCounsel(individualCounsel);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "系統發生錯誤，若有疑問請洽系統管理員。");
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            return;
        }
        response.sendRedirect("IndividualCounsel");
    }

}
