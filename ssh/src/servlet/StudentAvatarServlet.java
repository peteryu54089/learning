package servlet;

import config.Config;
import dao.StudentDao;
import dao.SystemConfigDao;
import dao.UploadFileDao;
import dao.impl.StudentDaoImpl;
import dao.impl.SystemConfigDaoImpl;
import dao.impl.UploadFileDaoImpl;
import model.Account;
import model.Authority;
import model.role.Student;
import util.HttpUtils;
import util.Upload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

@WebServlet("/studentAvatar")
public class StudentAvatarServlet extends _BaseServlet {
    public static final String PATH = "avatar";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Cache-Control", "no-cache");
        File file = null;
        try {
            HttpSession session = request.getSession();
            Account account = (Account) session.getAttribute("account");
            Student student;
            String rgno = null;
            if (account.getAuthority().isStudent()) {
                student = account.getRoleDetail(Authority.RoleIndex.STUDENT);
            } else {
                rgno = request.getParameter("rgno");
                StudentDao studentDao = new StudentDaoImpl(account);
                student = studentDao.getStudentByRgno(Integer.parseInt(rgno));
            }

            if (student == null ) {
                response.setStatus(404);
                return;
            }
            SystemConfigDao systemConfigDao = new SystemConfigDaoImpl(account);
            String schoolcode = systemConfigDao.getSystemConfig().getSchoolInfo().getId();
            file = Paths.get(UploadFileDaoImpl.getWebInfoPath(), Config.STU_PHTOT_PATH, schoolcode, rgno+".jpg").toFile();
            if (file.exists()) {
                HttpUtils.sendFile(response, file);
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendError(404);
    }
}
