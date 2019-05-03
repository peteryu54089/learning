package servlet;

import dao.StudentDao;
import dao.SystemConfigDao;
import dao.impl.StudentDaoImpl;
import dao.impl.SystemConfigDaoImpl;
import model.Account;
import model.Authority;
import model.SystemConfig;
import model.role.Student;
import util.Upload;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/studentBasicInfoModifyServlet")
@MultipartConfig
public class StudentBasicInfoModifyServlet extends _BaseServlet {
    private final int AvatarSizeLimit = 1 << 20;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        if (!checkPermission(request, response))
            return;
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");

        StudentDao studentDao = new StudentDaoImpl(account);
        SystemConfigDao configDao = new SystemConfigDaoImpl(account);
        SystemConfig config = configDao.getSystemConfig();
        Student student = null;

        if (account.getAuthority().isStudent()) {
            student = account.getRoleDetail(Authority.RoleIndex.STUDENT);
        } else {
            String regNo = request.getParameter("regNo");
            try {
                student = studentDao.getStudentByRegNumber(regNo);
            } catch (SQLException e) {
                e.printStackTrace();
                response.setStatus(404);
                return;
            }
        }

        request.setAttribute("config", config);
        request.setAttribute("student", student);
        request.setAttribute("avatarSizeLimit", AvatarSizeLimit);

        request.getRequestDispatcher("/WEB-INF/jsp/studentBasicInfoModify.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");

        if (!account.getAuthority().isStudent()) {
            response.setStatus(401);
            return;
        }

        Student student = account.getRoleDetail(Authority.RoleIndex.STUDENT);

        try {
            String phone = request.getParameter("phone");
            String socialAccount = request.getParameter("socialAccount");
            String bio = request.getParameter("bio");
            String nickname = request.getParameter("nickname");
            String interest = request.getParameter("interest");
            String email = request.getParameter("email");
            String altMail = request.getParameter("altMail");
            Part avatar = request.getPart("avatar");
            String avatarPath = student.getAvatar();
            if (avatar != null && avatar.getSize() > 0) {
                if (avatar.getSize() > AvatarSizeLimit) {
                    response.setStatus(400);
                    return;
                } else {
                    Upload upload = new Upload(getServletContext());
                    File dir = new File(StudentAvatarServlet.PATH, account.getIdNumber());
                    avatarPath = upload.uploadFile(dir, avatar);
                }
            }

            student.setSocialAccount(socialAccount);
            student.setBio(bio);
            student.setInterest(interest);
            student.setNickname(nickname);
            student.setEmail(email);
            student.setAltMail(altMail);
            student.setAvatar(avatarPath);
            student.setMobile_telno(phone);

            StudentDao studentDao = new StudentDaoImpl(account);
            if (studentDao.updateStudent(student)) {
                account.updateRoleDetail();
                session.setAttribute("account", account);
            }

            response.setStatus(200);
        } catch (NullPointerException e) {
            e.printStackTrace();
            response.setStatus(500);
        }
    }

    protected boolean checkPermission(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");
        if (account == null || !account.getAuthority().containsRole(
                Authority.RoleIndex.STUDENT, Authority.RoleIndex.MANAGER, Authority.RoleIndex.WORK_TEAM
        )) {
            response.setStatus(401);
            return false;
        }

        return true;
    }
}
