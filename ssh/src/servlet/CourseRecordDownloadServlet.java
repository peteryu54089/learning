package servlet;

import dao.CourseRecordDao;
import dao.CourseRecordDocumentDao;
import dao.StudentDao;
import dao.TeacherDao;
import dao.impl.CourseRecordDaoImpl;
import dao.impl.CourseRecordDocumentDaoImpl;
import dao.impl.StudentDaoImpl;
import dao.impl.TeacherDaoImpl;
import model.Account;
import model.Authority;
import model.CourseRecord;
import model.CourseRecordDocument;
import util.HttpUtils;
import util.Upload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

/**
 * Created by David on 2018/1/29.
 */

@WebServlet("/downloadCourseRecord")
public class CourseRecordDownloadServlet extends _BaseServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        this.doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Upload.getSaveRootDir(getServletContext());
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");
        Account account = (Account) request.getSession().getAttribute("account");
        StudentDao studentDao = new StudentDaoImpl(account);
        TeacherDao teacherDao = new TeacherDaoImpl(account);
        CourseRecordDocumentDao courseRecordDocumentDao = new CourseRecordDocumentDaoImpl(account);
        CourseRecordDao courseRecordDao = new CourseRecordDaoImpl(account);
        String regno = null;
        CourseRecordDocument courseRecordDocument = null;

        //TODO 身分判別
        try {
            courseRecordDocument = courseRecordDocumentDao.getCourseRecordDocumentByID(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (courseRecordDocument == null) {
            PrintWriter out = response.getWriter();
            out.println("<script>alert('data not found')</script>");
            out.println("<script>window.history.go(-1)</script>");
            //TODO  警告訊息
        }

        File file = null;
        String originalname = null;
        String fileIdNumber = null;
        //TODO 身分判別

        try {
            if (account.getAuthority().isStudent()) {
                fileIdNumber = account.getIdNumber();
            }
            if (account.getAuthority().containsRole(
                    Authority.RoleIndex.MANAGER, Authority.RoleIndex.WORK_TEAM, Authority.RoleIndex.TEACHER,Authority.RoleIndex.TUTOR
            )) {
                CourseRecord courseRecord = courseRecordDao.getCourseRecordByID(null, Integer.toString(courseRecordDocument.getCrid()));
                fileIdNumber = studentDao.getStudentByRegNumber(courseRecord.getReg_no()).getIdno();

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        file = new File(Upload.getAccountBaseDir(fileIdNumber), courseRecordDocument.getDocument());

        if (!file.exists() || !file.isFile()) {
            PrintWriter out = response.getWriter();
            out.println("<script>alert('file not exist')</script>");
            out.println("<script>window.history.go(-1)</script>");
            //TODO  警告訊息
            return;
        }

        HttpUtils.sendDownloadFile(response, file, originalname);
    }
}
