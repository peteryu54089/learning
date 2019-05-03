package servlet;

import dao.CourseRecordDao;
import dao.CourseRecordUnlockStatusDao;
import dao.SystemConfigDao;
import dao.impl.CourseRecordDaoImpl;
import dao.impl.CourseRecordUnlockStatusDaoImpl;
import dao.impl.SystemConfigDaoImpl;
import model.*;
import model.role.Student;
import util.AuthCheck;
import util.DateUtils;
import util.Upload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.crypto.Data;
import java.io.IOException;
import java.util.List;

@WebServlet("/courseRecordCheck")
public class CourseRecordCheckServlet extends _BaseServlet {
    //TODO 頁數整合
    public final static Integer RECORD_PER_PAGE = 4;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        if (!AuthCheck.isAuth(request, Authority.RoleIndex.STUDENT)) {
//        	response.sendRedirect("login");
        	request.setAttribute("error", "您無權限使用此系統");
        	request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            return;
        }
        try {
            HttpSession session = request.getSession();
            Account account = (Account) session.getAttribute("account");
            CourseRecordDao courseRecordDao = new CourseRecordDaoImpl(account);
            SystemConfigDao systemConfigDao = new SystemConfigDaoImpl(account);
            SystemConfig systemConfig = systemConfigDao.getSystemConfig();
            Student student = account.getRoleDetail(Authority.RoleIndex.STUDENT);
            String reg_no = student.getRegNumber();
            String id = request.getParameter("id");
            String check = request.getParameter("check");
            String submited = request.getParameter("submited");
            CourseRecordUnlockStatusDao courseRecordUnlockStatusDao = new CourseRecordUnlockStatusDaoImpl(account);
            if (submited == null) {
                //TODO 更好的取得RGNO
                Integer rgno  =Integer.valueOf(((Student)account.getRoleDetail(Authority.RoleIndex.STUDENT)).getRgno());
                CourseRecord courseRecord = courseRecordDao.getCourseRecordByID(rgno, id);
                courseRecord.setCheck(check);
                courseRecordDao.updateCourseRecordByID(rgno, id, courseRecord);
            } else {
                List<CourseRecord> courseRecordList = null;

                String year = request.getParameter("year");
                String semester = request.getParameter("semester");
                //TODO 檢查是否為當學年/學期
                String s = null;
                //TODO 更好的取得RGNO
                Integer rgno  =Integer.valueOf(((Student)account.getRoleDetail(Authority.RoleIndex.STUDENT)).getRgno());
                courseRecordList = courseRecordDao.getCourseRecord(rgno, year, semester, s, null, "3", null, false, "1", null);
                Integer count1 = courseRecordList.size();//目前勾選數量
                Integer count2 = courseRecordDao.getCourseRecord(rgno, year, semester, s, null, "5", null, false, null, null).size();//勾選數量
                boolean active = Integer.toString(systemConfig.getCourseStudyRecord().getActiveYear()).equals(year) && Integer.toString(systemConfig.getCourseStudyRecord().getActiveSem()).equals(semester);
                if(active == false)
                {
                    response.getWriter().write("不開放勾選");
                    return;
                }
                //TODO 抓取各學年/學期上限
                if (count1 + count2 > systemConfig.getCourseStudyRecord().getMaxSubmitAmount()) {
                    response.getWriter().write("數量錯誤");
                    return;
                }

                courseRecordUnlockStatusDao.revokeUnlock(
                        Integer.toString(systemConfig.getCourseStudyRecord().getActiveYear()),
                        Integer.toString(systemConfig.getCourseStudyRecord().getActiveSem()),
                        student.getRgno()
                );
                for (CourseRecord sc : courseRecordList) {
                    sc.setStatus("5");

                    courseRecordDao.updateCourseRecordByID(rgno, sc.getId(), sc);
                }
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

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");
        if (!account.getAuthority().isStudent()) {
//        	response.sendRedirect("login");
        	request.setAttribute("error", "您無權限使用此系統");
        	request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            return;
        }
        SystemConfigDao systemConfigDao = new SystemConfigDaoImpl(account);
        SystemConfig systemConfig = systemConfigDao.getSystemConfig();
        CourseRecordDao courseRecordDao = new CourseRecordDaoImpl(account);
        Student student = account.getRoleDetail(Authority.RoleIndex.STUDENT);
        try {
            Integer page = 1;
            if (request.getParameter("page") != null) {
                page = Integer.parseInt(request.getParameter("page"));
            }
            String year = request.getParameter("year");
            //TODO 改為當前學年/學期
            if (year == null) {
                year = Integer.toString(DateUtils.getCurrentSemester().year);
            }
            String semester = request.getParameter("semester");
            if (semester == null) {
                semester = Integer.toString(DateUtils.getCurrentSemester().semester);
            }
            String name = request.getParameter("courseName");
            List<CourseRecord> courseRecordList = null;
            String s = null;
            //TODO 更好的取得RGNO
            Integer rgno  =Integer.valueOf(((Student)account.getRoleDetail(Authority.RoleIndex.STUDENT)).getRgno());
            courseRecordList = courseRecordDao.getCourseRecord(rgno, year, semester, s, name, "3", null, false, null, page);
            for (CourseRecord crd : courseRecordList) {
                for (CourseRecordDocument crdd : crd.getCourseRecordDocumentList()) {
                    crdd.setDlLink(Upload.generateDownloadLink(request, account, crdd.getUploadFile()));
                }
            }
            Integer pageAmount;
            pageAmount = courseRecordDao.getPageNumber(rgno, year, semester, null, name, "3", null, false);
            //TODO　勾選數量是哪一個... 確認課程成果"勾選"數量
            boolean active = Integer.toString(systemConfig.getCourseStudyRecord().getActiveYear()).equals(year) && Integer.toString(systemConfig.getCourseStudyRecord().getActiveSem()).equals(semester);

            request.setAttribute("active",active);
            request.setAttribute("maxAmount", systemConfig.getCourseStudyRecord().getMaxSubmitAmount());
            request.setAttribute("year", year);
            request.setAttribute("semester", semester);
            request.setAttribute("courseName", name);
            request.setAttribute("page", page);
            request.setAttribute("pageAmount", pageAmount);
            request.setAttribute("courseRecordList", courseRecordList);
            request.setAttribute("config", systemConfig);
            request.setAttribute("DateUtils", new DateUtils());
            request.getRequestDispatcher("/WEB-INF/jsp/courseRecordCheck.jsp").forward(request, response);

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

