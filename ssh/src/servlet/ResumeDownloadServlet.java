package servlet;


import dao.ResumeDao;
import dao.impl.ResumeDaoImpl;
import dao.impl.StudentDaoImpl;
import model.Account;
import model.Authority;
import model.Resume;
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

@WebServlet("/downloadResume")
public class ResumeDownloadServlet extends _BaseServlet {


    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        this.doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Upload.getSaveRootDir(getServletContext());
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        Integer id = null;
        try {
            id = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException e) {
            response.setStatus(404);
            return;
        }
        Account account = (Account) request.getSession().getAttribute("account");
        ResumeDao resumeDao = new ResumeDaoImpl(account);
        Resume resume = null;
        String idNo = null;

        try {
            if (account.getAuthority().isStudent()) {
                resume = resumeDao.findResume(id, account.getRoleDetail(Authority.RoleIndex.STUDENT).getRegNumber());
            }
            if (account.getAuthority().containsRole(
                    Authority.RoleIndex.TUTOR, Authority.RoleIndex.WORK_TEAM, Authority.RoleIndex.MANAGER
            )) {
                resume = resumeDao.findResume(id, null);
            }
            idNo = new StudentDaoImpl(account).getStudentByRegNumber(resume.getReg_no()).getIdno();
        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
            response.setStatus(404);
            return;
        }


        //TODO: make other can dl file

        //檢查檔案是否存在
        File file = new File(Upload.getAccountBaseDir(idNo), resume.getFile_path());
        if (!file.exists() || !file.isFile()) {
            PrintWriter out = response.getWriter();
            out.println("<script>alert('file not exist')</script>");
            out.println("<script>window.history.go(-1)</script>");
            //TODO  警告訊息
            return;
        }
        //撈原檔名
        String originalname = "Unknown";
        originalname = resume.getOriginal_filename();
        HttpUtils.sendDownloadFile(response, file, originalname);
    }
}
