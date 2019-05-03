package servlet.management;

import dao.CourseRecordUnlockStatusDao;
import dao.impl.CourseRecordUnlockStatusDaoImpl;
import dao.impl.SystemConfigDaoImpl;
import model.Account;
import model.Authority;
import model.CourseRecord;
import util.DateUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/UnlockCourseRecord")
@MultipartConfig
public class UnlockCourseRecordServlet extends BaseManageServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        if (checkPermission(request, response) == null)
            return;
        bindStuClsInfo(request, response);
        request.setAttribute("title", "解鎖課程紀錄");
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");
        if (account.getAuthority()
                .containsRole(
                        Authority.RoleIndex.MANAGER,
                        Authority.RoleIndex.WORK_TEAM
                )) {
            request.getRequestDispatcher("/WEB-INF/jsp/managerPage/unlockCourseRecord.jsp").forward(request, response);
        }
        else
        {
            request.getRequestDispatcher("/WEB-INF/jsp/courseSubmitterUnlockCourseRecord.jsp").forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");

        CourseRecordUnlockStatusDao courseRecordUnlockStatusDao = new CourseRecordUnlockStatusDaoImpl(account);
        String rgno = request.getParameter("rgno");
        try {
            int year =new SystemConfigDaoImpl(account).getSystemConfig().getCourseStudyRecord().getActiveYear();
            int sem =new SystemConfigDaoImpl(account).getSystemConfig().getCourseStudyRecord().getActiveSem();
            if(courseRecordUnlockStatusDao.isInUnlockStatus(Integer.toString(year),Integer.toString(sem),Integer.parseInt(rgno)))
            {
                courseRecordUnlockStatusDao.revokeUnlock(Integer.toString(year),Integer.toString(sem),Integer.parseInt(rgno));
            }
            else
            {
                courseRecordUnlockStatusDao.grantUnlock(Integer.toString(year),Integer.toString(sem),Integer.parseInt(rgno));
            }



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

    protected Account checkPermission(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");
        if (account == null || !account.getAuthority()
                .containsRole(
                        Authority.RoleIndex.MANAGER,
                        Authority.RoleIndex.WORK_TEAM,
                        Authority.RoleIndex.COURSE_SUBMITTER
                )
        ) {
            response.setStatus(401);
            return null;
        }

        return account;
    }
}
