package servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dao.CourseRecordDao;
import dao.CourseRecordUnlockStatusDao;
import dao.impl.CourseRecordDaoImpl;
import dao.impl.CourseRecordUnlockStatusDaoImpl;
import model.Account;
import model.Authority;
import model.CourseRecord;
import model.CourseRecordDocument;
import model.role.Student;
import org.apache.commons.collections4.IterableUtils;
import util.AuthCheck;
import util.Upload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/courseRecordCheckInfo")
public class CourseRecordCheckInfoServlet extends _BaseServlet {


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
            String year = request.getParameter("year");
            CourseRecordDao courseRecordDao = new CourseRecordDaoImpl(account);

            //TODO 改為當前學年/學期
            if (year == null)
                year = "106";
            String semester = request.getParameter("semester");
            if (semester == null)
                semester = "1";
            List<CourseRecord> courseRecordList;
            Student student = account.getRoleDetail(Authority.RoleIndex.STUDENT);
            CourseRecordUnlockStatusDao unlockDao = new CourseRecordUnlockStatusDaoImpl(account);
            String s = null;
            //TODO 更好的取得RGNO
            Integer rgno  =Integer.valueOf(((Student)account.getRoleDetail(Authority.RoleIndex.STUDENT)).getRgno());
            courseRecordList = courseRecordDao.getCourseRecord(rgno, year, semester, s, null, "5", null, false, null, null);
            courseRecordList.addAll(courseRecordDao.getCourseRecord(rgno, year, semester, s, null, "3", null, false, "1", null));
            for (CourseRecord crd : courseRecordList) {
                for (CourseRecordDocument crdd : crd.getCourseRecordDocumentList()) {
                    crdd.setDlLink(Upload.generateDownloadLink(request, account, crdd.getUploadFile()));
                }
            }
            boolean isUnlockStatus = unlockDao.isInUnlockStatus(year,semester, student.getRgno());
            IterableUtils
                    .chainedIterable(courseRecordList)
                    .forEach(x -> x.setIsUnlockStatus(isUnlockStatus));
            Gson gson = new GsonBuilder().create();
            String json = gson.toJson(courseRecordList);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
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

    }
}

