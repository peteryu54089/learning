package servlet;

import config.Config;
import dao.*;
import dao.impl.*;
import model.*;
import model.Authority.RoleIndex;
import model.role.Student;
import util.AuthCheck;
import util.DateUtils;
import util.FilePartUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/courseRecordUpload")
@MultipartConfig
public class CourseRecordUploadServlet extends _BaseServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        Account account = (Account) request.getSession().getAttribute("account");
        CourseRecordDao courseRecordDao = new CourseRecordDaoImpl(account);
        CourseRecordDocumentDao courseRecordDocumentDao = new CourseRecordDocumentDaoImpl(account);
        SelectCourseDao selectCourseDao = new SelectCourseDaoImpl(account);
        SystemConfigDao systemConfigDao = new SystemConfigDaoImpl(account);
        UploadFileDao uploadDao = new UploadFileDaoImpl(account);
        SystemConfig systemConfig;
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
        if (!AuthCheck.isAuth(request, RoleIndex.STUDENT)) {
//        	response.sendRedirect("login");
            request.setAttribute("error", "您無權限使用此系統");
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            return;
        }
        try {
            HttpSession session = request.getSession();
            account = (Account) session.getAttribute("account");

            Integer fileAmount = systemConfig.getCourseStudyRecord().getMaxAmountPerRecord();
            //資料處理
            List<Part> filePart = new ArrayList<>();
            List<String> fileOriginalname = new ArrayList<>();
            Part part;
            for (int i = 1; i <= fileAmount; i++) {
                part = request.getPart("file" + Integer.toString(i));
                if (part != null && part.getSize() != 0) {
                    if (part.getSize() > systemConfig.getCourseStudyRecord().getMaxSize()) {
                        PrintWriter out = response.getWriter();
                        out.println("<script>alert('file size to large')</script>");
                        out.println("<script>window.history.go(-1)</script>");
                        return;
                        // TODO 警告訊息
                    }
                    filePart.add(part);
                    fileOriginalname.add(FilePartUtils.getSubmittedFileName(part));
                }
            }

            String course_no = request.getParameter("course_no");
            Course course = selectCourseDao.getSelectCourseByCourseNo(course_no);
            //TODO 檢查可否上傳
            if (!course.isUploadable()) {
                response.getWriter().write("發生異常錯誤，若有疑問請洽系統管理員");
                return;
            }
            String content = request.getParameter("content");
            String page = request.getParameter("page");
            Semester sem = DateUtils.getCurrentSemester();
            String modifyid = request.getParameter("modifyid");
            if (modifyid.equals("0")) {
                //TODO 更好的取得RGNO
                Integer rgno = ((Student) account.getRoleDetail(RoleIndex.STUDENT)).getRgno();
                Integer pid = courseRecordDao.uploadCourseRecord(rgno, Integer.parseInt(course.getTerm_year()), Integer.parseInt(course.getTerm_sem()), course_no, course.getCourse_Cname(), content);
                for (int i = 0; i < filePart.size(); i++) {
                    UploadFile uploadFile = uploadDao.insert(
                            Config.STUDENT_UPLOADED_FILE_DIR,
                            systemConfig.getSchoolInfo().getId(),
                            DateUtils.getCurrentSemester(),
                            rgno,
                            fileOriginalname.get(i),
                            filePart.get(i).getInputStream());
                    courseRecordDocumentDao.insertCourseRecordDocument(new CourseRecordDocument(pid, uploadFile.getId(), null, null, null));
                }
            } else {
                //檢查刪除檔案
                String delid;
                for (int i = 1; i <= fileAmount; i++) {
                    delid = request.getParameter("delfile" + Integer.toString(i));
                    if (delid == null)
                        continue;
                    if (Integer.parseInt(delid) > 0) {
                        uploadDao.deleteById(Integer.parseInt(delid));
                    }
                }
                Integer pid = Integer.parseInt(modifyid);
                for (int i = 0; i < filePart.size(); i++) {
                    //TODO 更好的取得RGNO
                    Integer rgno = ((Student) account.getRoleDetail(RoleIndex.STUDENT)).getRgno();
                    UploadFile uploadFile = uploadDao.insert(
                            Config.STUDENT_UPLOADED_FILE_DIR,
                            systemConfig.getSchoolInfo().getId(),
                            DateUtils.getCurrentSemester(),
                            rgno,
                            fileOriginalname.get(i),
                            filePart.get(i).getInputStream());
                    courseRecordDocumentDao.insertCourseRecordDocument(new CourseRecordDocument(pid, uploadFile.getId(), null, null, null));
                }
                CourseRecord courseRecord = courseRecordDao.getCourseRecordByID(null, modifyid);
                courseRecord.setContent(content);
                courseRecord.setVerify_message(null);
                courseRecord.setVerifier_staff_code(null);
                courseRecord.setVerify_at(null);
                //TODO 更好的取得RGNO
                Integer rgno = Integer.valueOf(((Student) account.getRoleDetail(RoleIndex.STUDENT)).getRgno());
                courseRecordDao.updateCourseRecordByID(rgno, modifyid, courseRecord);
            }
            PrintWriter out = response.getWriter();
            out.println("<script>window.location = document.referrer</script>");
            //response.sendRedirect("scheduler?page=" + page);
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
}

